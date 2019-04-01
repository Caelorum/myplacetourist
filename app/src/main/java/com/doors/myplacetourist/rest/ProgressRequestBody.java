package com.doors.myplacetourist.rest;

import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class ProgressRequestBody extends RequestBody {

    private File mFile;
    private String mPath;
    private UploadCallbacks mListener;
    private String content_type;
    private int mCurrentFileCount = 1;
    private int mTotalSerieCount = 1;


    private static final int DEFAULT_BUFFER_SIZE = 2048;

    public interface UploadCallbacks {
        void onProgressUpdate(int percentage);
        void onProgressUpdate(int percentage, int currentCount, int totalCount);
        void onError();
        void onFinish();
    }

    public ProgressRequestBody(final File file,
                               String content_type,
                               final  UploadCallbacks listener) {
        this.content_type = content_type;
        mFile = file;
        mListener = listener;
    }

    public ProgressRequestBody(final File file,
                               String content_type,
                               final  UploadCallbacks listener,
                               int currentFileCount,
                               int totalSerieCount) {
        this(file,content_type,listener);
        mCurrentFileCount = ++currentFileCount;
        mTotalSerieCount = totalSerieCount;
    }



    @Override
    public MediaType contentType() {
        return MediaType.parse(content_type+"/*");
    }

    @Override
    public long contentLength() {
        return mFile.length();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long fileLength = mFile.length();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        FileInputStream in = new FileInputStream(mFile);
        long uploaded = 0;

        try {
            int read;
            Handler handler = new Handler(Looper.getMainLooper());
            while ((read = in.read(buffer)) != -1) {

                // update progress on UI thread
                handler.post(new ProgressUpdater(uploaded, fileLength));

                uploaded += read;
                sink.write(buffer, 0, read);
            }
            handler.post(()->mListener.onProgressUpdate(100));
        } finally {
            in.close();
        }
    }

    private class ProgressUpdater implements Runnable {
        private long mUploaded;
        private long mTotal;
        public ProgressUpdater(long uploaded, long total) {
            mUploaded = uploaded;
            mTotal = total;
        }

        @Override
        public void run() {
            long percent = (100 / mTotalSerieCount) * mUploaded / mTotal;
            if(mTotalSerieCount > 1){
                if(mCurrentFileCount>=2){
                    percent += 100 / mTotalSerieCount * (mCurrentFileCount - 1);
                }
                mListener.onProgressUpdate((int)percent,mCurrentFileCount,mTotalSerieCount);
            } else {
                mListener.onProgressUpdate((int)percent);
            }
            /*Tools.log("ProgressUpdater","run",
                    (int)percent + "%"
                    +" count: " + mCurrentFileCount
                    + " total: " + mTotalSerieCount);*/
        }
    }
}
