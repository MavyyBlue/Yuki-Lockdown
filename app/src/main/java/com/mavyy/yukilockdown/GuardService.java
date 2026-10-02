package com.mavyy.yukilockdown;
import android.accessibilityservice.AccessibilityService;
import android.content.*;
import android.graphics.PixelFormat;
import android.os.*;
import android.view.*;
import android.view.accessibility.*;
import android.widget.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
public final class GuardService extends AccessibilityService {
 public static volatile boolean connected=false;
 public static volatile String status="Not connected",lastBrowserStatus="No supported address bar observed yet";
 private final Handler handler=new Handler(Looper.getMainLooper());private final ExecutorService worker=Executors.newSingleThreadExecutor();
 private volatile Map<String,Long>usage=Collections.emptyMap();private volatile boolean fetching;private long lastUsage,lastEvent;
 private static java.lang.ref.WeakReference<GuardService> live=new java.lang.ref.WeakReference<>(null);static volatile boolean appVisible;private PocketYuki pocket;
 public static void companionWarning(){GuardService service=live.get();if(service!=null)service.handler.post(()->{if(service.pocket!=null)service.pocket.react("warning");});}
 private WindowManager wm;private View overlay;private String shown="",currentPkg="",currentDomain="",launcher="";private Set<String>safe=Collections.emptySet();
 private final Runnable tick=new Runnable(){public void run(){refreshUsage();inspect();syncPocket();handler.postDelayed(this,1000);}};
 protected void onServiceConnected(){connected=true;live=new java.lang.ref.WeakReference<>(this);wm=getSystemService(WindowManager.class);pocket=new PocketYuki(this,wm);safe=Device.safe(this);android.content.pm.ResolveInfo r=getPackageManager().resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME),0);if(r!=null)launcher=r.activityInfo.packageName;handler.post(tick);Warnings.schedule(this);status="Monitoring selected rules";}
 public void onAccessibilityEvent(AccessibilityEvent e){if(SystemClock.elapsedRealtime()-lastEvent>200){lastEvent=SystemClock.elapsedRealtime();inspect();}}
 private void refreshUsage(){if(!fetching&&SystemClock.elapsedRealtime()-lastUsage>3000){fetching=true;worker.execute(()->{try{usage=Device.usage(this);}catch(Exception e){usage=Collections.emptyMap();}finally{lastUsage=SystemClock.elapsedRealtime();fetching=false;}});}}
 private void inspect(){if(wm==null)return;try{
  if(!getSystemService(PowerManager.class).isInteractive()||getSystemService(android.app.KeyguardManager.class).isKeyguardLocked()){dismiss();return;}
  AccessibilityNodeInfo root=null;for(AccessibilityWindowInfo w:getWindows())if(w.getType()==AccessibilityWindowInfo.TYPE_APPLICATION&&(w.isActive()||w.isFocused())){root=w.getRoot();if(root!=null)break;}if(root==null)root=getRootInActiveWindow();if(root==null||root.getPackageName()==null)return;
  String pkg=root.getPackageName().toString();
  if(overlay!=null&&(pkg.equals(getPackageName())||pkg.equals(launcher)))pkg=currentPkg;
  else{currentPkg=pkg;currentDomain=BrowserDomains.read(pkg,root);}
  if(!currentDomain.isEmpty())lastBrowserStatus="Address bar recognized in "+Device.label(this,pkg);
  Rules.Config c=Store.get(this).load();long used=Device.usageAllowed(this)?usage.getOrDefault(pkg,0L):0;
  Rules.Decision d=Rules.evaluate(c,pkg,currentDomain,safe,ZonedDateTime.now(),used,Store.get(this).bypassActive());
  status=Store.get(this).error.isEmpty()?(Device.usageAllowed(this)?"Monitoring selected rules":"Schedules active; daily limits need Usage access"):Store.get(this).error;
  if(!d.blocked){dismiss();return;}String key=d.key+":"+pkg+":"+currentDomain;if(!key.equals(shown)){dismiss();show(d,c,currentDomain.isEmpty()?Device.label(this,pkg):currentDomain,key);}
 }catch(Exception e){dismiss();status="Intervention unavailable; reopen setup and check permissions";}}
 private void show(Rules.Decision d,Rules.Config c,String target,String key){if(pocket!=null)pocket.hide();int n=Rules.reaction(Store.get(this).attempt(d.key),c.reactions);if(pocket!=null)pocket.react(n>=3?"annoyed":n==2?"warning":d.mode);shown=key;performGlobalAction(GLOBAL_ACTION_HOME);
  String line=n==1?(d.mode.equals("bedtime")?"Darling, it’s bedtime. "+target+" can wait. ♡":d.mode.equals("outdoors")?"Go touch grass, Darling! "+target+" will be here later.":d.mode.equals("focus")?"Stay with your focus plan, Darling. "+target+" is taking a break.":target+" has reached the boundary you chose. Come talk to me instead! ♡"):n==2?"Come on. "+target+" will still exist later.":n==3?"Again, Darling? You asked me to keep "+target+" closed.":"The rule still stands. Let’s step away, Darling.";
  overlay=Intervention.create(this,n>=3?"annoyed":n==2?"warning":d.mode,d.reason,line,n,
   ()->{dismiss();performGlobalAction(GLOBAL_ACTION_HOME);},
   ()->{dismiss();Device.talk(this);},
   ()->{dismiss();startActivity(new Intent(this,MainActivity.class).putExtra("bypass",true).putExtra("strict",d.strict).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));},false);
  WindowManager.LayoutParams p=new WindowManager.LayoutParams(-1,-1,WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,PixelFormat.TRANSLUCENT);p.gravity=Gravity.TOP;p.setTitle("Yuki Lockdown intervention");wm.addView(overlay,p);
 }
 private void dismiss(){if(overlay!=null){try{wm.removeView(overlay);}catch(Exception ignored){}overlay=null;}shown="";}
 public static void resetCompanionPosition(){GuardService service=live.get();if(service!=null)service.handler.post(()->{if(service.pocket!=null)service.pocket.hide();});}
 private void syncPocket(){if(pocket==null)return;boolean systemSetup=currentPkg.equals("com.android.systemui")||currentPkg.equals("com.android.settings")||currentPkg.contains("permissioncontroller");boolean eligible=overlay==null&&!appVisible&&!systemSetup&&getSystemService(PowerManager.class).isInteractive()&&!getSystemService(android.app.KeyguardManager.class).isKeyguardLocked();for(AccessibilityWindowInfo w:getWindows())if(w.getType()==AccessibilityWindowInfo.TYPE_INPUT_METHOD)eligible=false;pocket.sync(eligible,currentPkg);}
 public void onInterrupt(){dismiss();if(pocket!=null)pocket.hide();}
 public void onDestroy(){connected=false;live.clear();status="Disconnected";if(pocket!=null)pocket.hide();handler.removeCallbacksAndMessages(null);worker.shutdownNow();dismiss();super.onDestroy();}
}
