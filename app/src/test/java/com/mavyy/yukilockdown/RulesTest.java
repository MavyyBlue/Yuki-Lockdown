package com.mavyy.yukilockdown;
import org.junit.Test;
import static org.junit.Assert.*;
import java.time.*;
import java.util.*;
public class RulesTest {
 private final Rules.Config c=new Rules.Config();
 private ZonedDateTime at(String s){return LocalDateTime.parse(s).atZone(ZoneId.of("America/Chicago"));}
 private Rules.Schedule schedule(){Rules.Schedule s=new Rules.Schedule();s.apps.add("video");c.schedules.add(s);return s;}
 private Rules.Decision eval(String t,long used){return Rules.evaluate(c,"video","",Collections.emptySet(),at(t),used,false);}
 @Test public void scheduleOverridesAllowance(){schedule();Rules.AppRule a=new Rules.AppRule();a.minutes=45;c.apps.put("video",a);assertTrue(eval("2026-09-28T00:00",0).blocked);}
 @Test public void overnightStartDay(){Rules.Schedule s=schedule();s.start=1320;s.end=480;s.days=new TreeSet<>(Arrays.asList(1));assertTrue(eval("2026-09-29T07:59",0).blocked);assertFalse(eval("2026-09-29T22:00",0).blocked);}
 @Test public void endExclusive(){schedule();assertTrue(eval("2026-09-28T00:00",0).blocked);assertFalse(eval("2026-09-28T08:00",0).blocked);}
 @Test public void equalTimesFullDay(){Rules.Schedule s=schedule();s.start=480;s.end=480;s.days=new TreeSet<>(Arrays.asList(1));assertTrue(eval("2026-09-29T07:59",0).blocked);assertFalse(eval("2026-09-29T08:00",0).blocked);}
 @Test public void permanentExemption(){schedule();Rules.AppRule a=new Rules.AppRule();a.exempt=true;a.blocked=true;c.apps.put("video",a);assertFalse(eval("2026-09-28T01:00",999999).blocked);}
 @Test public void safeExemption(){schedule();assertFalse(Rules.evaluate(c,"video","",Collections.singleton("video"),at("2026-09-28T01:00"),999999,false).blocked);}
 @Test public void bypass(){schedule();assertFalse(Rules.evaluate(c,"video","",Collections.emptySet(),at("2026-09-28T01:00"),0,true).blocked);}
 @Test public void exactDailyLimit(){Rules.AppRule a=new Rules.AppRule();a.minutes=45;c.apps.put("video",a);assertFalse(eval("2026-09-28T12:00",2699999).blocked);assertTrue(eval("2026-09-28T12:00",2700000).blocked);}
 @Test public void negativeAllowanceScheduleOnly(){Rules.AppRule a=new Rules.AppRule();c.apps.put("video",a);assertFalse(eval("2026-09-28T12:00",99999999).blocked);}
 @Test public void zeroAllowance(){Rules.AppRule a=new Rules.AppRule();a.minutes=0;c.apps.put("video",a);assertTrue(eval("2026-09-28T12:00",0).blocked);}
 @Test public void disabled(){schedule().enabled=false;assertFalse(eval("2026-09-28T01:00",0).blocked);}
 @Test public void paused(){schedule();c.enabled=false;assertFalse(eval("2026-09-28T01:00",0).blocked);}
 @Test public void overlapDeterministic(){Rules.Schedule a=schedule();a.id="a";Rules.Schedule b=schedule();b.id="b";b.strict=true;assertTrue(eval("2026-09-28T01:00",0).strict);Collections.reverse(c.schedules);assertTrue(eval("2026-09-28T01:00",0).key.contains(":b:"));}
 @Test public void domainNormalization(){assertEquals("youtube.com",Rules.domain("https://YouTube.com/watch?q=1"));assertEquals("",Rules.domain("javascript:alert(1)"));}
 @Test public void domainBoundary(){assertTrue(Rules.matchesAny("m.youtube.com",Arrays.asList("youtube.com")));assertFalse(Rules.matchesAny("notyoutube.com",Arrays.asList("youtube.com")));}
 @Test public void websiteSchedule(){Rules.Schedule s=schedule();s.domains.add("youtube.com");assertTrue(Rules.evaluate(c,"browser","m.youtube.com",Collections.emptySet(),at("2026-09-28T01:00"),0,false).blocked);assertFalse(Rules.evaluate(c,"browser","m.youtube.com",Collections.emptySet(),at("2026-09-28T12:00"),0,false).blocked);}
 @Test public void browserExemption(){c.domains.add("youtube.com");Rules.AppRule a=new Rules.AppRule();a.exempt=true;c.apps.put("browser",a);assertFalse(Rules.evaluate(c,"browser","youtube.com",Collections.emptySet(),at("2026-09-28T01:00"),0,false).blocked);}
 @Test public void reactions(){assertEquals(1,Rules.reaction(0,true));assertEquals(4,Rules.reaction(99,true));assertEquals(1,Rules.reaction(99,false));}
 @Test public void stableOvernightPeriod(){Rules.Schedule s=schedule();s.start=1320;assertEquals(eval("2026-09-28T23:00",0).key,eval("2026-09-29T01:00",0).key);}
 @Test public void dailyPeriodChanges(){Rules.AppRule a=new Rules.AppRule();a.blocked=true;c.apps.put("video",a);assertNotEquals(eval("2026-09-28T23:00",0).key,eval("2026-09-29T00:00",0).key);}
 @Test public void daylightSavingReset(){assertEquals(23*3600000L,Rules.dayStart(at("2026-03-09T12:00"))-Rules.dayStart(at("2026-03-08T12:00")));}
 @Test public void nextWeek(){Rules.Schedule s=schedule();s.days=new TreeSet<>(Arrays.asList(1));assertEquals(LocalDate.of(2026,10,5),s.nextStart(at("2026-09-28T01:00")).toLocalDate());}
 @Test public void usageClipsMidnight(){UsageLedger l=new UsageLedger(1000,3000);l.resume("video",500);l.pause("video",2000);assertEquals(Long.valueOf(1000),l.finish().get("video"));}
 @Test public void duplicateResumeAndScreenOff(){UsageLedger l=new UsageLedger(0,3000);l.resume("video",1000);l.resume("video",1500);l.stopAll(2000);assertEquals(Long.valueOf(1000),l.finish().get("video"));}
 @Test public void emptyUsage(){assertTrue(new UsageLedger(0,1000).finish().isEmpty());}
}
