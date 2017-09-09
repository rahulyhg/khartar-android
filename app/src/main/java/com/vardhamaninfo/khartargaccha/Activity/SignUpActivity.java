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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    Spinner sp;
    SharedPreferences sharedPreferences;
    TextView signup;
    EditText fname, lname, eaddress, mnumber, password, conf_pass, mob_num;
    String lati, longi;
    LinearLayout lay;
    RadioGroup rg;
    RadioButton sadhu, sadhvi, n_user;
    GoogleCloudMessaging gcm;
    String regid;
    String SENDER_ID = "133104379678";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up);
        //sp=(Spinner)findViewById(R.id.user_type);
        lay = (LinearLayout) findViewById(R.id.lay_regis);

        List<String> list = new ArrayList<String>();
        list.add("sadhu");
        list.add("sadhvi");
        checkLocation();

/*
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        sp.setAdapter(dataAdapter);
*/
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SignUpActivity.this);
        fname = (EditText) findViewById(R.id.first_name);
        //lname=(EditText)findViewById(R.id.last_name);
        eaddress = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        conf_pass = (EditText) findViewById(R.id.conf_password);
        mob_num = (EditText) findViewById(R.id.mobile_number);
        signup = (TextView) findViewById(R.id.sign_register);
        rg = (RadioGroup) findViewById(R.id.u_type);
        sadhu = (RadioButton) findViewById(R.id.l_sadhu);
        sadhvi = (RadioButton) findViewById(R.id.l_sadhvi);
        n_user = (RadioButton) findViewById(R.id.l_user);
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

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String user_typpe = ((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString();
                String firstname = fname.getText().toString();
                //String lastname=lname.getText().toString();
                String emailaddrss = eaddress.getText().toString();
                String pwd = password.getText().toString();
                String m_n = mob_num.getText().toString();
                //String user_typpe= String.valueOf(sp.getSelectedItem());
                String user_typpe = "User";
                Log.d("userType", user_typpe);
                String conf_pwd = conf_pass.getText().toString();


                if (!VUStringUtility.stringNotEmpty(firstname)) {
                    makeSnake(getResources().getString(R.string.fill_username));
                    return;
                } else if (!VUStringUtility.stringNotEmpty(emailaddrss)) {
                    makeSnake(getResources().getString(R.string.fill_email_id));
                    return;
                } else if (!VUStringUtility.validateEmail(emailaddrss)) {
                    makeSnake(getResources().getString(R.string.invalid_email));
                    return;

                } else if (!VUStringUtility.stringNotEmpty(m_n)) {
                    makeSnake(getResources().getString(R.string.fill_mobile));
                    return;


                } else if (!VUStringUtility.validateMobileNumber(m_n)) {
                    makeSnake(getResources().getString(R.string.fill_mobile));
                    return;


                } else if (!VUStringUtility.stringNotEmpty(pwd)) {
                    makeSnake(getResources().getString(R.string.fill_password));
                    return;
                } else if (!VUStringUtility.stringNotEmpty(conf_pwd)) {
                    makeSnake(getResources().getString(R.string.fill_confirm_password));
                    return;
                } else if (!VUStringUtility.stringNotEmpty(user_typpe)) {
                    makeSnake("UserType must be Selected");
                    return;
                } else if (!pwd.equals(conf_pwd)) {
                    makeSnake("Password and Confirm Password are not same");
                    return;
                } else {
                    registration(firstname, emailaddrss, pwd, lati, longi, user_typpe, regid, m_n);
                }
            }
        });
    }

    public void makeSnake(String msg) {

        Snackbar snackbar1 = Snackbar.make(lay, msg, Snackbar.LENGTH_LONG);
        snackbar1.getView().setBackgroundColor(Color.parseColor("#774A2B"));
        snackbar1.show();
    }

    public void registration(String mfirstname, String memailaddress, String mpassword, String latitude,
                             String logitude, String userType, String dev_id, String mob_number) {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        Call<JsonObject> call = Application.getAppInstance().getServerBackend(RestAdapter.AuthType.UNAUTHARIZED)
                .userSignUp(mfirstname, memailaddress, mpassword, latitude, logitude, userType, dev_id, mob_number);

        new HttpServerBackend(Application.getAppInstance()).getData(call, new HttpServerBackend.ResponseListener() {
                    @Override
                    public void onReturn(boolean success, Response<JsonObject> data) {

                        pDialog.dismiss();
                        if (success) {

                            Log.e(" Login Response ", data.body().toString() + " response");
                            try {

                                JSONObject result = new JSONObject(data.body().toString());

//        new VUBaseAsyncTask(SignUpActivity.this, null, true, Constant.HOST + Constant.REGISTRATION, VUBaseAsyncTask.METHOD_POST,
//                new String[]{"username", "email_address", "password", "latitude", "longitude", "user_type", "device_id", "mobile_number"},
//                new String[]{mfirstname, memailaddress, mpassword, latitude, logitude, userType, dev_id, mob_number}, new VUIBaseAsyncTask() {
//            @Override
//            public void onTaskCompleted(int success, JSONObject result) {
//                if (success == 1) {


                                JSONObject jsonObject = result.getJSONObject("user_array");
                                String useid = jsonObject.getString("user_id");
                                Log.d("regisuid", useid);
                                String usertype = jsonObject.getString("user_type");
                                String uname = jsonObject.getString("username");
                                String emailaddress = jsonObject.getString("email_address");
                                String mobilenumber = jsonObject.getString("mobile_number");
                                sharedPreferences.edit().putString(Constant.NEW_ID, useid).apply();
                                //sharedPreferences.edit().putBoolean(Constant.KEY_IS_LOGGED_IN,true).apply();
                                //sharedPreferences.edit().putString(Constant.USERID,useid).apply();
                                sharedPreferences.edit().putString(Constant.NEW_NAME, uname).apply();
                                sharedPreferences.edit().putString(Constant.NEW_NUMBER, mobilenumber).apply();
                                sharedPreferences.edit().putString(Constant.NEW_EMAIL, emailaddress).apply();
                                sharedPreferences.edit().putString(Constant.IS_LOGIN, "1").apply();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Intent in = new Intent(SignUpActivity.this, Navigation_Drawer_Activity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                                Toast.makeText(SignUpActivity.this, getResources().getString(R.string.try_Again), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );//.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public void checkLocation() {
        Log.d("checkloc", "insideloc");

        //gps testing start
        GpsTracker gps = new GpsTracker(SignUpActivity.this);
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
                pDialog = new ProgressDialog(SignUpActivity.this);

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

                        gcm = GoogleCloudMessaging.getInstance(SignUpActivity.this);
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


}
