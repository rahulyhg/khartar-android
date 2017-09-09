package com.vardhamaninfo.khartargaccha.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {
    EditText emailaddress;
    Button submit, resend;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        emailaddress = (EditText) findViewById(R.id.forget_email);
        submit = (Button) findViewById(R.id.submitforgot);
        resend = (Button) findViewById(R.id.resendforgot);
        layout = (LinearLayout) findViewById(R.id.forget_lay);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailaddress.getText().toString();

                if (!VUStringUtility.stringNotEmpty(email)) {
                    makeSnake("UserName must be Enter");
                    return;
                }
                forgotpassword(email);
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailaddress.getText().toString();
                forgotpassword(email);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void forgotpassword(final String femail) {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        Call<JsonObject> call = Application.getAppInstance().getServerBackend(RestAdapter.AuthType.UNAUTHARIZED)
                .forgotPassword(femail);

        new HttpServerBackend(Application.getAppInstance()).getData(call, new HttpServerBackend.ResponseListener() {
            @Override
            public void onReturn(boolean success, Response<JsonObject> data) {

                pDialog.dismiss();
                if (success) {

                    Log.e("  Response ", data.body().toString() + " response");
                    try {

                        JSONObject result = new JSONObject(data.body().toString());

                        Intent in = new Intent(ForgetPasswordActivity.this, ForgetPasswordConfirmActivity.class);
                        in.putExtra("email", femail);
                        startActivity(in);
                        Toast.makeText(getBaseContext(), "successfully", Toast.LENGTH_LONG).show();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "invalid emailid", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }


       /* new VUBaseAsyncTask(ForgetPasswordActivity.this, null, true, Constant.HOST + Constant.GETFORGOTPASSWORD +femail,
       VUBaseAsyncTask.METHOD_GET,

                new VUIBaseAsyncTask() {
                    @Override
                    public void onTaskCompleted(int success, JSONObject result) {
                        if (success==1){

                            JSONObject object = null;
                            // result.getJSONObject("user_array");

                            Intent in = new Intent(ForgetPasswordActivity.this, ForgetPasswordConfirmActivity.class);
                            in.putExtra("email",femail);
                            startActivity(in);
                            Toast.makeText(getBaseContext(), "successfully", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(ForgetPasswordActivity.this, "invalid emailid", Toast.LENGTH_SHORT).show();
                        }


                    }
                }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);*/



    public void makeSnake(String msg) {

        Snackbar snackbar1 = Snackbar.make(layout, msg, Snackbar.LENGTH_LONG);
        snackbar1.getView().setBackgroundColor(Color.parseColor("#774A2B"));
        snackbar1.show();

    }


}
