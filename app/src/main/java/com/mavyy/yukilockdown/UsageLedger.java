package com.mavyy.yukilockdown;
import java.util.*;
public final class UsageLedger {
 private final long start,end;private final Map<String,Long>open=new HashMap<>(),totals=new HashMap<>();
 public UsageLedger(long s,long e){start=s;end=e;}
 public void resume(String p,long t){open.putIfAbsent(p,t);}
 public void pause(String p,long t){Long since=open.remove(p);if(since!=null)totals.merge(p,Math.max(0,Math.min(end,t)-Math.max(start,since)),Long::sum);}
 public void stopAll(long t){for(String p:new ArrayList<>(open.keySet()))pause(p,t);}
 public Map<String,Long>finish(){stopAll(end);return totals;}
}
