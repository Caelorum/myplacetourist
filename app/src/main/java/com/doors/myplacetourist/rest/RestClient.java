package com.doors.myplacetourist.rest;

import android.net.Uri;
import android.widget.Toast;

import com.doors.myplacetourist.common.Tools;
import com.doors.myplacetourist.rest.pojo.Sticker;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    private final String TAG = "RestClient";
    private DoorsClient doorsClient;
    private Retrofit retrofit;//TODO: check object to safe delete (make local variable)
    private ProgressRequestBody.UploadCallbacks mProgressListener;
    private Call<Sticker> call;
    private Call<ResponseBody> callTest;
    private ExecutorService executor = Executors.newCachedThreadPool();
    private Future<?> result;


    //TODO make static
    public RestClient(String baseUrl) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .encodedAuthority(baseUrl)
                .appendEncodedPath("");
        baseUrl = builder.build().toString();
        Tools.log(TAG, "RestClient, constructor", "url: " + baseUrl);

        try{
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        } catch (IllegalArgumentException e) {
            Tools.log(TAG,"RestClient, constructor", e.toString());
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://46.32.68.119:3457/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        finally {
            doorsClient = retrofit.create(DoorsClient.class);
        }
    }

    public RestClient(String baseUrl, ProgressRequestBody.UploadCallbacks progressListener) {
        this(baseUrl);
        mProgressListener = progressListener;
    }

    public void getLink(File file, Callback<Sticker> responseCallback) {
        Tools.log(TAG, "getLink", "Start getting link");
        call = doorsClient.getStickers("none", prepareMultipart(file,"file"));
        call.enqueue(responseCallback);
    }

    public void getLinkTest(File file, Callback<ResponseBody> responseCallback) {
        Tools.log(TAG, "getLinkTest", "Start getting link");
        callTest = doorsClient.getStickersTest("none", prepareMultipart(file,"file"));
        callTest.enqueue(responseCallback);
    }

    private MultipartBody.Part prepareMultipart(File file, String partKey){
        //Tools.log(TAG, "prepareMultipart", "filename: " + file.getName() );
        //Tools.log(TAG, "prepareMultipart", "key: " + partKey );
        ProgressRequestBody fileBody = new ProgressRequestBody(
                file,
                "multipart/form-data",
                mProgressListener
        );
        return MultipartBody.Part.createFormData(partKey, file.getName(),fileBody);
    }

    public void cancel() {

        if(result!=null && result.isDone()){
            result = null;
        }
        if (call != null){
            call.cancel();
        } else {
            result = executor.submit(() -> {
                while(call==null){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(call!=null) call.cancel();
                }
            });
        }
    }
}
