package com.doors.myplacetourist.common;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class Tools {
    private static final String TAG = "MyPlaceTag";

    public static Bitmap fileToRotatedBitmap(File file) throws IOException {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return rotateBitmap(
                BitmapFactory.decodeFile(
                        file.getAbsolutePath(),
                        options
                ),
                getOrientationDegrees(file)
        );
    }

    public static int getOrientation(File file) throws IOException {
        ExifInterface exif = new ExifInterface(file.getAbsolutePath());
        String orientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        if(orientation.equals(""))
            return 0;
        else
            return Integer.decode(orientation);
    }

    public static int getOrientationDegrees(File file) throws IOException {
        ExifInterface exif = new ExifInterface(file.getAbsolutePath());
        String orientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        if(orientation.equals(""))
            return orientationInDegrees(0);
        else
            return orientationInDegrees(Integer.decode(orientation));
    }

    public static int orientationInDegrees(int exifOrientation) {
        switch (exifOrientation) {
            case 3:
                return 180;
            case 6:
                return 90;
            case 8:
                return 270;
        }
        return 0;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees){
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static void log (String text){
        Log.v(TAG,text);
    }

    public static void log (String label, String text){
        Log.v(TAG,label + " : " + text);
    }

    public static void log (String localTAG, String label, String text){
        Log.v(TAG,localTAG + " | " + label + " : " + text);
    }
    public static void logE (String localTAG, String label, String text, Throwable t){
        Log.e(TAG,localTAG + " | " + label + " : " + text + '\n', t);
    }

    public static String getPath(Context context, Uri uri ) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver()
                .query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }

    public static byte[] inputStreamToBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static File imageToFile(Image image, File path, String name) throws IOException {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        File file = new File(path, name);

        OutputStream output = new BufferedOutputStream(new FileOutputStream(file));
        output.write(bytes);
        output.flush();
        output.close();

        return file;
    }
    public static void imageToFile(Image image, File targetFile) throws IOException {
        byte[] bytes = imageToByteArray(image);

        OutputStream output = new BufferedOutputStream(new FileOutputStream(targetFile));
        output.write(bytes);
        output.flush();
        output.close();
    }

    public static void byteArrayToFile(byte[] bytes, File targetFile) throws IOException {

        OutputStream output = new BufferedOutputStream(new FileOutputStream(targetFile));
        output.write(bytes);
        output.flush();
        output.close();
    }

    public static Bitmap byteArrayToBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }

    public static Bitmap imageToBitmap(Image image) {
        byte[] bytes = imageToByteArray(image);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }

    public static byte[] imageToByteArray(Image image) {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        return bytes;
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        //BitmapFactory.Options optionss = new BitmapFactory.Options();
        //optionss.inPreferredConfig = Bitmap.Config.RGB_565;


        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeFile(path,options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
// Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;}

}
