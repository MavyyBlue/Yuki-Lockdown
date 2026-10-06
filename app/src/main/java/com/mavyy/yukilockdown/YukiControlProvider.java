package com.mavyy.yukilockdown;

import android.content.*;
import android.content.pm.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.*;
import android.provider.Settings;
import org.json.*;
import java.security.*;
import java.time.*;
import java.util.*;

/** Versioned, pinned-certificate IPC. Every call authenticates UID and owner enablement before parsing. */
public final class YukiControlProvider extends ContentProvider {
 private static void fields(JSONObject j,String... names){Set<String> allowed=new HashSet<>(Arrays.asList(names));for(Iterator<String>it=j.keys();it.hasNext();)if(!allowed.contains(it.next()))throw new IllegalArgumentException("Unknown argument field");}
 public static final String AUTHORITY="com.mavyy.yukilockdown.localyuki.v1";
 public boolean onCreate(){return true;}
 private static String hash(byte[] b)throws Exception{byte[] h=MessageDigest.getInstance("SHA-256").digest(b);StringBuilder s=new StringBuilder();for(byte x:h)s.append(String.format(java.util.Locale.ROOT,"%02x",x));return s.toString();}
 private void authenticate(){
  Context c=getContext();int uid=Binder.getCallingUid();PackageManager pm=c.getPackageManager();
  try{
   String[] packages=pm.getPackagesForUid(uid);List<String>certs=new ArrayList<>();
   if(packages!=null&&packages.length==1&&BridgeAuth.CLIENT.equals(packages[0])) {
    PackageInfo info=pm.getPackageInfo(BridgeAuth.CLIENT,Build.VERSION.SDK_INT>=28?PackageManager.GET_SIGNING_CERTIFICATES:PackageManager.GET_SIGNATURES);
    android.content.pm.Signature[] signatures=Build.VERSION.SDK_INT>=28?info.signingInfo.getApkContentsSigners():info.signatures;
    for(android.content.pm.Signature s:signatures)certs.add(hash(s.toByteArray()));
   }
   if(!BridgeAuth.permits(c.getSharedPreferences("local_yuki_bridge",0).getBoolean("enabled",false),packages,certs))throw new SecurityException("Local Yuki control requires owner enablement and the pinned continuity signer");
  }catch(SecurityException e){throw e;}catch(Exception e){throw new SecurityException("Caller authentication failed");}
 }
 private JSONObject state()throws Exception{
  Context c=getContext();Store s=Store.get(c);Rules.Config config=s.load();if(!s.error.isEmpty())throw new IllegalStateException(s.error);
  SharedPreferences p=c.getSharedPreferences("pocket_yuki",0),motion=c.getSharedPreferences("presentation",0),bridge=c.getSharedPreferences("local_yuki_bridge",0);
  JSONObject companion=new JSONObject().put("enabled",p.getBoolean("enabled",true)).put("wander",p.getBoolean("wander",true)).put("sizeDp",p.getInt("size_dp",160)).put("x",p.getFloat("x",.85f)).put("y",p.getFloat("y",.7f));
  JSONObject j=new JSONObject().put("version",1).put("config",new JSONObject(Store.encode(config))).put("companion",companion)
   .put("reduceMotion",motion.getBoolean("reduce_motion",false)).put("bypassActive",s.bypassActive()).put("guardConnected",GuardService.connected)
   .put("dialogue",bridge.getString("dialogue","")).put("reaction",bridge.getString("reaction","idle"));
  JSONArray attempts=new JSONArray();try(Cursor r=s.getReadableDatabase().rawQuery("SELECT period,count,updated FROM attempts ORDER BY period LIMIT 256",null)){while(r.moveToNext())attempts.put(new JSONObject().put("period",r.getString(0)).put("count",r.getInt(1)).put("updated",r.getLong(2)));}j.put("attempts",attempts);
  return j;
 }
 private void validate(Rules.Config c)throws Exception{
  if(c.apps.size()>256||c.domains.size()>256||c.schedules.size()>128)throw new IllegalArgumentException("Rule bounds exceeded");
  if(!c.aiPackage.matches("[A-Za-z][A-Za-z0-9_]*(\\.[A-Za-z0-9_]+)+")||!c.aiUrl.startsWith("https://")||Rules.domain(c.aiUrl).isEmpty())throw new IllegalArgumentException("Invalid AI destination");
  Set<String>safe=Device.safe(getContext());
  for(Map.Entry<String,Rules.AppRule>e:c.apps.entrySet()){
   if(!e.getKey().matches("[A-Za-z][A-Za-z0-9_]*(\\.[A-Za-z0-9_]+)+")||e.getValue().name.length()>160)throw new IllegalArgumentException("Invalid application rule");
   if(safe.contains(e.getKey())&&(e.getValue().blocked||e.getValue().minutes>=0)&&!e.getValue().exempt)throw new IllegalArgumentException("System-safe apps cannot be restricted");
  }
  for(String d:c.domains)if(!d.equals(Rules.domain(d)))throw new IllegalArgumentException("Invalid canonical domain");
  for(Rules.Schedule p:c.schedules){if(p.name.length()<1||p.name.length()>160||p.id.length()>128||!Arrays.asList("bedtime","focus","outdoors","neutral").contains(p.mode)||p.days.isEmpty()||(p.apps.isEmpty()&&p.domains.isEmpty()))throw new IllegalArgumentException("Invalid plan");for(String a:p.apps)if(safe.contains(a))throw new IllegalArgumentException("System-safe app in plan");for(String d:p.domains)if(!d.equals(Rules.domain(d)))throw new IllegalArgumentException("Invalid plan domain");}
 }
 @Override public synchronized Bundle call(String method,String arg,Bundle extras){
  authenticate();return dispatch(method,arg,extras);
 }
 /** Internal package boundary for deterministic integration verification; exported call always authenticates. */
 Bundle dispatch(String method,String arg,Bundle extras) {
  if(!"command".equals(method)||arg!=null||extras==null)throw new IllegalArgumentException("Unknown protocol method");
  String raw=extras.getString("json");if(raw==null||raw.getBytes(java.nio.charset.StandardCharsets.UTF_8).length>65536)throw new IllegalArgumentException("Bounded JSON required");
  try{
   JSONObject request=new JSONObject(raw);if(request.getInt("version")!=1)throw new IllegalArgumentException("Unknown protocol version");
   for(Iterator<String>it=request.keys();it.hasNext();)if(!Arrays.asList("version","id","command","expectedState","arguments").contains(it.next()))throw new IllegalArgumentException("Unknown command field");
   String id=request.getString("id");if(!id.matches("[A-Za-z0-9_.:-]{1,128}"))throw new IllegalArgumentException("Invalid command ID");
   String operation=request.getString("command");Store store=Store.get(getContext());synchronized(store) { JSONObject before=state();String digest=hash(before.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8));String requestHash=hash(raw.getBytes(java.nio.charset.StandardCharsets.UTF_8));
   if(!operation.equals("inspect"))try(Cursor receipt=store.getReadableDatabase().rawQuery("SELECT request_hash,result FROM bridge_receipt WHERE id=?",new String[]{id})){
    if(receipt.moveToFirst()){if("DISPATCHING".equals(receipt.getString(1)))throw new IllegalStateException("Previous dispatch interrupted; inspect state and use a new intent after reconciling the uncertain outcome");if(!requestHash.equals(receipt.getString(0)))throw new IllegalArgumentException("Command ID reused with different contents");Bundle result=new Bundle();result.putString("json",receipt.getString(1));return result;}
   }
   JSONObject response=new JSONObject().put("version",1).put("id",id).put("command",operation);
   JSONObject a=request.optJSONObject("arguments");if(a==null)a=new JSONObject();
   if(!operation.equals("inspect")) {
    if(!digest.equals(request.getString("expectedState")))throw new IllegalStateException("State changed; inspect again");
    Set<String> operations=new HashSet<>(Arrays.asList("setConfig","setApp","setDomain","setPlan","removePlan","setControls","setCompanion","setPresentation","resetAttempts","endBreak","beginBreak","confirmBreak","react","setDialogue"));
    if(!operations.contains(operation))throw new IllegalArgumentException("Unknown typed command");
    ContentValues claim=new ContentValues();claim.put("id",id);claim.put("request_hash",requestHash);claim.put("result","DISPATCHING");claim.put("created",System.currentTimeMillis());store.getWritableDatabase().insertOrThrow("bridge_receipt",null,claim);
    switch(operation){
     case "setConfig":{fields(a,"config");Rules.Config next=Store.decode(a.getJSONObject("config").toString());validate(next);if(!store.save(next))throw new IllegalStateException(store.error);Warnings.schedule(getContext());break;}
     case "setApp":case "setDomain":case "setPlan":case "removePlan":case "setControls":{
      JSONObject config=new JSONObject(Store.encode(store.load()));
      switch(operation) {
       case "setApp":fields(a,"package","rule");String pkg=a.getString("package");JSONObject rule=a.getJSONObject("rule");fields(rule,"name","exempt","blocked","minutes");config.getJSONObject("apps").put(pkg,rule);break;
       case "setDomain":fields(a,"domain","protected");String domain=a.getString("domain");if(!domain.equals(Rules.domain(domain)))throw new IllegalArgumentException("Canonical domain required");Set<String>domains=new TreeSet<>();JSONArray d=config.getJSONArray("domains");for(int i=0;i<d.length();i++)domains.add(d.getString(i));if(a.getBoolean("protected"))domains.add(domain);else domains.remove(domain);config.put("domains",new JSONArray(domains));break;
       case "setPlan":case "removePlan":{
        fields(a,operation.equals("setPlan")?"plan":"id");JSONObject plan=a.optJSONObject("plan");String planId=plan!=null?plan.getString("id"):a.getString("id");
        if(plan!=null)fields(plan,"id","name","mode","enabled","strict","start","end","days","apps","domains","warnings");
        JSONArray prior=config.getJSONArray("schedules"),next=new JSONArray();for(int i=0;i<prior.length();i++)if(!prior.getJSONObject(i).getString("id").equals(planId))next.put(prior.getJSONObject(i));if(plan!=null)next.put(plan);config.put("schedules",next);break;}
       case "setControls":fields(a,"enabled","websites","reactions","aiPackage","aiUrl","bypassMinutes","bypassWaitSeconds");for(Iterator<String>it=a.keys();it.hasNext();){String k=it.next();config.put(k,a.get(k));}break;
      }
      Rules.Config next=Store.decode(config.toString());validate(next);if(!store.save(next))throw new IllegalStateException(store.error);Warnings.schedule(getContext());break;}
     case "setCompanion":{
      for(Iterator<String>it=a.keys();it.hasNext();)if(!Arrays.asList("enabled","wander","sizeDp","x","y","resetPosition").contains(it.next()))throw new IllegalArgumentException("Unknown companion field");
      SharedPreferences.Editor e=getContext().getSharedPreferences("pocket_yuki",0).edit();
      if(a.has("enabled"))e.putBoolean("enabled",a.getBoolean("enabled"));if(a.has("wander"))e.putBoolean("wander",a.getBoolean("wander"));
      if(a.has("sizeDp")){int n=a.getInt("sizeDp");if(n<80||n>240)throw new IllegalArgumentException("Companion size 80..240");e.putInt("size_dp",n);}
      for(String axis:Arrays.asList("x","y"))if(a.has(axis)){double v=a.getDouble(axis);if(!Double.isFinite(v)||v<0||v>1)throw new IllegalArgumentException("Position 0..1");e.putFloat(axis,(float)v);}
      if(a.optBoolean("resetPosition")){e.remove("x").remove("y");}if(!e.commit())throw new IllegalStateException("Companion save failed");GuardService.resetCompanionPosition();break;}
     case "setPresentation":if(a.length()!=1||!a.has("reduceMotion"))throw new IllegalArgumentException("Presentation fields");if(!getContext().getSharedPreferences("presentation",0).edit().putBoolean("reduce_motion",a.getBoolean("reduceMotion")).commit())throw new IllegalStateException("Presentation save failed");break;
     case "resetAttempts":fields(a);store.getWritableDatabase().delete("attempts",null,null);break;
     case "endBreak":fields(a);store.clearBypass();break;
     case "beginBreak":{fields(a,"reason");
      String why=a.getString("reason");if(why.length()<4||why.length()>256)throw new IllegalArgumentException("Break reason required");Rules.Config config=store.load();boolean strict=config.schedules.stream().anyMatch(p->p.strict&&p.occurrence(ZonedDateTime.now())!=null);
      int seconds=Math.max(config.bypassWaitSeconds,strict?60:0);String token=UUID.randomUUID().toString();int boot=Settings.Global.getInt(getContext().getContentResolver(),Settings.Global.BOOT_COUNT,-1);if(boot<0)throw new IllegalStateException("Boot identity unavailable");
      if(!getContext().getSharedPreferences("local_yuki_break",0).edit().putString("token",token).putLong("ready",SystemClock.elapsedRealtime()+seconds*1000L).putInt("boot",boot).commit())throw new IllegalStateException("Break checkpoint failed");response.put("breakToken",token).put("waitSeconds",seconds);break;}
     case "confirmBreak":{fields(a,"token");
      SharedPreferences p=getContext().getSharedPreferences("local_yuki_break",0);long ready=p.getLong("ready",Long.MAX_VALUE);if(!a.getString("token").equals(p.getString("token",""))||p.getInt("boot",-2)!=Settings.Global.getInt(getContext().getContentResolver(),Settings.Global.BOOT_COUNT,-1)||SystemClock.elapsedRealtime()<ready||SystemClock.elapsedRealtime()>ready+300000)throw new IllegalArgumentException("Break token expired or countdown unfinished");
      if(!p.edit().clear().commit())throw new IllegalStateException("Break checkpoint failed");store.bypass(store.load().bypassMinutes);break;}
     case "react":{fields(a,"state");String reaction=a.getString("state");if(!Arrays.asList("idle","warning","annoyed","bedtime","outdoors","focus").contains(reaction))throw new IllegalArgumentException("Invalid reaction");getContext().getSharedPreferences("local_yuki_bridge",0).edit().putString("reaction",reaction).commit();GuardService.companionReact(reaction);break;}
     case "setDialogue":{fields(a,"text");String text=a.getString("text");if(text.length()>512)throw new IllegalArgumentException("Dialogue too long");if(!getContext().getSharedPreferences("local_yuki_bridge",0).edit().putString("dialogue",text).commit())throw new IllegalStateException("Dialogue save failed");MainActivity.bridgeDialogue(text);break;}
     default:throw new IllegalArgumentException("Unknown typed command");
    }
   }
   JSONObject after=state();response.put("success",true).put("state",after).put("stateDigest",hash(after.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8)));
   String result=response.toString();if(!operation.equals("inspect")){ContentValues v=new ContentValues();v.put("id",id);v.put("request_hash",requestHash);v.put("result",result);v.put("created",System.currentTimeMillis());store.getWritableDatabase().update("bridge_receipt",v,"id=?",new String[]{id});}
   Bundle b=new Bundle();b.putString("json",result);return b; }
  }catch(Exception e){Bundle b=new Bundle();try{JSONObject r=new JSONObject().put("version",1).put("success",false).put("error",e.getMessage()==null?"Command rejected":e.getMessage());b.putString("json",r.toString());}catch(Exception ignored){}return b;}
 }
 public Cursor query(Uri u,String[]p,String s,String[]a,String o){throw new SecurityException("Typed call only");}
 public String getType(Uri u){return null;}public Uri insert(Uri u,ContentValues v){throw new SecurityException("Typed call only");}public int delete(Uri u,String s,String[]a){throw new SecurityException("Typed call only");}public int update(Uri u,ContentValues v,String s,String[]a){throw new SecurityException("Typed call only");}
}
