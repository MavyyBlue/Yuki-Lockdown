package com.mavyy.yukilockdown;
import org.junit.Test;
import static org.junit.Assert.*;
public class RoomDialogueTest {
 @Test public void launchIsQuietAndGreetingPrecedesChoices(){RoomDialogue f=new RoomDialogue();assertEquals(RoomDialogue.Step.QUIET,f.step());f.tap();assertEquals(RoomDialogue.Step.GREETING,f.step());String first=f.text();f.tap();assertEquals(RoomDialogue.Step.GREETING,f.step());assertNotEquals(first,f.text());f.tap();assertEquals(RoomDialogue.Step.CHOICES,f.step());}
 @Test public void tappingChoicesDoesNotSkipToAnotherPage(){RoomDialogue f=new RoomDialogue();f.tap();f.tap();f.tap();f.tap();assertEquals(RoomDialogue.Step.CHOICES,f.step());}
 @Test public void panelPreventsDialogueAdvancement(){RoomDialogue f=new RoomDialogue();f.panel();f.tap();assertEquals(RoomDialogue.Step.PANEL,f.step());}
 @Test public void savedPanelReturnsThroughDialogueToChoices(){RoomDialogue f=new RoomDialogue();f.panel();f.returnToDialogue("All saved.");assertEquals(RoomDialogue.Step.RESPONSE,f.step());assertEquals("All saved.",f.text());f.tap();assertEquals(RoomDialogue.Step.CHOICES,f.step());}
 @Test public void backToQuietCanRestartGreeting(){RoomDialogue f=new RoomDialogue();f.returnToDialogue("Hello");f.quiet();f.tap();assertEquals(RoomDialogue.Step.GREETING,f.step());assertTrue(f.text().startsWith("There you are"));}
 @Test public void rotationRetainsGreetingProgress(){RoomDialogue f=new RoomDialogue();f.tap();f.tap();RoomDialogue copy=new RoomDialogue();copy.restore(f.checkpoint());assertEquals(f.step(),copy.step());assertEquals(f.text(),copy.text());copy.tap();assertEquals(RoomDialogue.Step.CHOICES,copy.step());}
 @Test public void checkpointRetainsResponseIncludingColons(){RoomDialogue f=new RoomDialogue();f.returnToDialogue("Status: connected: local");RoomDialogue copy=new RoomDialogue();copy.restore(f.checkpoint());assertEquals(f.text(),copy.text());assertEquals(f.step(),copy.step());}
 @Test public void invalidCheckpointFallsBackToQuiet(){RoomDialogue f=new RoomDialogue();f.restore("unknown");assertEquals(RoomDialogue.Step.QUIET,f.step());}
}
