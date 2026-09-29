package com.mavyy.yukilockdown;
import android.view.accessibility.AccessibilityNodeInfo;
import java.util.*;
/** Address-bar IDs only: never searches page text, messages or forms. */
public final class BrowserDomains {
 private static final Map<String,String[]> IDS=new HashMap<>();
 static {for(String p:Arrays.asList("com.android.chrome","com.chrome.beta","com.chrome.dev","com.brave.browser","com.microsoft.emmx"))IDS.put(p,new String[]{p+":id/url_bar"});IDS.put("com.sec.android.app.sbrowser",new String[]{"com.sec.android.app.sbrowser:id/location_bar_edit_text"});for(String p:Arrays.asList("org.mozilla.firefox","org.mozilla.fenix"))IDS.put(p,new String[]{p+":id/mozac_browser_toolbar_url_view",p+":id/mozac_browser_toolbar_edit_url_view"});}
 public static String read(String pkg,AccessibilityNodeInfo root){String[] ids=IDS.get(pkg);if(ids==null||root==null)return "";for(String id:ids)for(AccessibilityNodeInfo n:root.findAccessibilityNodeInfosByViewId(id)){String host=n.getText()==null?"":Rules.domain(n.getText().toString());if(!host.isEmpty())return host;}return "";}
}
