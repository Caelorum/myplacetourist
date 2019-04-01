package com.doors.myplacetourist.rest;

import com.doors.myplacetourist.common.Tools;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class RestResponse {
    private final String TAG = "RestResponse";

    private int methodCode;

    private int stateCode;
    private int httpCode;
    private String message;

    public RestResponse(int methodCode){
        this.methodCode = methodCode;
    }

    public RestResponse(int methodCode, int stateCode, int httpCode, String message) {
        this(methodCode);
        this.stateCode = stateCode;
        this.httpCode = httpCode;
        this.message = message;
    }

    public void parseResponse(Response<ResponseBody> response) {

        httpCode = response.code();
        message = response.body().toString();
    }
    //TODO to write over
    public static RestResponse parseResponse(Response<ResponseBody> response, int methodID){
        return new RestResponse(0,0,0,"");
    }

    public void parseErrorResponse(Response<?> response) throws IOException {
        httpCode = response.code();
        if(response.errorBody()!=null) {
            if (response.errorBody().string().isEmpty()){
                message = response.message().toLowerCase();
            }
            else {
                message = response.errorBody().string();
                Tools.log(TAG,"parseErrorResponse", "errorBody: " + message);
            }
        } else {
            message = response.message().toLowerCase();
        }
}

    @Override
    public String toString(){
        return "methodCode: " + methodCode +
                " stateCode: " + stateCode +
                " httpCode: " + httpCode +
                " message: " + message;
    }


    public void setMessage(String message){
        this.message = message;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public int getMethodCode() {
        return methodCode;
    }

    public int getStateCode() {
        return stateCode;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public String getMessage() {
        return message;
    }
}
