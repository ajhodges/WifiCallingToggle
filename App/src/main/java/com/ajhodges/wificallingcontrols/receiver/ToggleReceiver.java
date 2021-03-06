package com.ajhodges.wificallingcontrols.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ajhodges.wificallingcontrols.Constants;
import com.ajhodges.wificallingcontrols.NotCompatibleException;
import com.ajhodges.wificallingcontrols.bundle.BundleScrubber;
import com.ajhodges.wificallingcontrols.bundle.PluginBundleManager;
import com.ajhodges.wificallingcontrols.ipphone.WifiCallingManager;

/**
 * Created by Adam on 3/9/14.
 */
public class ToggleReceiver extends BroadcastReceiver{
    public static final String TOGGLE_WFC_ACTION = "com.ajhodges.wificallingcontrols.TOGGLE_WFC";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!(com.twofortyfouram.locale.Intent.ACTION_FIRE_SETTING.equals(intent.getAction()) ||
                                              TOGGLE_WFC_ACTION.equals(intent.getAction()))){
            //unexpected intent
            return;
        }

        BundleScrubber.scrub(intent);
        final Bundle bundle = intent.getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);
        BundleScrubber.scrub(bundle);

        if (PluginBundleManager.isBundleValid(bundle))
        {
            final int mode = bundle.getInt(PluginBundleManager.BUNDLE_EXTRA_INT_MODE);

            try{
                WifiCallingManager wifiCallingManager = WifiCallingManager.getInstance(context);

                if(mode < WifiCallingManager.PREFER_WIFI) {
                    Log.v(Constants.LOG_TAG, "Toggling WFC");
                    wifiCallingManager.toggleWifi(context, mode);
                    context.getSharedPreferences("ToggleWidgetProvider", Context.MODE_PRIVATE).edit().putBoolean("widgetUpdating", false);
                }
                else {
                    wifiCallingManager.setPreferred(context, mode);
                }
            } catch(NotCompatibleException ex){
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
