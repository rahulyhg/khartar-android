package com.vardhamaninfo.khartargaccha.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.JsonObject;
import com.vardhaman.vardhamanutilitylibrary.util.VUStringUtility;
import com.vardhamaninfo.khartargaccha.Application.Application;
import com.vardhamaninfo.khartargaccha.R;
import com.vardhamaninfo.khartargaccha.Retrofit.HttpServerBackend;
import com.vardhamaninfo.khartargaccha.Retrofit.RestAdapter;
import com.vardhamaninfo.khartargaccha.Util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button sign_in;
    TextView sign_up, forget_pass;
    EditText email, password;
    private SharedPreferences prefs;
    String lati, longi;
    LinearLayout lay, sign_up_lay;
    GoogleCloudMessaging gcm;
    String regid;
    String SENDER_ID = "133104379678";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sign_in = (Button) findViewById(R.id.sign_in);
        sign_up = (TextView) findViewById(R.id.signup);
        forget_pass = (TextView) findViewById(R.id.forget_pass);
        forget_pass.setOnClickListener(this);
        sign_in.setOnClickListener(this);
        sign_up.setOnClickListener(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        email = (EditText) findViewById(R.id.login_email);
        password = (EditText) findViewById(R.id.login_pass);
        lay = (LinearLayout) findViewById(R.id.login_lay);
        sign_up_lay = (LinearLayout) findViewById(R.id.lay_sign_up);
        sign_up_lay.setOnClickListener(this);

        checkLocation();
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(this);
            Log.d("token", regid);

            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i("Login Activity", "No valid Google Play Services APK found.");
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in:
                String musername = email.getText().toString();
                String mpassword = password.getText().toString();


                if (!VUStringUtility.stringNotEmpty(musername)) {
                    makeSnake(getResources().getString(R.string.fill_username));
                    return;
                } else if (!VUStringUtility.stringNotEmpty(mpassword)) {
                    makeSnake(getResources().getString(R.string.fill_password));
                    return;
                } else {
                    login(musername, mpassword, lati, longi);
                }
                break;
            case R.id.lay_sign_up:
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
                break;
            case R.id.forget_pass:
                Intent j = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(j);

                break;

        }
    }


    public void makeSnake(String msg) {

        Snackbar snackbar1 = Snackbar.make(lay, msg, Snackbar.LENGTH_LONG);
        snackbar1.getView().setBackgroundColor(Color.parseColor("#774A2B"));
        snackbar1.show();

    }


    public void checkLocation() {
        Log.d("checkloc", "insideloc");

        //gps testing start

        GpsTracker gps = new GpsTracker(LoginActivity.this);
        double latitude1 = gps.getLatitude();
        double longitude1 = gps.getLongitude();
        Log.d("checkloc", "" + latitude1 + "" + longitude1);
        Log.d("checkloc", "" + gps.canGetLocation);


        if (gps.canGetLocation()) {
            Log.d("checkloc", "insidegetloc");

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            lati = String.valueOf(latitude);
            longi = String.valueOf(longitude);
            Log.d("lati", lati);
            Log.d("longi", longi);
            String dl = String.valueOf(latitude);
            String dll = String.valueOf(longitude);
            Log.d("checkloc", dl + "" + dll);
            if (!dl.equals("0.0") && !dll.equals("0.0")) {
                Log.d("gps latitude work fine", String.valueOf(latitude));

            } else {

//                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                changeGPS();
                Log.d("gps latitude error", String.valueOf(latitude));
                Log.d("gps longitude error", String.valueOf(longitude));

            }


        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
//            gps.showSettingsAlert();
            changeGPS();
        }


        //gps testing end
    }

    public void changeGPS() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        // Setting Dialog Title
        alertDialog.setTitle("Enable Location");

        alertDialog.setMessage("Unable to get your location. Please enable your location ");

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Enable GPS
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();

    }

    public void login(String susername, String spassword, String latitude, String logitude) {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        Call<JsonObject> call = Application.getAppInstance().getServerBackend(RestAdapter.AuthType.UNAUTHARIZED)
                .userLogin(susername, spassword, latitude, logitude, regid);

        new HttpServerBackend(Application.getAppInstance()).getData(call, new HttpServerBackend.ResponseListener() {
            @Override
            public void onReturn(boolean success, Response<JsonObject> data) {

                pDialog.dismiss();
                if (success) {

                    Log.e(" Login Response ", data.body().toString() + " response");

//        new VUBaseAsyncTask(LoginActivity.this, null, true, Constant.HOST + Constant.LOGIN, VUBaseAsyncTask.METHOD_POST,
//                new String[]{"email_address", "password", "longitude", "latitude","device_id"},
//                new String[]{susername,spassword,latitude,logitude,regid}, new VUIBaseAsyncTask() {
//            @Override
//            public void onTaskCompleted(int success, JSONObject result) {
//                if (success==1){

                    try {

                        JSONObject result = new JSONObject(data.body().toString());

                        JSONObject jsonObject = result.getJSONObject("user_array");
                        String user_id = jsonObject.getString("user_id");
                        prefs.edit().putString(Constant.NEW_ID, user_id).apply();
                        Log.d("user_id", user_id);
                        String uname = jsonObject.getString("username");
                        prefs.edit().putString(Constant.NEW_NAME, uname).apply();
                        Log.d("user_name", uname);
                        String email_address = jsonObject.getString("email_address");
                        // String mobilenumber=jsonObject.getString("email_address");
                        prefs.edit().putString(Constant.NEW_EMAIL, email_address).apply();
                        // prefs.edit().putString(Constant.NEW_NUMBER, mobilenumber).apply();
                        Log.d("email", email_address);


                        prefs.edit().putString(Constant.IS_LOGIN, "1").apply();


/*
                        prefs.edit().putString("user_id", user_id);
                        Constant.NEW_ID=user_id;
                        Log.d("login",""+Constant.NEW_ID);
                        prefs.edit().putString(Constant.NEW_NAME,uname);
                        prefs.edit().putString(Constant.NEW_EMAIL,email_address);
                        prefs.edit().putString(Constant.IS_LOGIN,"1");*/





                       /* String uid=jsonObject.getString("user_id");
                        Log.d("loginuid",uid);
                        String usernam=jsonObject.getString("username");
                        String emailaddre=jsonObject.getString("email_address");
                        prefs.edit().putString(Constant.KEY_IS_LOGGED_IN,"1").apply();
                        prefs.edit().putString(Constant.USERID, uid).apply();
                        Log.d("loginuidpre", "" + prefs.getString(Constant.USERID, ""));
                        prefs.edit().putString(Constant.USERNAME,usernam).apply();
                        prefs.edit().putString(Constant.EMAILADDRESS,emailaddre).apply();
                        prefs.edit().putString(Constant.KEY_ID,uid).apply();*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Intent in = new Intent(LoginActivity.this, Navigation_Drawer_Activity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                    //Toast.makeText(getBaseContext(), "successfully", Toast.LENGTH_LONG).show();
                } else {

                    if (data != null) {

                        try {
                            String msg = (new JSONObject(data.body().toString())).getString("msg");
                            makeSnake(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.try_Again), Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });//.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);


    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("tag", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void registerInBackground() {
        new AsyncTask() {

            ProgressDialog pDialog;

            @Override
            protected void onPreExecute() {

                super.onPreExecute();
                pDialog = new ProgressDialog(LoginActivity.this);

                pDialog.setMessage("Loading. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();

            }

            @Override
            protected Object doInBackground(Object... params) {
                // TODO Auto-generated method stub

                String msg = "";
                try {
                    if (gcm == null) {

                        gcm = GoogleCloudMessaging.getInstance(LoginActivity.this);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    Log.i("regis_id", msg);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;

            }

            protected void onPostExecute(Object result) {
                super.onPostExecute(result);
                try {
                    if (pDialog != null && pDialog.isShowing())
                        pDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ;


        }.execute(null, null, null);
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i("GCMgetId", "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
                Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i("get", "App version changed.");
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences,
        // but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(getClass().getSimpleName(),
                Context.MODE_PRIVATE);
    }

    @Override
    public void onBackPressed() {
        VUStringUtility.exitApp(LoginActivity.this);
    }
}
