package com.doors.myplacetourist.common;

import com.doors.myplacetourist.R;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

public final class Values {
    public static final String TAG = "ValuesTag";
    public static  final String
            SETTINGS_PREFS = "settings_pref",
            SERVER_PREFS = "server_pref",
            SERVER_URL = "server_url",
            TRIP_URL = "link";
    public enum  Key {
        RATING,
        FEEDBACK_AMOUNT,
        PRICE,
        KITCHEN,
        SITE,
        PHONE,
        ADDRESS,
        FEEDBACK_LAST
    }
    private final static int[] settingsBtns = {
            R.id.settingsLayout_btnRating,
            R.id.settingsLayout_btnFeedbackAmount,
            R.id.settingsLayout_btnPrice,
            R.id.settingsLayout_btnKitchen,
            R.id.settingsLayout_btnSite,
            R.id.settingsLayout_btnPhone,
            R.id.settingsLayout_btnAddress,
            R.id.settingsLayout_btnFeedbackLast
    };
    private final static int[] markerIds = {
            R.id.marker_RB_rating,
            R.id.marker_TV_fbAmount,
            R.id.marker_TV_price,
            R.id.marker_TV_kitchen,
            R.id.marker_TV_site,
            R.id.marker_TV_phone,
            R.id.marker_TV_address,
            0
    };

    private final static int[] markerFields = {
            R.layout.marker_field_rating_feedback,
            R.layout.marker_field_rating_feedback,
            R.layout.marker_field_price_kitchen,
            R.layout.marker_field_price_kitchen,
            R.layout.marker_field_site,
            R.layout.marker_field_phone,
            R.layout.marker_field_address,
            0
    };

    private final static int[] nFStrings = {
            R.string.json_address_nf,
            R.string.json_feedbacks_nf,
            R.string.json_phone_nf,
            R.string.json_price_nf,
            R.string.json_rating_nf
    };

    private final static int[] nFStringsLoc = {
            R.string.marker_address_nf,
            R.string.marker_feedbacks_nf,
            R.string.marker_phone_nf,
            R.string.marker_price_nf,
            R.string.marker_rating_nf
    };

    public static int getSettingsKey(Key k) {return settingsBtns[k.ordinal()];}

    public static int getMarkerId(Key k) {return markerIds[k.ordinal()];}
    public static int[] getSettingsBtns() {return settingsBtns;}

    public static int[] getMarkerIds() {return markerIds;}
    public static int getField(int markerId) {
        for(int i = 0; i < markerIds.length; i++) {
            if(markerIds[i] == markerId)
                return markerFields[i];
        }
        return 0;
    }

    public static int[] getnFStrings() {return nFStrings;}

    public static int[] getnFStringsLoc() {return nFStringsLoc;}
}
