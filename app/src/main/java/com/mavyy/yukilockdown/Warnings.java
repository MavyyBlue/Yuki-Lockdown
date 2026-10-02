package com.mavyy.yukilockdown;
import android.Manifest;
import android.app.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.os.Build;
import java.time.*;
import java.util.*;
public final class Warnings {
 public static void schedule(Context c){AlarmManager alarms=c.getSystemService(AlarmManager.class);Intent i=new Intent(c,WarningReceiver.class);PendingIntent old=PendingIntent.getBroadcast(c,1,i,PendingIntent.FLAG_NO_CREATE|PendingIntent.FLAG_IMMUTABLE);if(old!=null){alarms.cancel(old);old.cancel();}Rules.Config conf=Store.get(c).load();if(!conf.enabled)return;
  long best=Long.MAX_VALUE,expires=0;List<String>labels=new ArrayList<>();ZonedDateTime now=ZonedDateTime.now();
  for(Rules.Schedule s:conf.schedules){ZonedDateTime next=s.nextStart(now);for(int n=0;n<2&&next!=null;n++){for(int m:s.warnings){long at=next.minusMinutes(m).toInstant().toEpochMilli();if(at<=System.currentTimeMillis())continue;if(at<best){best=at;expires=next.toInstant().toEpochMilli();labels.clear();}if(at==best){labels.add(m+" min until "+s.name);expires=Math.min(expires,next.toInstant().toEpochMilli());}}next=s.nextStart(next.plusSeconds(1));}}
  if(best==Long.MAX_VALUE)return;i.putExtra("message",String.join(" • ",labels)).putExtra("expires",expires);PendingIntent p=PendingIntent.getBroadcast(c,1,i,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);alarms.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,best,p);
 }
 public static void deliver(Context c,Intent i){if(Store.get(c).load().enabled&&System.currentTimeMillis()<i.getLongExtra("expires",0))GuardService.companionWarning();if(Store.get(c).load().enabled&&System.currentTimeMillis()<i.getLongExtra("expires",0)&&(Build.VERSION.SDK_INT<33||c.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)==PackageManager.PERMISSION_GRANTED)){NotificationManager nm=c.getSystemService(NotificationManager.class);nm.createNotificationChannel(new NotificationChannel("warnings","Upcoming Lockdowns",NotificationManager.IMPORTANCE_DEFAULT));String text=i.getStringExtra("message")+". Start wrapping up, Darling. ♡";PendingIntent open=PendingIntent.getActivity(c,0,new Intent(c,MainActivity.class),PendingIntent.FLAG_IMMUTABLE);nm.notify(10,new Notification.Builder(c,"warnings").setSmallIcon(R.drawable.notification_lock).setContentTitle("Yuki’s reminder").setContentText(text).setStyle(new Notification.BigTextStyle().bigText(text)).setContentIntent(open).setAutoCancel(true).build());}schedule(c);}
}
