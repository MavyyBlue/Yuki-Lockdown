package com.mavyy.yukilockdown;
import java.net.*;
import java.time.*;
import java.util.*;
/** Pure policy: every external input, including time and usage, is supplied by the caller. */
public final class Rules {
 public static final class AppRule { public String name=""; public boolean exempt,blocked; public int minutes=-1; }
 public static final class Schedule {
  public String id=UUID.randomUUID().toString(),name="Bedtime",mode="bedtime";
  public boolean enabled=true,strict=false;
  public int start=0,end=480;
  public Set<Integer> days=new TreeSet<>(Arrays.asList(1,2,3,4,5,6,7)),warnings=new TreeSet<>(Arrays.asList(5,15));
  public Set<String> apps=new TreeSet<>(),domains=new TreeSet<>();
  public LocalDate occurrence(ZonedDateTime now) {
   if(!enabled)return null;
   int m=now.getHour()*60+now.getMinute();LocalDate d=now.toLocalDate();
   if(start<end)return m>=start&&m<end&&days.contains(d.getDayOfWeek().getValue())?d:null;
   if(m<end)d=d.minusDays(1);else if(m<start)return null;
   return days.contains(d.getDayOfWeek().getValue())?d:null;
  }
  public ZonedDateTime nextStart(ZonedDateTime now){if(!enabled)return null;for(int i=0;i<=7;i++){LocalDate d=now.toLocalDate().plusDays(i);ZonedDateTime t=d.atTime(start/60,start%60).atZone(now.getZone());if(days.contains(d.getDayOfWeek().getValue())&&t.isAfter(now))return t;}return null;}
 }
 public static final class Config {
  public Map<String,AppRule> apps=new TreeMap<>();public Set<String> domains=new TreeSet<>();public List<Schedule>schedules=new ArrayList<>();
  public boolean enabled=true,websites=true,reactions=true;
  public String aiPackage="com.openai.chatgpt",aiUrl="https://chatgpt.com";
  public int bypassMinutes=5,bypassWaitSeconds=15;
 }
 public static final class Decision {
  public final boolean blocked,strict;public final String reason,key,mode;
  Decision(boolean b,String r,String k,String m,boolean s){blocked=b;reason=r;key=k;mode=m;strict=s;}
 }
 public static Decision allow(String reason){return new Decision(false,reason,"","neutral",false);}
 public static Decision evaluate(Config c,String pkg,String domain,Set<String> safe,ZonedDateTime now,long used,boolean bypass){
  if(safe.contains(pkg))return allow("System-safe exemption");AppRule a=c.apps.get(pkg);
  if(a!=null&&a.exempt)return allow("Always Allowed");
  if(!c.enabled)return allow("Protection paused");if(bypass)return allow("Owner bypass");
  List<Schedule> sorted=new ArrayList<>(c.schedules);sorted.sort(Comparator.comparing((Schedule s)->!s.strict).thenComparing(s->s.id));
  for(Schedule s:sorted){LocalDate d=s.occurrence(now);if(d!=null&&(s.apps.contains(pkg)||(c.websites&&matchesAny(domain,s.domains))))return new Decision(true,s.name,"schedule:"+s.id+":"+d,s.mode,s.strict);}
  if(a!=null&&(a.blocked||(a.minutes>=0&&used>=a.minutes*60000L)))return new Decision(true,a.blocked?"Always restricted":"Daily allowance reached","daily:"+now.toLocalDate()+":"+pkg,"neutral",false);
  if(c.websites&&matchesAny(domain,c.domains))return new Decision(true,"Restricted website","website:"+now.toLocalDate(),"neutral",false);
  return allow("Available");
 }
 public static boolean matchesAny(String host,Collection<String> domains){if(host==null||host.isEmpty())return false;for(String d:domains)if(host.equals(d)||host.endsWith("."+d))return true;return false;}
 public static String domain(String raw){try{String s=raw.trim().toLowerCase(Locale.ROOT);if(s.contains(" ")||s.isEmpty())return "";URI u=new URI(s.contains("://")?s:"https://"+s);if(!"https".equals(u.getScheme())&&!"http".equals(u.getScheme()))return "";String host=u.getHost();if(host==null)return "";host=IDN.toASCII(host).replaceAll("\\.$","");if(!host.contains(".")||host.contains(":")||host.matches("[0-9.]+")||host.length()>253)return "";for(String label:host.split("\\."))if(!label.matches("[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?"))return "";return host;}catch(Exception e){return "";}}
 public static long dayStart(ZonedDateTime n){return n.toLocalDate().atStartOfDay(n.getZone()).toInstant().toEpochMilli();}
 public static int reaction(int n,boolean enabled){return enabled?Math.min(4,Math.max(1,n)):1;}
}
