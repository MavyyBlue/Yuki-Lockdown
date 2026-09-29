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
 private WindowManager wm;private View overlay;private String shown="",currentPkg="",currentDomain="",launcher="";private Set<String>safe=Collections.emptySet();
 private final Runnable tick=new Runnable(){public void run(){refreshUsage();inspect();handler.postDelayed(this,1000);}};
 protected void onServiceConnected(){connected=true;wm=getSystemService(WindowManager.class);safe=Device.safe(this);android.content.pm.ResolveInfo r=getPackageManager().resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME),0);if(r!=null)launcher=r.activityInfo.packageName;handler.post(tick);Warnings.schedule(this);status="Monitoring selected rules";}
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
 private void show(Rules.Decision d,Rules.Config c,String target,String key){int n=Rules.reaction(Store.get(this).attempt(d.key),c.reactions);shown=key;performGlobalAction(GLOBAL_ACTION_HOME);
  ScrollView scroll=new ScrollView(this);scroll.setFillViewport(true);scroll.setBackgroundColor(Ui.BG);LinearLayout content=Ui.column(this);content.setPadding(Ui.dp(this,24),Ui.dp(this,36),Ui.dp(this,24),Ui.dp(this,28));scroll.addView(content);
  Ui.text(content,"YUKI LOCKDOWN",13,Ui.BLUE);Ui.text(content,d.reason,28,Ui.WHITE);Ui.character(content,n>=3?"annoyed":n==2?"warning":d.mode,270);
  String line=n==1?(d.mode.equals("bedtime")?"Darling, it’s bedtime. "+target+" can wait. ♡":d.mode.equals("outdoors")?"Go touch grass, Darling! "+target+" will be here later.":d.mode.equals("focus")?"Stay with your focus plan, Darling. "+target+" is taking a break.":target+" has reached the boundary you chose. Come talk to me instead! ♡"):n==2?"Come on. "+target+" will still exist later.":n==3?"Again, Darling? You asked me to keep "+target+" closed.":"The rule still stands. Let’s step away, Darling.";
  Ui.text(content,line,20,Ui.WHITE);Ui.text(content,"Your rule • reaction "+n,13,Ui.MUTED);
  Ui.button(content,"Return Home",()->{dismiss();performGlobalAction(GLOBAL_ACTION_HOME);});Ui.button(content,"Talk to Yuki",()->{dismiss();Device.talk(this);});Ui.button(content,"Emergency bypass / Edit my rules",()->{dismiss();startActivity(new Intent(this,MainActivity.class).putExtra("bypass",true).putExtra("strict",d.strict).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));});
  WindowManager.LayoutParams p=new WindowManager.LayoutParams(-1,-1,WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,PixelFormat.TRANSLUCENT);p.gravity=Gravity.TOP;p.setTitle("Yuki Lockdown intervention");overlay=scroll;wm.addView(overlay,p);
 }
 private void dismiss(){if(overlay!=null){try{wm.removeView(overlay);}catch(Exception ignored){}overlay=null;}shown="";}
 public void onInterrupt(){dismiss();}
 public void onDestroy(){connected=false;status="Disconnected";handler.removeCallbacksAndMessages(null);worker.shutdownNow();dismiss();super.onDestroy();}
}
