package com.mavyy.yukilockdown;
import java.util.*;
/** Pin the existing Local Yuki continuity signer, never a self-declared package or model token. */
public final class BridgeAuth {
 public static final String CLIENT="com.mavyy.localyuki";
 public static final String CERT="14192d225b5ded98889e3c0bd3f704d94c1d90b931ba89ab0a22f48ac96cdd2f";
 public static boolean permits(boolean enabled,String[] packages,Collection<String> certificates){return enabled&&packages!=null&&packages.length==1&&CLIENT.equals(packages[0])&&certificates.size()==1&&certificates.contains(CERT);}
 private BridgeAuth(){}
}
