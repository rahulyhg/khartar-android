package com.vardhamaninfo.khartargaccha.Retrofit;

import android.util.Log;

import com.google.gson.JsonObject;
import com.vardhamaninfo.khartargaccha.Application.Application;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HttpServerBackend {

    private final Application context;

    public HttpServerBackend(Application context) {
        this.context = context;
    }

    public static class ResponseListener {
        public ResponseListener() {
        }

        public void onReturn(boolean success, Response<JsonObject> data) {
        }

    }

    public void getData(final Call<JsonObject> call, final ResponseListener back) {

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

      //           Log.e("success getData ","success  "+response.body().get("success").getAsString());
        //         Log.e("response full getData ",response.body().toString()+" mm");

                boolean isSuccess = false;

                try {

                    if (response.body().has("items")) {//YouTube
                    //    Log.e("response YouTube"," has");
                        isSuccess = true;

                    }else if (response.body().get("success").getAsString().equals("1")) {//STATUS_SUCCESS
                        isSuccess = true;
                    } else if (response.body().get("success").getAsString().equals("true")) {//STATUS_ERROR
                        isSuccess = true;
                    } else if (response.body().get("success").getAsString().equals("0")) {//STATUS_ERROR
                        isSuccess = false;

                    } else {
                        isSuccess = false;
                    }
                    back.onReturn(isSuccess, response);
                } catch (Exception e) {
                    back.onReturn(false, null);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                t.printStackTrace();
                //handleIt
                Log.e("onFailure  ", " fghfh");
                back.onReturn(false, null);//STATUS_FAILURE
            }
        });
    }

}
