package com.mavyy.yukilockdown;
import android.app.*;
import android.app.usage.*;
import android.content.*;
import android.content.pm.*;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.view.inputmethod.*;
import java.time.*;
import java.util.*;
public final class Device {
 public static boolean usageAllowed(Context c){return c.getSystemService(AppOpsManager.class).checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,android.os.Process.myUid(),c.getPackageName())==AppOpsManager.MODE_ALLOWED;}
 public static boolean accessibilityEnabled(Context c){String s=Settings.Secure.getString(c.getContentResolver(),Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);if(s==null)return false;for(String id:s.split(":")){ComponentName n=ComponentName.unflattenFromString(id);if(new ComponentName(c,GuardService.class).equals(n))return true;}return false;}
 public static Set<String>safe(Context c){Set<String>s=new HashSet<>(Arrays.asList(c.getPackageName(),"com.android.systemui","com.android.settings","com.android.permissioncontroller","com.google.android.permissioncontroller","com.android.phone","com.android.emergency","com.google.android.dialer","com.samsung.android.dialer","com.samsung.android.emergency"));for(Intent i:Arrays.asList(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME),new Intent(Settings.ACTION_SETTINGS),new Intent(Intent.ACTION_DIAL))){ResolveInfo r=c.getPackageManager().resolveActivity(i,PackageManager.MATCH_DEFAULT_ONLY);if(r!=null)s.add(r.activityInfo.packageName);}TelecomManager t=c.getSystemService(TelecomManager.class);if(t!=null&&t.getDefaultDialerPackage()!=null)s.add(t.getDefaultDialerPackage());String sms=android.provider.Telephony.Sms.getDefaultSmsPackage(c);if(sms!=null)s.add(sms);for(InputMethodInfo i:c.getSystemService(InputMethodManager.class).getEnabledInputMethodList())s.add(i.getPackageName());return s;}
 public static Map<String,Long>usage(Context c){if(!usageAllowed(c))return Collections.emptyMap();long now=System.currentTimeMillis(),start=Rules.dayStart(ZonedDateTime.now());UsageLedger ledger=new UsageLedger(start,now);UsageEvents events=c.getSystemService(UsageStatsManager.class).queryEvents(start-86400000L,now);UsageEvents.Event e=new UsageEvents.Event();while(events.hasNextEvent()){events.getNextEvent(e);int t=e.getEventType();if(t==UsageEvents.Event.ACTIVITY_RESUMED)ledger.resume(e.getPackageName(),e.getTimeStamp());else if(t==UsageEvents.Event.ACTIVITY_PAUSED||t==UsageEvents.Event.ACTIVITY_STOPPED)ledger.pause(e.getPackageName(),e.getTimeStamp());else if(t==UsageEvents.Event.SCREEN_NON_INTERACTIVE||t==UsageEvents.Event.DEVICE_SHUTDOWN)ledger.stopAll(e.getTimeStamp());}return ledger.finish();}
 public static String label(Context c,String pkg){try{return c.getPackageManager().getApplicationLabel(c.getPackageManager().getApplicationInfo(pkg,0)).toString();}catch(Exception e){return pkg+" (not installed)";}}
 public static void talk(Context c){Rules.Config conf=Store.get(c).load();Intent i=c.getPackageManager().getLaunchIntentForPackage(conf.aiPackage);if(i==null)i=new Intent(Intent.ACTION_VIEW,android.net.Uri.parse(conf.aiUrl));try{c.startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));}catch(ActivityNotFoundException e){android.widget.Toast.makeText(c,"No app can open this destination. Check Settings.",android.widget.Toast.LENGTH_LONG).show();}}
}
