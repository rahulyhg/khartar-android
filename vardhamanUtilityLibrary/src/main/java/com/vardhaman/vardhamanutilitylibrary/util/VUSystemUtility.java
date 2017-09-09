package com.vardhaman.vardhamanutilitylibrary.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Android on 4/1/2015.
 */
public class VUSystemUtility {

    private static VUSystemUtility vuSystemUtility;
    private Context context;


    public static VUSystemUtility getInstance(Context context){
        if(vuSystemUtility == null)
            vuSystemUtility = new VUSystemUtility(context);

        return vuSystemUtility;
    }

    VUSystemUtility(Context context){
        this.context = context;
    }

    public boolean appInstalledOrNot(String packageName) {
        if(context == null)
            throw new NullPointerException("Context must be provided");
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public Intent generateShareIntent(String text){
        return new Intent().setAction(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, VUStringUtility.validateString(text) ? text : "null").setType("text/plain");
    }

    public Intent generateShareIntent(Uri imageUri){
        return new Intent().setAction(Intent.ACTION_SEND).putExtra(Intent.EXTRA_STREAM, imageUri).setType("image/*");
    }

    public Intent generateShareIntent(ArrayList<Uri> imageUris){
        return new Intent().setAction(Intent.ACTION_SEND_MULTIPLE).putExtra(Intent.EXTRA_STREAM,imageUris).setType("image/*");
    }

    public Intent generateNavigationIntent(String sLatitude,String sLongitude,String dLatitude,String dLongitude,String label){
        String format = "geo:" + sLatitude + "," + sLongitude + "?q=" + dLatitude + "," + dLongitude + (VUStringUtility.validateString(label) ? "(" + label + ")" : "");
        Uri uri = Uri.parse(format);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    public Intent generateNavigationIntent(String query,String label){
        String format = "google.navigation:q="+query;
        Uri uri = Uri.parse(format);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    public boolean validateFilePath(String path){
        return VUStringUtility.validateString(path) && validateFile(new File(path));
    }

    public boolean validateDirectoryPath(String path){
        return validateDirectory(VUStringUtility.validateString(path) ? new File(path) : null);
    }

    public boolean validateFile(File file){
        return file != null && file.exists();
    }

    public boolean validateDirectory(File file){
        return file != null && validateFile(file) && file.isDirectory();
    }

    public String getMimeType(String path)
    {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;

        try {
            if (contentUri.getScheme().toString().compareTo("content")==0) {
                String[] proj = {MediaStore.Images.Media.DATA};
                cursor = context.getContentResolver().query(contentUri, proj, null,
                        null, null);
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }else if (contentUri.getScheme().toString().compareTo("file") == 0){
                File file = new File(contentUri.getPath());
                if(vuSystemUtility.validateFile(file)){
                    return file.getAbsolutePath();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }


    public String getDevice(){
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }



    public static Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    public String generateFBHashKey(){
        // Add code to print out the key hash
        try {
            PackageInfo info =context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String key = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("FB KeyHash: ", key);
                return key;
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        return null;
    }

    public String getMediaDirectory(){
        File dir = new File(Environment.getExternalStorageDirectory() + context.getApplicationInfo().name);
        if (dir!=null && !dir.exists()){
            dir.mkdirs();
        }
        if (dir.exists() && dir.isDirectory()){
            return dir.getAbsolutePath();
        }
        return null;
    }

    /*
    * generate file chooser dialog, default pathToFolder in root of sdcard
    * fileExtension: pass null to select any example: image/*|application/pdf|audio/*|video/*|text/**/
    public Intent generateFileChooserDialog(boolean multiple, String pathToFolder,String fileExtensions){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        if (validateDirectoryPath(pathToFolder)){
            File file = new File(pathToFolder);
            intent.setDataAndType(Uri.fromFile(vuSystemUtility.validateFile(file) ? file : new File("/sdcard")) ,VUStringUtility.validateString(fileExtensions) ? fileExtensions : "*/*");
        }else{
            intent.setType(VUStringUtility.validateString(fileExtensions) ? fileExtensions : "*/*");
        }
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, multiple);
        }
        return Intent.createChooser(intent, "Select File");
    }

    public String generateBase64(String filePath){
        if(validateFilePath(filePath)) {
            InputStream inputStream = null;//You can get an inputStream using any IO API
            try {
                inputStream = new FileInputStream(filePath);
                byte[] bytes;
                byte[] buffer = new byte[8192];
                int bytesRead;
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                try {
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bytes = output.toByteArray();
                return Base64.encodeToString(bytes, Base64.DEFAULT);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }



}
