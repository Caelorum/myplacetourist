package com.doors.myplacetourist;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.doors.myplacetourist.common.Tools;
import com.doors.myplacetourist.common.Values;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;

public class MarkerSettingsActivity extends Activity {
    private final static String TAG = "SettingsActivityTag";
    private SharedPreferences mSettPref;
    private GestureDetectorCompat gestureDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        setupPreferences();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void setupPreferences() {
        List<Integer> btnsIdList = Arrays.stream(Values.getSettingsBtns()).boxed().collect(Collectors.toList());
        mSettPref = getSharedPreferences("settings_pref",MODE_PRIVATE);
        Map<String, ?> settingsMap = mSettPref.getAll();
        if(!settingsMap.isEmpty()) {
            settingsMap.forEach((BiConsumer<String, Object>) (s, o) -> {
                Tools.log(TAG,"setupPreferences", "key: " + s + " value: " + o);
                int id = Integer.decode(s);
                if (btnsIdList.contains(id)) {
                    boolean isSelected = (Boolean) o;
                    if(isSelected){
                        findViewById(id).setSelected(isSelected);
                    }
                }
            });
        }
    }


    private void initUI() {
        setContentView(R.layout.activity_marker_settings);
        findViewById(R.id.settingsLayout_btnBack).setOnClickListener((v -> finish()));

        for(int btnId : Values.getSettingsBtns()) {
            findViewById(btnId).setOnClickListener(this::configure);
        }
        gestureDetector = new GestureDetectorCompat(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(e2.getX() > e1.getX()){
                    finish();
                }
                return true;
            }
        });
    }

    private void configure(View v) {
        v.setSelected(!v.isSelected());
        SharedPreferences.Editor editor = mSettPref.edit();
        editor.putBoolean(String.valueOf(v.getId()),v.isSelected());
        editor.apply();
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
        overridePendingTransition(R.anim.slide_right_out, R.anim.slide_right_in);
    }
}
