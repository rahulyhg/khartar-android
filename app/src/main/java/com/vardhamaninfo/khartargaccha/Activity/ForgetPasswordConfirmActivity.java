package com.vardhamaninfo.khartargaccha.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.vardhaman.vardhamanutilitylibrary.util.VUStringUtility;
import com.vardhamaninfo.khartargaccha.Application.Application;
import com.vardhamaninfo.khartargaccha.R;
import com.vardhamaninfo.khartargaccha.Retrofit.HttpServerBackend;
import com.vardhamaninfo.khartargaccha.Retrofit.RestAdapter;

import retrofit2.Call;
import retrofit2.Response;

public class ForgetPasswordConfirmActivity extends AppCompatActivity {

    EditText code, fpassword, confpassword;
    Button submit;
    LinearLayout layout;
    private SharedPreferences prefs;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_confirm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        code = (EditText) findViewById(R.id.fcode);
        fpassword = (EditText) findViewById(R.id.fpassword);
        confpassword = (EditText) findViewById(R.id.fconpassword);
        submit = (Button) findViewById(R.id.fsubmit);
        layout = (LinearLayout) findViewById(R.id.conf_forg);
        prefs = PreferenceManager.getDefaultSharedPreferences(ForgetPasswordConfirmActivity.this);
        email = getIntent().getStringExtra("email");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fscode = code.getText().toString();
                String fspassord = fpassword.getText().toString();
                String fsconpwd = confpassword.getText().toString();
                if (!VUStringUtility.stringNotEmpty(fscode)) {
                    makeSnake("UserName must be Enter");
                    return;
                } else if (!VUStringUtility.stringNotEmpty(fspassord)) {
                    makeSnake("EmailAddress must be Enter");

                    return;
                } else if (!VUStringUtility.stringNotEmpty(fsconpwd)) {
                    makeSnake("EmailAddress must be Enter");

                    return;
                }
                fsforgotpassword(email, fscode, fspassord);
            }
        });


    }


    public void fsforgotpassword(String fsemail, String fcode, String fpassword) {
        //userForgotPasswordConfirm

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        Call<JsonObject> call = Application.getAppInstance().getServerBackend(RestAdapter.AuthType.UNAUTHARIZED)
                .userForgotPasswordConfirm(fsemail, fcode, fpassword);

        new HttpServerBackend(Application.getAppInstance()).getData(call, new HttpServerBackend.ResponseListener() {
            @Override
            public void onReturn(boolean success, Response<JsonObject> data) {

                pDialog.dismiss();
                if (success) {

                    Log.e("  Response ", data.body().toString() + " response");
//                    try {
//
//                        JSONObject result = new JSONObject(data.body().toString());


//        new VUBaseAsyncTask(ForgetPasswordConfirmActivity.this, null, true, Constant.HOST + Constant.POSTFORGOTPASSWORD,
//                VUBaseAsyncTask.METHOD_POST,
//                new String[]{"email_address", "code", "password"},
//                new String[]{fsemail,fcode,fpassword}, new VUIBaseAsyncTask() {
//            @Override
//            public void onTaskCompleted(int success, JSONObject result) {
//                if (success==1){


                    Intent in = new Intent(ForgetPasswordConfirmActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                    Toast.makeText(getBaseContext(), "successfully", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(ForgetPasswordConfirmActivity.this, "invalid username and password", Toast.LENGTH_SHORT).show();
                }


            }
        });//.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);


    }

    public void makeSnake(String msg) {

        Snackbar snackbar1 = Snackbar.make(layout, msg, Snackbar.LENGTH_LONG);
        snackbar1.getView().setBackgroundColor(Color.parseColor("#774A2B"));
        snackbar1.show();

    }


}
