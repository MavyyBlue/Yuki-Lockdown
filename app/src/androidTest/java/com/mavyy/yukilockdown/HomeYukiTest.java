package com.mavyy.yukilockdown;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/** Run on a disposable device, along with the existing persistence suite. */
@RunWith(AndroidJUnit4.class)
public class HomeYukiTest {
 private View text(View root,String value){if(root instanceof TextView t&&value.equals(t.getText().toString()))return root;if(root instanceof ViewGroup g)for(int i=0;i<g.getChildCount();i++){View found=text(g.getChildAt(i),value);if(found!=null)return found;}return null;}
 private MainActivity launch(){var instrumentation=InstrumentationRegistry.getInstrumentation();Intent i=new Intent(instrumentation.getTargetContext(),MainActivity.class);i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);return (MainActivity)instrumentation.startActivitySync(i);}
 @Test public void choicesNavigateToExistingEditors(){
  for(String[] route:new String[][]{{"Settings","Diagnostics & privacy"},{"Protected apps","Protected Apps"},{"Restricted domains","Add restricted domain"}}){
   MainActivity activity=launch();try{InstrumentationRegistry.getInstrumentation().runOnMainSync(()->{
    HomeYuki yuki=activity.findViewById(android.R.id.content).findViewWithTag(HomeYuki.TAG);assertNotNull(yuki);yuki.openChoices();AlertDialog dialog=yuki.dialogue();assertTrue(dialog.isShowing());
    View choice=text(dialog.getWindow().getDecorView(),route[0]);assertNotNull(choice);choice.performClick();assertFalse(dialog.isShowing());assertNotNull(text(activity.getWindow().getDecorView(),route[1]));
   });}finally{InstrumentationRegistry.getInstrumentation().runOnMainSync(activity::finish);}
  }
 }
 @Test public void closingAndPausingLeaveNoDialogueWindow(){MainActivity activity=launch();try{InstrumentationRegistry.getInstrumentation().runOnMainSync(()->{
  HomeYuki yuki=activity.findViewById(android.R.id.content).findViewWithTag(HomeYuki.TAG);yuki.openChoices();AlertDialog first=yuki.dialogue();yuki.openChoices();assertSame(first,yuki.dialogue());first.getButton(AlertDialog.BUTTON_NEGATIVE).performClick();assertFalse(yuki.choicesOpen());yuki.openChoices();AlertDialog second=yuki.dialogue();yuki.pause();assertFalse(second.isShowing());assertFalse(yuki.choicesOpen());
 });}finally{InstrumentationRegistry.getInstrumentation().runOnMainSync(activity::finish);}}
}
