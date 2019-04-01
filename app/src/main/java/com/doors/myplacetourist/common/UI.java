package com.doors.myplacetourist.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doors.myplacetourist.R;
import com.doors.myplacetourist.rest.pojo.Sticker;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class UI {
    private static final String TAG = "UItag";

    public enum Mode {
        manual,
        uiOff,
        logOn
    }


    public enum Task {
        noTask,
        capture2upload,
        success,
        done,
        failed,
        checked
    }
    private Mode currentMode;
    private SharedPreferences shPref;

    private Activity a;
    private LayoutInflater layoutInflater;
    private View.OnClickListener mOnClickListener;

    private Button takePictureBtn;
    private Button settingsBtn;
    private Button burgerBtn;
    private TextView progressText;
    private ImageView photoPreview;
    private View frame;
    private View tripLogo;
    private CircularProgressBar progressBar;

    private Sticker.ObjectsInfoBean.StickerBean mSticker;

    private RelativeLayout marker;
    private LinearLayout markerList;

    private boolean isTripChecked = false;

    public Mode getCurrentMode() {return currentMode;}

    public ImageView getPhotoPreview() {return photoPreview;}
    public TextView getProgressText() {return progressText;}
    public Button getTakePictureBtn() {return takePictureBtn;}
    public CircularProgressBar getProgressBar() {return progressBar;}
    public View getTripLogo() {return tripLogo;}
    public boolean isTripChecked() {
        return isTripChecked;
    }

    public UI(Mode mode, Context context, View.OnClickListener onClickListener) {
        a = (Activity)context;
        mOnClickListener = onClickListener;
        layoutInflater = a.getLayoutInflater();

        shPref = a.getSharedPreferences(Values.SETTINGS_PREFS,Context.MODE_PRIVATE);

        takePictureBtn = a.findViewById(R.id.phoLay_btnTakePictures);
        settingsBtn = a.findViewById(R.id.phoLay_btnMarkerSettings);
        burgerBtn = a.findViewById(R.id.phoLay_btnSettings);
        progressBar = a.findViewById(R.id.phoLay_progressBar);
        progressText = a.findViewById(R.id.phoLay_progressText);
        photoPreview = a.findViewById(R.id.phoLay_photo);
        frame = a.findViewById(R.id.phoLay_IV_frame);

        marker = a.findViewById(R.id.phoLay_marker);
        markerList = a.findViewById(R.id.phoLay_marker_LL_3);
        tripLogo = a.findViewById(R.id.phoLay_marker_V_1);

        takePictureBtn.setOnClickListener(onClickListener);
        settingsBtn.setOnClickListener(onClickListener);
        burgerBtn.setOnClickListener(onClickListener);

        switch (mode) {
            case logOn:

                break;
            case manual:
                //do something
                break;
            case uiOff:
                settingsBtn.setVisibility(View.INVISIBLE);
                burgerBtn.setVisibility(View.INVISIBLE);
                progressText.setVisibility(View.INVISIBLE);
                photoPreview.setVisibility(View.INVISIBLE);
                a.findViewById(R.id.phoLay_IV_bottom).setVisibility(View.INVISIBLE);
                a.findViewById(R.id.phoLay_IV_frame).setVisibility(View.INVISIBLE);
                a.findViewById(R.id.phoLay_IV_logo).setVisibility(View.INVISIBLE);
                break;
        }
        currentMode = mode;
    }

    public void changeUi(Task task) {
        switch (currentMode) {
            case logOn:
            case manual:
                switch (task) {
                    case capture2upload:
                        takePictureBtn.setBackgroundResource(R.drawable.btn_tp_s);
                        takePictureBtn.setText(R.string.stop);
                        frame.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setProgress(0);
                        progressText.setText("0%");
                        photoPreview.setVisibility(View.VISIBLE);
                        burgerBtn.setEnabled(false);
                        settingsBtn.setEnabled(false);
                        break;
                    case success:
                        marker.setVisibility(View.VISIBLE);
                        takePictureBtn.setBackgroundResource(R.drawable.btn_tp_ok);
                        takePictureBtn.setText(R.string.ok);
                        settingsBtn.setEnabled(true);
                        break;
                    case checked:
                        marker.setVisibility(View.INVISIBLE);
                        takePictureBtn.setOnClickListener(mOnClickListener);
                        mSticker = null;
                    case done:
                        photoPreview.setVisibility(View.INVISIBLE);
                        photoPreview.setImageDrawable(null);
                    case failed:
                        takePictureBtn.setBackgroundResource(R.drawable.btn_tp);
                        takePictureBtn.setText("");
                        progressBar.setVisibility(View.INVISIBLE);
                        progressText.setText("");
                        frame.setVisibility(View.VISIBLE);
                        burgerBtn.setEnabled(true);
                        settingsBtn.setEnabled(true);
                        progressBar.setVisibility(View.INVISIBLE);
                        break;
                }
                break;
        }
    }


    public void updateMarkerViaSettings(boolean isTripAdvisorLink) {
        tripLogo = a.findViewById(R.id.phoLay_marker_V_1);
        if(isTripAdvisorLink){
            tripLogo.setVisibility(View.VISIBLE);
            marker.setOnClickListener(mOnClickListener);
        }
        else {
            tripLogo.setVisibility(View.INVISIBLE);
            marker.setOnClickListener(null);
        }
        if(markerList.getChildCount() > 1)
            markerList.removeViews(1,markerList.getChildCount()-1);

        if(mSticker!=null)
            ((TextView)a.findViewById(R.id.marker_TV_name)).setText(mSticker.getSticker_text());

        Map<String,?> mapOfSet = shPref.getAll();
        if(mapOfSet.isEmpty())
            return;

        List<Integer> settingsBtns = Arrays.stream(Values.getSettingsBtns()).boxed().collect(Collectors.toList());
        int[] markerFields = Values.getMarkerIds();
        boolean[] btnsSel = new boolean[markerFields.length];

        mapOfSet.forEach((BiConsumer<String, Object>) (s, o) -> {
            Tools.log(TAG,"setupPreferences", "key: " + s + " value: " + o);
            int id = Integer.decode(s);
            if (settingsBtns.contains(id))
                btnsSel[settingsBtns.indexOf(id)] = (Boolean) o;
        });

        boolean ratingFbAdded = false;
        boolean priceKitchenAdded = false;
        for(int i = 0; i < btnsSel.length; i++) {
            int id = markerFields[i];
            if(id == 0) continue;
            if(btnsSel[i]) {
                if(id == Values.getMarkerId(Values.Key.RATING) || id == Values.getMarkerId(Values.Key.FEEDBACK_AMOUNT)) {
                    if(!ratingFbAdded){
                        ratingFbAdded =true;
                        markerList.addView(layoutInflater.inflate(Values.getField(id),null));
                    }
                } else if(id == Values.getMarkerId(Values.Key.PRICE) || id == Values.getMarkerId(Values.Key.KITCHEN)) {
                    if(!priceKitchenAdded) {
                        priceKitchenAdded = true;
                        markerList.addView(layoutInflater.inflate(Values.getField(id), null));
                    }
                }else{
                    markerList.addView(layoutInflater.inflate(Values.getField(id),null));
                }
                a.findViewById(id).setVisibility(View.VISIBLE);
                updateMarkerUnit(id);
            } else {
                if(a.findViewById(id)!=null) {
                    a.findViewById(id).setVisibility(View.INVISIBLE);
                    if(id == Values.getMarkerId(Values.Key.FEEDBACK_AMOUNT)) {
                        a.findViewById(R.id.marker_TV_fbAmountPostfix).setVisibility(View.INVISIBLE);
                    }
                    if (id == Values.getMarkerId(Values.Key.RATING)) {
                        a.findViewById(R.id.marker_RB_rating).setVisibility(View.INVISIBLE);
                    }
                    //да я знаю что это отвратительно и что нужно переписать это как можно скорее
                }
            }
            if(id == Values.getMarkerId(Values.Key.SITE)) {
                isTripChecked = btnsSel[i];
            }
        }
    }

    private void updateMarkerUnit(int resId) {
        if(mSticker == null) return;
        switch (resId) {
            case R.id.marker_RB_rating:
                String rating = checkForNf(mSticker.getRating());
                if(rating.equals(a.getString(R.string.marker_rating_nf)))
                    ((RatingBar)a.findViewById(resId)).setRating(0);
                else
                    ((RatingBar)a.findViewById(resId)).setRating(Float.parseFloat(rating));
                break;
            case R.id.marker_TV_fbAmount:
                String fba = checkForNf(mSticker.get_$FeedbackAmount52());
                ((TextView)a.findViewById(resId)).setText(fba);
                if (fba.equals(a.getString(R.string.marker_feedbacks_nf))) {
                    ((TextView)a.findViewById(R.id.marker_TV_fbAmountPostfix)).setText("");
                } else {
                    ((TextView)a.findViewById(R.id.marker_TV_fbAmountPostfix)).setText(R.string.tripAdv_feedbacks);
                }
                break;
            case R.id.marker_TV_price:
                ((TextView)a.findViewById(resId)).setText(checkForNf(mSticker.get_$PriceCategory101()));
                break;
            case R.id.marker_TV_address:
                ((TextView)a.findViewById(resId)).setText(checkForNf(mSticker.getAddress()));
                break;
            case R.id.marker_TV_phone:
                ((TextView)a.findViewById(resId)).setText(checkForNf(mSticker.get_$PhoneNumber276()));
                break;
            case R.id.marker_TV_site:
                Uri uri = Uri.parse(mSticker.getPath());
                ((TextView)a.findViewById(resId)).setText(uri.getAuthority());
        }
    }

    public void setSticker(Sticker sticker) {
        Tools.log(TAG,"setSticker", sticker.stickerToString());
        mSticker = sticker.getSticker();
    }

    public void showMessageBox(Object title, Object message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(a);

        if(title.getClass()==String.class)
            builder.setTitle((String)title);
        else if(title.getClass()==Integer.class)
            builder.setTitle((Integer)title);

        if(message.getClass()==String.class)
            builder.setMessage((String)title);
        else if(message.getClass()==Integer.class)
            builder.setMessage((Integer)title);

        builder.setCancelable(false)
                .setNegativeButton("OK",
                        (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showMessageBox(Object title, Object message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(a);

        if(title.getClass()==String.class)
            builder.setTitle((String)title);
        else if(title.getClass()==Integer.class)
            builder.setTitle((Integer)title);

        if(message.getClass()==String.class)
            builder.setMessage((String)message);
        else if(message.getClass()==Integer.class)
            builder.setMessage((Integer)message);

        builder.setCancelable(false)
                .setPositiveButton("OK", onClickListener);
        AlertDialog alert = builder.create();
        alert.show();
    }

    private String checkForNf(String stickerField) {
        for(int i = 0; i < Values.getnFStrings().length; i++) {
            if(stickerField.equals(a.getString(Values.getnFStrings()[i])))
                return a.getString(Values.getnFStringsLoc()[i]);
        }
        return stickerField;
    }
}
