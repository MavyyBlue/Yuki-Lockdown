package com.mavyy.yukilockdown;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;
public class BridgeAuthTest {
 @Test public void pinnedSignerAndOwnerGrantRequired(){assertTrue(BridgeAuth.permits(true,new String[]{BridgeAuth.CLIENT},Set.of(BridgeAuth.CERT)));assertFalse(BridgeAuth.permits(false,new String[]{BridgeAuth.CLIENT},Set.of(BridgeAuth.CERT)));}
 @Test public void forgedPackageSharedUidAndWrongSignersFail(){assertFalse(BridgeAuth.permits(true,new String[]{"evil.app"},Set.of(BridgeAuth.CERT)));assertFalse(BridgeAuth.permits(true,new String[]{BridgeAuth.CLIENT,"evil.app"},Set.of(BridgeAuth.CERT)));assertFalse(BridgeAuth.permits(true,new String[]{BridgeAuth.CLIENT},Set.of("wrong")));assertFalse(BridgeAuth.permits(true,null,Set.of(BridgeAuth.CERT)));assertFalse(BridgeAuth.permits(true,new String[]{BridgeAuth.CLIENT},Set.of(BridgeAuth.CERT,"wrong")));}
}
