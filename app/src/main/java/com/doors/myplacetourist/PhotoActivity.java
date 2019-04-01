package com.doors.myplacetourist;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.LocaleList;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.doors.myplacetourist.common.ErrorDialog;
import com.doors.myplacetourist.common.Tools;
import com.doors.myplacetourist.common.UI;
import com.doors.myplacetourist.common.Values;
import com.doors.myplacetourist.managers.LocationManager;
import com.doors.myplacetourist.managers.PermissionManager;
import com.doors.myplacetourist.rest.ProgressRequestBody;
import com.doors.myplacetourist.rest.RestClient;
import com.doors.myplacetourist.rest.pojo.Sticker;
import com.google.android.gms.location.LocationListener;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "PhotoActivity";
    private final int WEB_BROWSER_REQUEST = 0, SETTINGS_REQUEST = 1;
    private CameraView mCamera;
    private UI ui;
    private LocationManager mLocationManager;
    private PermissionManager permissionManager;
    private RestClient mRestClient;
    private Handler mMessageHandler;
    private Location mLocation;
    private Handler mActivityHandler;
    private SharedPreferences mServerUrlPref;
    private UI.Task currentTask;
    private String mLink = "";
    private File mFile;


    //**********************************************************************************************

    private DialogInterface.OnClickListener oCL = (dialog, which) -> dialog.cancel();

    private DialogInterface.OnCancelListener oCanL = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            currentTask = UI.Task.done;
            ui.changeUi(UI.Task.done);
        }
    };

    private Callback<Sticker> mResponseCallback = new Callback<Sticker>() {
        @Override
        public void onResponse(Call<Sticker> call, Response<Sticker> response) {
            Tools.log(TAG, "mResponseCallback, onResponse", "Response taken");
            String responseMessage = response.message();

            if (response.isSuccessful()) {
                int code = response.body().getStatus().getCode();
                responseMessage = response.body().getStatus().getMessage();
                Tools.log(TAG, "mResponseCallback, onResponse", "server code = "+ code +" message: " + responseMessage);

                if(code < 1){
                    if(response.body().getSticker() != null){
                        Tools.log(TAG, "mResponseCallback, onResponse", "objects_info != null");
                        String link = getLink(response.body().getObjects_info());
                        if(!link.isEmpty()){
                            mLink = link;
                        }
                        ui.setSticker(response.body());
                        showTriangle();
                        return;
                    }
                    showErrorDialog(ErrorDialog.NO_OBJ);
                } else {
                    Tools.log(TAG, "mResponseCallback, onResponse", "before dialog show");
                    showErrorDialog(ErrorDialog.UNDEF_LOC, (d,v)->{
                        oCL.onClick(d,v);
                        if (v == ErrorDialog.POSITIVE_BTN)
                            takePicture();
                    });
                    Tools.log(TAG, "mResponseCallback, onResponse", "after dialog show");
                }
            } else if(response.code()>=500) {
                showErrorDialog(ErrorDialog.SERVER_ERR);
            } else if (response.code() == 400) {
                showErrorDialog(ErrorDialog.BAD_REQ);
            } else {
                showErrorDialog(responseMessage);
            }
        }

        @Override
        public void onFailure(Call<Sticker> call, Throwable t) {
            Tools.log(TAG, "mResponseCallback, onFailure", "Response didn't taken: \n" + t.toString());
            currentTask = UI.Task.done;
            ui.changeUi(UI.Task.done);
            if(t.getMessage().equals("Socket closed") ) return;
            if(t.getMessage().equals("Canceled") ) return;
                Tools.log(TAG, "mResponseCallback, onFailure",
                        "message: " + t.getMessage() +
                                "loc message: " + t.getLocalizedMessage());
                showErrorDialog(ErrorDialog.NO_INT, (d, v) -> {
                    oCL.onClick(d, v);
                    if (v == ErrorDialog.POSITIVE_BTN)
                        takePicture();
                });

        }
    };

    private CameraListener mCameraListener = new CameraListener() {
        @Override
        public void onPictureTaken(@NonNull PictureResult result) {
            /*result.toBitmap(bitmap -> {
                ui.getPhotoPreview().setImageBitmap(Tools.rotateBitmap(bitmap,Tools.orientationInDegrees(result.getRotation())));
                ui.getPhotoPreview().setVisibility(View.VISIBLE);
                ui.getPhotoPreview().invalidate();
            });*/
            result.toFile(mFile, imageFile -> {
                Tools.log(TAG, "cameraCallback", "saved in file: " + imageFile.getAbsolutePath());
                Tools.log(TAG, "cameraCallback", "current task: " + currentTask.toString());
                Bitmap b = null;
                try {
                    b = Tools.fileToRotatedBitmap(imageFile);
                    ui.getPhotoPreview().setImageBitmap(b);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(mLocationManager.getmLocation()!=null) mLocation = mLocationManager.getmLocation();
                if(mLocation==null){
                    showErrorDialog(ErrorDialog.GEO_PROBL, (d, id) -> {
                        if(id == ErrorDialog.POSITIVE_BTN)
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        oCL.onClick(d,id);
                    });
                    currentTask = UI.Task.failed;
                    mActivityHandler.post(()-> ui.changeUi(UI.Task.failed));
                    return;
                }
                if (setupExif(imageFile)){
                    Tools.log(TAG, "cameraCallback", "Start getting link");
                    mRestClient.getLink(imageFile, mResponseCallback);

                    if (false)
                        mRestClient.getLinkTest(imageFile, new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Tools.log(TAG,"getLinkTest", "onResponse");
                                if(response.body()!=null) {
                                    try {
                                        File file = new File(Environment.getExternalStorageDirectory(),"response.txt");
                                        FileUtils.writeByteArrayToFile(file,response.body().bytes());
                                        Toast.makeText(PhotoActivity.this,"response write to: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                                        Tools.log(TAG,"getLinkTest", "Response: " + response.body().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }else if(response.errorBody()!=null) {
                                    try {
                                        Tools.log(TAG,"getLinkTest", "ErrorResponse: " + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Tools.log(TAG,"getLinkTest", "onFailure: " + t.toString());
                            }
                        });
                } else {
                    showErrorDialog(ErrorDialog.UNK_ERR);
                    currentTask = UI.Task.done;
                    mActivityHandler.post(()-> ui.changeUi(UI.Task.done));
                }
            });
        }
    };

    private ProgressRequestBody.UploadCallbacks mProgressCallback =
            new ProgressRequestBody.UploadCallbacks() {
        private boolean finished = false;
        @Override
        public void onProgressUpdate(int percentage) {
            mActivityHandler.post(()->{
                ui.getProgressBar().setProgress(percentage);
                ui.getProgressText().setText(String.valueOf(percentage)+"%");
                if(percentage==0) {
                    finished = false;
                }
            });
            if(percentage == 100 && !finished){
                mActivityHandler.postDelayed(()->{
                    if (ui.getProgressBar().getVisibility() == View.VISIBLE){
                        ui.getProgressBar().setVisibility(View.INVISIBLE);
                        ui.getProgressText().setText("");
                    }
                },3 * 1000);
                finished = true;
            }
        }

        @Override
        public void onProgressUpdate(int percentage, int currentCount, int totalCount) {

        }

        @Override
        public void onError() {

        }

        @Override
        public void onFinish() {

        }
    };

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                mLocation = location;
            }
        }
    };

    //**********************************************************************************************
    private void initUI() {
        setContentView(R.layout.activity_photo);
        ui = new UI(UI.Mode.manual,this,this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        mActivityHandler = new Handler();
        mMessageHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Activity activity = PhotoActivity.this;
                Toast.makeText(activity, (String) msg.obj, Toast.LENGTH_SHORT).show();
            }
        };
        mFile = new File(getCacheDir(),getString(R.string.filename));
        permissionManager = new PermissionManager(this);
        mLocationManager = new LocationManager(mLocationListener,this);
        if (!permissionManager.hasAllPermissionsGranted()) {
            permissionManager.requestAppPermissions();
        } else {
            startCamera();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationManager.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String language = LocaleList.getDefault().get(0).getLanguage();
        Tools.log(TAG, "onResume", "language: " + language);
        if(language.equals("ru"))
            ui.getTripLogo().setBackgroundResource(R.drawable.marker_1_ru);
        mRestClient = new RestClient(getCurrentUrl(), mProgressCallback);

        if (!mLocationManager.checkPlayServices()) {
            Toast.makeText(this,R.string.missing_gplay_services,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationManager.pause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PermissionManager.REQUEST_CAMERA_PERMISSIONS) {
            if(permissionManager.handleResult(requestCode, grantResults)){
                startCamera();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onClick(View v) {
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.btn_alpha);
        switch (v.getId()) {
            case R.id.phoLay_btnTakePictures:
                mActivityHandler.post(()->v.startAnimation(animAlpha));
                takePicture();
                break;
            case R.id.phoLay_btnMarkerSettings:
                openMarkerSettings(v,animAlpha);
                break;
            case R.id.phoLay_btnSettings:
                openSettings(v,animAlpha);
                break;
            case R.id.phoLay_marker:
                openHttpPage(mLink);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            if (requestCode == WEB_BROWSER_REQUEST) {
                Tools.log(TAG,"onActivityResult","requestCode == WEB_BROWSER_REQUEST");

            } else if (requestCode == SETTINGS_REQUEST) {
                Tools.log(TAG,"onActivityResult","requestCode == SETTINGS_REQUEST");
                ui.updateMarkerViaSettings(isTripLink());
            }
        }
    }

    //**********************************************************************************************
    private void startCamera(){
        mCamera = findViewById(R.id.phoLay_camera);
        mCamera.setLifecycleOwner(this);
        mCamera.addCameraListener(mCameraListener);
    }

    private void takePicture() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionManager.requestAppPermissions();
            return;
        }

        if (currentTask != UI.Task.capture2upload) {
            currentTask = UI.Task.capture2upload;
            mLocationManager.updateLocation();
            mCamera.takePicture();
            ui.changeUi(UI.Task.capture2upload);
        } else {
            mRestClient.cancel();
        }
    }

    private void openMarkerSettings(View view, Animation animation) {
        mActivityHandler.post(()->{
            view.startAnimation(animation);
            if (currentTask == UI.Task.capture2upload) return;
            Intent intent = new Intent(PhotoActivity.this, MarkerSettingsActivity.class);
            startActivityForResult(intent,SETTINGS_REQUEST);
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
        });
    }

    private void openSettings(View view, Animation animation) {
        mActivityHandler.post(()->{
            view.startAnimation(animation);
            if (currentTask == UI.Task.capture2upload) return;
            startActivity(new Intent(PhotoActivity.this, SettingsActivity.class));
            //overridePendingTransition(R.anim.slide_right_out, R.anim.slide_right_in);
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
        });

    }

    private boolean setupExif(File file) {
        if (mLocation != null) {
            try {
            ExifInterface exifInterface = new ExifInterface(file.getAbsolutePath());
            Tools.log(TAG, "setupExif", "mLocation: lat - " + mLocation.getLatitude() + " long - " + mLocation.getLongitude());
            double latitude = Math.abs(mLocation.getLatitude());
            double longitude = Math.abs(mLocation.getLongitude());

            int num1Lat = (int) Math.floor(latitude);
            int num2Lat = (int) Math.floor((latitude - num1Lat) * 60);
            double num3Lat = (latitude - ((double) num1Lat + ((double) num2Lat / 60))) * 3600000;

            int num1Lon = (int) Math.floor(longitude);
            int num2Lon = (int) Math.floor((longitude - num1Lon) * 60);
            double num3Lon = (longitude - ((double) num1Lon + ((double) num2Lon / 60))) * 3600000;

            String lat = num1Lat + "/1," + num2Lat + "/1," + num3Lat + "/1000";
            String lon = num1Lon + "/1," + num2Lon + "/1," + num3Lon + "/1000";

            if (mLocation.getLatitude() > 0) {
                exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "N");
            } else {
                exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "S");
            }

            if (mLocation.getLatitude() > 0) {
                exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "E");
            } else {
                exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "W");
            }

            exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE, lat);
            exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, lon);

            exifInterface.saveAttributes();
            return true;
            } catch (IOException e) {
                e.printStackTrace();
                Tools.log(TAG, "setupExif", "setupExif done: " + e.toString());
                return false;
            }
        } else
            Tools.log(TAG, "setupExif", "mLocation is null");
        return false;
    }

    private void openHttpPage(String link) {
        if(!ui.isTripChecked()) return;
        Intent intent = new Intent(PhotoActivity.this, WebBrowserActivity.class);
        intent.putExtra(Values.TRIP_URL, link);
        startActivityForResult(intent,WEB_BROWSER_REQUEST);
    }

    private String getCurrentUrl() {
        mServerUrlPref = getSharedPreferences("server_pref",MODE_PRIVATE);
        String url = mServerUrlPref.getString("server_url","http://46.32.68.119:3457/");

        return url;
    }

    private String getLink(List<Sticker.ObjectsInfoBean> objectsInfo) {
        String[] listOfLinks = objectsInfo.stream()
                .map(objInfo -> objInfo.getSticker().getPath())
                .toArray(String[]::new);
        if (listOfLinks.length > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Tools.log(TAG, "getLink", "tripUrls: " + String.join(" || ", listOfLinks));
            }
            for (String link : listOfLinks) {
                if (!link.isEmpty()) {
                    return link;
                }
            }
        } else Tools.log(TAG, "getLink", "listOfLinks.length = 0");
        return "";
    }

    private void showErrorDialog(int errorCode) {
        ErrorDialog.makeDialog(this, errorCode, oCL, oCanL).show();
    }

    private void showErrorDialog(int errorCode, DialogInterface.OnClickListener onClickListener) {
        ErrorDialog.makeDialog(this, errorCode, onClickListener, oCanL).show();
    }

    private void showErrorDialog(String message) {
        ErrorDialog errorDialog = new ErrorDialog(PhotoActivity.this);
        errorDialog.setTitle(R.string.error_title);
        errorDialog.setMessage(message);
        errorDialog.show();
    }

    private void showTriangle(){
        ui.updateMarkerViaSettings(isTripLink());
        currentTask = UI.Task.success;
        ui.changeUi(UI.Task.success);
        ui.getTakePictureBtn().setOnClickListener(v->{
            currentTask = UI.Task.checked;
            ui.changeUi(UI.Task.checked);
            mLink = "";
        });
    }

    private boolean isTripLink() {
        return !mLink.isEmpty();
    }
}
