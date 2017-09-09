package com.vardhamaninfo.khartargaccha.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.vardhamaninfo.khartargaccha.R;

import java.util.regex.Pattern;

public class StringUtility {


    public static boolean stringNotNull(String str){
        return str != null && !str.equals("null");
    }

    public static boolean stringNotEmpty(String str){
        return !str.isEmpty();
    }


    /*
    * Validate input string for null object and not empty string.
    * */
    public static boolean validateString(String str){
        return stringNotNull(str) && stringNotEmpty(str);
    }

    public static boolean validateEditText(EditText editText){
        return stringNotEmpty(editText.getText().toString().trim());
    }

    public static boolean validateText(TextView textView){
        return stringNotEmpty(textView.getText().toString().trim());
    }

    public static boolean validateEmail(String email){
        final Pattern emailPattern = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+(aero|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel|ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|ax|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cu|cv|cx|cy|cz|de|dj|dk|dm|do|dz|ec|ee|eg|er|es|et|eu|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|im|in|io|iq|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|mk|ml|mm|mn|mo|mp|mq|mr|ms|mt|mu|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|re|ro|rs|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|sk|sl|sm|sn|so|sr|st|su|sv|sy|sz|tc|td|tf|tg|th|tj|tk|tl|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|uz|va|vc|ve|vg|vi|vn|vu|wf|ws|ye|yt|yu|za|zm|zw)\\b",2);
        return emailPattern.matcher(email).matches();
    }

    public static void exitApp(final Activity activity) {
        new AlertDialog.Builder(activity).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure you want to close App?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                }).setNegativeButton("No", null).show();
    }




    public static void hideKeyboard(Activity activity){
        try {
            InputMethodManager imm = (InputMethodManager) activity.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }catch(Exception e){
            e.printStackTrace();
        }
    }



    // Method responsible for replacing fragments
    public void replaceFragment (Fragment fragment, FragmentManager manager){
        String backStateName =  fragment.getClass().getName();
        String fragmentTag = backStateName;
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);
        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null){
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.container, fragment, fragmentTag);
          //  ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    /*private void showError(final VolleyError volleyError, final ResponseHandler responseHandler){
        NetworkResponse networkResponse = volleyError.networkResponse;


        if (volleyError instanceof NoConnectionError ||volleyError instanceof TimeoutError) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "No Internet Connection, Please check your internet connection to use this application", Toast.LENGTH_SHORT).show();
                    responseHandler.Error(volleyError);
                }
            });
        }
        else  if (volleyError instanceof ServerError){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(getApplicationContext(), volleyError.getLocalizedMessage() + volleyError.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
                    Log.e("ServerError",volleyError.getLocalizedMessage() + volleyError.networkResponse.statusCode);
                    responseHandler.Error(volleyError);
                }
            });
        }
        else if (volleyError instanceof AuthFailureError) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    // Toast.makeText(getApplicationContext(), "AuthFailureError", Toast.LENGTH_SHORT).show();
                    Log.e("AuthFailureError","AuthFailureError");
                    responseHandler.Error(volleyError);
                }
            });
        }
        else if (volleyError instanceof NetworkError) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(getApplicationContext(), "NetworkError", Toast.LENGTH_SHORT).show();
                    Log.e("NetworkError","NetworkError");
                    responseHandler.Error(volleyError);
                }
            });
        }
        else if (volleyError instanceof ParseError) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    //  Toast.makeText(getApplicationContext(), "ParseError", Toast.LENGTH_SHORT).show();
                    Log.e("ParseError","ParseError");
                    responseHandler.Error(volleyError);
                }
            });
        }
        else {
            responseHandler.Error(volleyError);
        }
    }*/


  /*  public static boolean isStoragePermissionGranted(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission","Permission is granted");
                return true;
            } else {

                Log.e("Permission","Permission is revoked");
                activity.requestPermissions( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.e("Permission","Permission is granted");
            return true;
        }
    }*/

}
