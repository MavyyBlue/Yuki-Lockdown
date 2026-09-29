package com.mavyy.yukilockdown;
import android.content.*;
public final class RestoreReceiver extends BroadcastReceiver {public void onReceive(Context c,Intent i){Warnings.schedule(c);}}
