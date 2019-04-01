package com.doors.myplacetourist.common;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.doors.myplacetourist.R;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;

public class ErrorDialog extends Dialog implements View.OnClickListener {
    private final static String TAG = "ErrorDialogTag";
    public static final int
        NO_OBJ = 0,
        NO_INT = 1,
        UNDEF_LOC = 2,
        GEO_PROBL = 3,
        UNK_ERR = 4,
        BAD_REQ = 5,
        SERVER_ERR = 6,
        TASK_CANCELED = 7;

    public static final int
        POSITIVE_BTN = R.id.alertDialog_positiveBtn,
        NEGATIVE_BTN = R.id.alertDialog_negativeBtn;

    private TextView title, message;
    private ImageView image;
    private int mode = -1;
    private Button positiveBtn, negativeBtn;
    private DialogInterface.OnClickListener mOnClickListener;
    public DialogInterface.OnCancelListener mOnCancelListener;
    private GestureDetector gestureDetector;

    public ErrorDialog(@NonNull Context context) {
        super(context);
        getWindow().getAttributes().windowAnimations = R.style.ErrorDialogSlideAnim;
    }

    public ErrorDialog(@NonNull Context context, int msg, OnClickListener onClickListener, OnCancelListener onCancelListener) {
        this(context);
        mode = msg;
        mOnClickListener = onClickListener;
        mOnCancelListener = onCancelListener;
    }

    public static ErrorDialog makeDialog(@NonNull Context context, int msg, OnClickListener onClickListener, OnCancelListener onCancelListener) {
        return new ErrorDialog(context,msg,onClickListener, onCancelListener);
    }

    public static ErrorDialog makeDialog(@NonNull Context context) {
        return new ErrorDialog(context);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setTitle(int title) {
        this.title.setText(title);
    }

    public void setMessage(String message) {
        this.message.setText(message);
    }

    public void setMessage(int message) {
        this.message.setText(message);
    }

    public void setImage(int resID) {
       image.setImageResource(resID);
    }

    public void setPositiveBtnText(String text) {
        positiveBtn.setText(text);
    }

    public void setPositiveBtnText(int text) {
        positiveBtn.setText(text);
    }

    private void initUI() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.error_dialog);
        image = findViewById(R.id.alertDialog_image);
        title = findViewById(R.id.alertDialog_title);
        message = findViewById(R.id.alertDialog_message);
        positiveBtn = findViewById(POSITIVE_BTN);
        negativeBtn = findViewById(NEGATIVE_BTN);
        positiveBtn.setOnClickListener(this);
        negativeBtn.setOnClickListener(this);
        Window window = this.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams params = window.getAttributes();
        params.y = -100;
        setCanceledOnTouchOutside(true);
        gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(e2.getX() > e1.getX()){
                    ErrorDialog.this.cancel();
                }
                return true;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        if(mode>=0)
            setupUI();
    }

    private void setupUI() {
        switch (mode) {
            case NO_OBJ:
                setImage(R.drawable.alert_dialog_no_obj);
                setTitle(R.string.errorDialog_objNotFound_title);
                positiveBtn.setVisibility(View.INVISIBLE);
                break;
            case UNDEF_LOC:
                setImage(R.drawable.alert_dialog_loc_not_def);
                setTitle(R.string.errorDialog_locNotDefined_title);
                setMessage(R.string.errorDialog_locNotDefined_message);
                positiveBtn.setVisibility(View.INVISIBLE);
                break;
            case GEO_PROBL:
                setImage(R.drawable.alert_dialog_geo_problem);
                setTitle(R.string.errorDialog_geoProblem_tittle);
                setMessage(R.string.errorDialog_geoProblem_message);
                setPositiveBtnText(R.string.errorDialog_open_settings_btn);
                break;
            case NO_INT:
                setImage(R.drawable.alert_dialog_no_internet);
                setTitle(R.string.errorDialog_noInternet_title);
                setMessage(R.string.errorDialog_noInternet_message);
                positiveBtn.setVisibility(View.INVISIBLE);
                break;
            case UNK_ERR:
                setImage(R.drawable.alert_dialog_undefined_error);
                setTitle(R.string.errorDialog_unknownError_title);
                setPositiveBtnText(R.string.errorDialog_send_bug_report_btn);
                break;
            case BAD_REQ:
                setImage(R.drawable.alert_dialog_bad_request);
                setTitle(R.string.errorDialog_invalidRequest_title);
                setMessage(R.string.errorDialog_invalidRequest_message);
                positiveBtn.setVisibility(View.INVISIBLE);
                break;
            case SERVER_ERR:
                setImage(R.drawable.alert_dialog_server_error);
                setTitle(R.string.errorDialog_serverError_title);
                setMessage(R.string.errorDialog_serverError_message);
                setPositiveBtnText(R.string.errorDialog_send_bug_report_btn);
                break;
            case TASK_CANCELED:
                setImage(R.drawable.alert_dialog_cancel_icon);
                setTitle(R.string.error_title);
                setMessage(R.string.error_dialog_canceled_message);
                positiveBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        if(mOnCancelListener != null)
            mOnCancelListener.onCancel(this);
    }

    @Override
    public void onClick(View v) {
        if(mOnClickListener == null)
            cancel();
        else
            mOnClickListener.onClick(ErrorDialog.this, v.getId());
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
