package com.doors.myplacetourist;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.doors.myplacetourist.common.Tools;
import com.doors.myplacetourist.common.Values;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;

public class SettingsActivity extends Activity {
    private final static String TAG = "SettingsActivity";
    private String[] urls;
    private SharedPreferences mServerUrlPref;
    private String url;
    private GestureDetectorCompat gestureDetector;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        urls = getResources().getStringArray(R.array.server_urls);
        initUI();
        setupPreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();
        configureASTextView();
    }

    private void configureASTextView() {
        AutoCompleteTextView edittext = findViewById(R.id.menuLayout_ACTextView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, urls);
        edittext.setAdapter(adapter);
        url = mServerUrlPref.getString(Values.SERVER_URL,urls[0]);
        edittext.setText(url);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Tools.log(TAG, "afterTextChanged", s.toString());
                url = s.toString();
            }
        };
        edittext.addTextChangedListener(textWatcher);
    }

    private void initUI() {
        setContentView(R.layout.activity_settings);
        findViewById(R.id.menuLayout_btnBack).setOnClickListener(v->finish());
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

    private void setupPreferences() {
        mServerUrlPref = getSharedPreferences(Values.SERVER_PREFS,MODE_PRIVATE);
        if(!mServerUrlPref.contains(Values.SERVER_URL) || mServerUrlPref.getString(Values.SERVER_URL,"").equals("")) {
            SharedPreferences.Editor shEditor = mServerUrlPref.edit();
            shEditor.putString(Values.SERVER_URL,urls[0]);
            shEditor.apply();
        }
    }

    @Override
    public void finish() {
        if (!url.equals("")) {
            Tools.log(TAG, "on finish", url);
            SharedPreferences.Editor editor = mServerUrlPref.edit();
            editor.putString(Values.SERVER_URL, url);
            editor.apply();
        }
        super.finish();
        //overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
        overridePendingTransition(R.anim.slide_right_out, R.anim.slide_right_in);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
