package com.mavyy.yukilockdown;
import android.content.*;import android.os.*;import android.database.sqlite.*;import org.json.*;
import org.junit.*;import static org.junit.Assert.*;import org.junit.runner.RunWith;
import org.robolectric.*;import org.robolectric.annotation.Config;import org.robolectric.util.ReflectionHelpers;
@RunWith(RobolectricTestRunner.class) @Config(sdk=35)
public class BridgeIntegrationTest {
 Context context;YukiControlProvider provider;
 @Before public void setup(){context=RuntimeEnvironment.getApplication();ReflectionHelpers.setStaticField(Store.class,"instance",null);context.deleteDatabase("yuki.db");context.getSharedPreferences("local_yuki_bridge",0).edit().clear().commit();context.getSharedPreferences("pocket_yuki",0).edit().clear().commit();provider=Robolectric.setupContentProvider(YukiControlProvider.class);}
 @After public void cleanup(){Store.get(context).close();ReflectionHelpers.setStaticField(Store.class,"instance",null);context.deleteDatabase("yuki.db");}
 JSONObject send(String id,String op,JSONObject args,String digest)throws Exception {
  JSONObject q=new JSONObject().put("version",1).put("id",id).put("command",op).put("arguments",args);if(digest!=null)q.put("expectedState",digest);
  Bundle b=new Bundle();b.putString("json",q.toString());return new JSONObject(provider.dispatch("command",null,b).getString("json"));
 }
 JSONObject inspect()throws Exception{return send("inspect","inspect",new JSONObject(),null);}
 String digest()throws Exception{return inspect().getString("stateDigest");}
 @Test public void exportedCallerCannotReachDispatcherWithoutOwnerAndPinnedSigner(){try{provider.call("command",null,new Bundle());fail("Untrusted caller reached dispatch");}catch(SecurityException expected){}}
 @Test public void typedPatchPreservesUnrelatedControlsAndIsReplaySafe()throws Exception {
  String before=digest();JSONObject a=new JSONObject().put("websites",false);JSONObject first=send("patch","setControls",a,before);assertTrue(first.getBoolean("success"));
  JSONObject replay=send("patch","setControls",a,before);assertEquals(first.toString(),replay.toString());assertFalse(Store.get(context).load().websites);assertTrue(Store.get(context).load().enabled);
  assertFalse(send("patch","setControls",new JSONObject().put("enabled",false),before).getBoolean("success"));assertTrue(Store.get(context).load().enabled);
 }
 @Test public void staleInspectionAndAuthorityFieldsCannotChangeConfiguration()throws Exception {
  String old=digest();assertTrue(send("first","setControls",new JSONObject().put("websites",false),old).getBoolean("success"));
  assertFalse(send("stale","setControls",new JSONObject().put("enabled",false),old).getBoolean("success"));assertTrue(Store.get(context).load().enabled);
  assertFalse(send("forged","setControls",new JSONObject().put("grantAccessibility",true),digest()).getBoolean("success"));
 }
 @Test public void interruptedReceiptRefusesAutomaticReplay()throws Exception {
  ContentValues claim=new ContentValues();claim.put("id","interrupted");claim.put("request_hash","irrelevant");claim.put("result","DISPATCHING");claim.put("created",1);Store.get(context).getWritableDatabase().insertOrThrow("bridge_receipt",null,claim);
  JSONObject r=send("interrupted","setControls",new JSONObject().put("enabled",false),digest());assertFalse(r.getBoolean("success"));assertTrue(r.getString("error").contains("interrupted"));assertTrue(Store.get(context).load().enabled);
 }
 @Test public void applicationAndDomainChangesAreGroundedAndBounded()throws Exception {
  JSONObject rule=new JSONObject().put("name","Video").put("exempt",false).put("blocked",false).put("minutes",30);
  assertTrue(send("app","setApp",new JSONObject().put("package","example.video").put("rule",rule),digest()).getBoolean("success"));assertEquals(30,Store.get(context).load().apps.get("example.video").minutes);
  assertTrue(send("domain","setDomain",new JSONObject().put("domain","example.com").put("protected",true),digest()).getBoolean("success"));assertTrue(Store.get(context).load().domains.contains("example.com"));
  assertFalse(send("invalid","setDomain",new JSONObject().put("domain","https://example.com/path").put("protected",true),digest()).getBoolean("success"));
 }
 @Test public void companionSizeAndBreakCountdownCannotBypassInvariants()throws Exception {
  assertFalse(send("size","setCompanion",new JSONObject().put("sizeDp",10000),digest()).getBoolean("success"));assertEquals(160,context.getSharedPreferences("pocket_yuki",0).getInt("size_dp",160));
  assertFalse(send("break","confirmBreak",new JSONObject().put("token","forged"),digest()).getBoolean("success"));assertFalse(Store.get(context).bypassActive());
 }
 @Test public void schemaOneUpgradeKeepsConfigurationAndCreatesReceipts()throws Exception {
  Store s=Store.get(context);Rules.Config c=s.load();c.websites=false;assertTrue(s.save(c));s.close();ReflectionHelpers.setStaticField(Store.class,"instance",null);
  SQLiteDatabase db=SQLiteDatabase.openDatabase(context.getDatabasePath("yuki.db").getPath(),null,0);db.execSQL("DROP TABLE bridge_receipt");db.setVersion(1);db.close();
  assertFalse(Store.get(context).load().websites);assertEquals(2,Store.get(context).getReadableDatabase().getVersion());assertTrue(inspect().getBoolean("success"));
 }
}
