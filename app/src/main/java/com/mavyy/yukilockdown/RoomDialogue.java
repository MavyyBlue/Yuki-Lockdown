package com.mavyy.yukilockdown;

/** Local visual-novel flow. Never evaluates or mutates protection policy. */
public final class RoomDialogue {
 public enum Step { QUIET, GREETING, CHOICES, PANEL, RESPONSE }
 private Step step=Step.QUIET;private int line;private String response="";
 private static final String[] GREETING={"There you are, Darling. Make yourself comfortable. ♡","We can talk for a while, or take care of your boundaries together."};
 public Step step(){return step;}
 public String text(){return step==Step.GREETING?GREETING[line]:step==Step.RESPONSE?response:"What would you like to do, Darling?";}
 public void tap(){switch(step){case QUIET-> {step=Step.GREETING;line=0;}case GREETING->{if(++line>=GREETING.length){line=GREETING.length-1;step=Step.CHOICES;}}case RESPONSE->step=Step.CHOICES;default->{}}}
 public void panel(){step=Step.PANEL;}
 public void returnToDialogue(String text){response=text;step=Step.RESPONSE;}
 public void quiet(){step=Step.QUIET;line=0;}
 public String checkpoint(){return step.name()+":"+line+":"+response;}
 public void restore(String value){try{String[] parts=value.split(":",3);step=Step.valueOf(parts[0]);line=Math.max(0,Math.min(GREETING.length-1,Integer.parseInt(parts[1])));response=parts.length>2?parts[2]:"";}catch(Exception e){quiet();}}
}
