package com.mavyy.yukilockdown;
import android.content.*;
public final class WarningReceiver extends BroadcastReceiver {public void onReceive(Context c,Intent i){Warnings.deliver(c,i);}}
