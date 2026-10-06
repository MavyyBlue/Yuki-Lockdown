package com.mavyy.yukilockdown;
import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import android.os.SystemClock;
import android.provider.Settings;
import org.json.*;
import java.util.*;
/** SQLite schema 1: atomic configuration, bounded attempts and reboot-scoped bypass. */
public final class Store extends SQLiteOpenHelper {
 private static Store instance;private final Context context;public volatile String error="";
 public static synchronized Store get(Context c){if(instance==null)instance=new Store(c.getApplicationContext());return instance;}
 private Store(Context c){super(c,"yuki.db",null,2);context=c;}
 public void onCreate(SQLiteDatabase d){createBridge(d);d.execSQL("CREATE TABLE config(id INTEGER PRIMARY KEY CHECK(id=1),json TEXT NOT NULL)");d.execSQL("CREATE TABLE attempts(period TEXT PRIMARY KEY,count INTEGER NOT NULL,updated INTEGER NOT NULL)");d.execSQL("CREATE TABLE bypass(id INTEGER PRIMARY KEY CHECK(id=1),until_elapsed INTEGER,boot INTEGER)");}
 public void onUpgrade(SQLiteDatabase d,int old,int next){if(old==1&&next==2)createBridge(d);else throw new IllegalStateException("Unsupported database migration");}
 private static void createBridge(SQLiteDatabase d){d.execSQL("CREATE TABLE bridge_receipt(id TEXT PRIMARY KEY NOT NULL,request_hash TEXT NOT NULL,result TEXT NOT NULL,created INTEGER NOT NULL)");}
 public synchronized Rules.Config load(){try(Cursor r=getReadableDatabase().rawQuery("SELECT json FROM config WHERE id=1",null)){Rules.Config c=r.moveToFirst()?decode(r.getString(0)):new Rules.Config();error="";return c;}catch(Exception e){error="Configuration unavailable. Protection is paused; saved data has not been overwritten.";Rules.Config c=new Rules.Config();c.enabled=false;return c;}}
 public synchronized boolean save(Rules.Config c){if(!error.isEmpty())return false;try{ContentValues v=new ContentValues();v.put("id",1);v.put("json",encode(c));getWritableDatabase().insertWithOnConflict("config",null,v,SQLiteDatabase.CONFLICT_REPLACE);return true;}catch(Exception e){error="Could not save rules. Retry after reopening the app.";return false;}}
 public synchronized int attempt(String period){SQLiteDatabase d=getWritableDatabase();d.beginTransaction();try{d.execSQL("DELETE FROM attempts WHERE updated < ?",new Object[]{System.currentTimeMillis()-8*86400000L});d.execSQL("INSERT OR IGNORE INTO attempts VALUES (?,0,?)",new Object[]{period,System.currentTimeMillis()});d.execSQL("UPDATE attempts SET count=MIN(count+1,999),updated=? WHERE period=?",new Object[]{System.currentTimeMillis(),period});try(Cursor r=d.rawQuery("SELECT count FROM attempts WHERE period=?",new String[]{period})){r.moveToFirst();int n=r.getInt(0);d.setTransactionSuccessful();return n;}}finally{d.endTransaction();}}
 private int boot(){return Settings.Global.getInt(context.getContentResolver(),Settings.Global.BOOT_COUNT,-1);}
 public synchronized boolean bypassActive(){try(Cursor r=getReadableDatabase().rawQuery("SELECT until_elapsed,boot FROM bypass WHERE id=1",null)){return r.moveToFirst()&&boot()>=0&&r.getInt(1)==boot()&&SystemClock.elapsedRealtime()<r.getLong(0);}catch(Exception e){return false;}}
 public synchronized void bypass(int m){ContentValues v=new ContentValues();v.put("id",1);v.put("until_elapsed",SystemClock.elapsedRealtime()+m*60000L);v.put("boot",boot());getWritableDatabase().insertWithOnConflict("bypass",null,v,SQLiteDatabase.CONFLICT_REPLACE);}
 public void clearBypass(){getWritableDatabase().delete("bypass",null,null);}
 public static String encode(Rules.Config c)throws JSONException{
  JSONObject j=new JSONObject();j.put("version",1);j.put("enabled",c.enabled);j.put("websites",c.websites);j.put("reactions",c.reactions);j.put("aiPackage",c.aiPackage);j.put("aiUrl",c.aiUrl);j.put("bypassMinutes",c.bypassMinutes);j.put("bypassWaitSeconds",c.bypassWaitSeconds);
  JSONObject apps=new JSONObject();for(Map.Entry<String,Rules.AppRule> e:c.apps.entrySet()){Rules.AppRule a=e.getValue();JSONObject x=new JSONObject();x.put("name",a.name);x.put("exempt",a.exempt);x.put("blocked",a.blocked);x.put("minutes",a.minutes);apps.put(e.getKey(),x);}j.put("apps",apps);j.put("domains",new JSONArray(c.domains));
  JSONArray schedules=new JSONArray();for(Rules.Schedule s:c.schedules){JSONObject x=new JSONObject();x.put("id",s.id);x.put("name",s.name);x.put("mode",s.mode);x.put("enabled",s.enabled);x.put("strict",s.strict);x.put("start",s.start);x.put("end",s.end);x.put("days",new JSONArray(s.days));x.put("apps",new JSONArray(s.apps));x.put("domains",new JSONArray(s.domains));x.put("warnings",new JSONArray(s.warnings));schedules.put(x);}j.put("schedules",schedules);return j.toString();
 }
 private static Set<String> strings(JSONArray a)throws JSONException{Set<String>s=new TreeSet<>();for(int i=0;i<a.length();i++)s.add(a.getString(i));return s;}
 private static Set<Integer> ints(JSONArray a)throws JSONException{Set<Integer>s=new TreeSet<>();for(int i=0;i<a.length();i++)s.add(a.getInt(i));return s;}
 public static Rules.Config decode(String raw)throws JSONException{
  JSONObject j=new JSONObject(raw);if(j.getInt("version")!=1)throw new JSONException("Unknown version");Rules.Config c=new Rules.Config();c.enabled=j.getBoolean("enabled");c.websites=j.getBoolean("websites");c.reactions=j.getBoolean("reactions");c.aiPackage=j.getString("aiPackage");c.aiUrl=j.getString("aiUrl");c.bypassMinutes=j.getInt("bypassMinutes");c.bypassWaitSeconds=j.getInt("bypassWaitSeconds");if(c.bypassMinutes<1||c.bypassMinutes>60||c.bypassWaitSeconds<5||c.bypassWaitSeconds>120)throw new JSONException("Invalid bypass");
  JSONObject apps=j.getJSONObject("apps");Iterator<String>keys=apps.keys();while(keys.hasNext()){String k=keys.next();JSONObject x=apps.getJSONObject(k);Rules.AppRule a=new Rules.AppRule();a.name=x.getString("name");a.exempt=x.getBoolean("exempt");a.blocked=x.getBoolean("blocked");a.minutes=x.getInt("minutes");if(a.minutes< -1||a.minutes>1440)throw new JSONException("Invalid allowance");c.apps.put(k,a);}
  c.domains=strings(j.getJSONArray("domains"));JSONArray ss=j.getJSONArray("schedules");Set<String>ids=new HashSet<>();for(int i=0;i<ss.length();i++){JSONObject x=ss.getJSONObject(i);Rules.Schedule s=new Rules.Schedule();s.id=x.getString("id");s.name=x.getString("name");s.mode=x.getString("mode");s.enabled=x.getBoolean("enabled");s.strict=x.getBoolean("strict");s.start=x.getInt("start");s.end=x.getInt("end");s.days=ints(x.getJSONArray("days"));s.apps=strings(x.getJSONArray("apps"));s.domains=strings(x.getJSONArray("domains"));s.warnings=ints(x.getJSONArray("warnings"));if(!ids.add(s.id)||s.start<0||s.start>1439||s.end<0||s.end>1439||s.days.stream().anyMatch(v->v<1||v>7)||s.warnings.stream().anyMatch(v->v<1||v>120))throw new JSONException("Invalid schedule");c.schedules.add(s);}return c;
 }
}
