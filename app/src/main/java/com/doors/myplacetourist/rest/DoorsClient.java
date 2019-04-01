package com.doors.myplacetourist.rest;

import com.doors.myplacetourist.rest.pojo.Sticker;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface DoorsClient {

    @Multipart
    @POST("stickers/api/get_stickers")
    Call<Sticker> getStickers(
            @Query("scene_format") String scene_format,
            @Part MultipartBody.Part file
    );

    @Multipart
    @POST("stickers/api/get_stickers")
    Call<ResponseBody> getStickersTest(
            @Query("scene_format") String scene_format,
            @Part MultipartBody.Part file
    );
}
