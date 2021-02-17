package com.cilys.linphoneformobile.utils;

import android.media.AudioManager;

import com.cilys.linphoneformobile.BuildConfig;

import org.linphone.core.Address;
import org.linphone.core.Call;
import org.linphone.core.Core;

public class LinphoneUtils {

    public static String getDisplayName(Call core) {
        if (BuildConfig.DEBUG) {
            return "1001";
        }
        if (core == null) {
            return null;
        }
        return getAddressDisplayName(core.getRemoteAddress());
    }

    public static String getAddressDisplayName(Address address) {
        if (address == null) return null;

        String displayName = address.getDisplayName();
        if (displayName == null || displayName.isEmpty()) {
            displayName = address.getUsername();
        }
        if (displayName == null || displayName.isEmpty()) {
            displayName = address.asStringUriOnly();
        }
        return displayName;
    }

    public static void toggleMic(Core core){
        if (core == null) {
            return;
        }
        core.enableMic(!core.micEnabled());
    }

    public static void toggleSpeaker(AudioManager audioManager){

    }

}
