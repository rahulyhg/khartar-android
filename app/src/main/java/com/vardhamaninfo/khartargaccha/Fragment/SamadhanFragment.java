package com.vardhamaninfo.khartargaccha.Fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.JsonObject;
import com.vardhaman.vardhamanutilitylibrary.util.VUStringUtility;
import com.vardhamaninfo.khartargaccha.Application.Application;
import com.vardhamaninfo.khartargaccha.R;
import com.vardhamaninfo.khartargaccha.Retrofit.HttpServerBackend;
import com.vardhamaninfo.khartargaccha.Retrofit.RestAdapter;
import com.vardhamaninfo.khartargaccha.Util.Constant;

import retrofit2.Call;
import retrofit2.Response;

import static com.vardhamaninfo.khartargaccha.Util.StringUtility.hideKeyboard;


public class SamadhanFragment extends Fragment {


    LinearLayout lay;
    EditText sama;
    Button sama_sub;
    private SharedPreferences prefs;
    String key_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_samadhan_fragment, container, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        key_id = prefs.getString(Constant.NEW_ID, "");
        lay = (LinearLayout) view.findViewById(R.id.samadhan_lay);
        sama = (EditText) view.findViewById(R.id.sam_ed);
        sama_sub = (Button) view.findViewById(R.id.sam_submit);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.navigation_menu_samadhan);
        sama_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = sama.getText().toString();
                if (!VUStringUtility.stringNotEmpty(text)) {
                    makeSnake("Questions must be write");
                } else {
                    hideKeyboard(getActivity());
                    sendemail(key_id, text);
                }

            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                hideKeyboard(getActivity());

                return true;
            }
        });

        return view;
    }



   /* @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }*/


    void sendemail(String id, String ques) {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        Call<JsonObject> call = Application.getAppInstance().getServerBackend(RestAdapter.AuthType.UNAUTHARIZED)
                .userSamadhan(id, ques);

        new HttpServerBackend(Application.getAppInstance()).getData(call, new HttpServerBackend.ResponseListener() {
            @Override
            public void onReturn(boolean success, Response<JsonObject> data) {

                pDialog.dismiss();
                if (success) {

                    Log.e("  Response ", data.body().toString() + " response");
//                try {
//
//                    JSONObject result = new JSONObject(data.body().toString());

//        new VUBaseAsyncTask(getActivity(), null, true,
//                Constant.HOST + Constant.Samadhan, VUBaseAsyncTask.METHOD_POST,
//                new String[]{"user_id", "question"},
//                new String[]{id,ques}, new VUIBaseAsyncTask() {
//            @Override
//            public void onTaskCompleted(int success, JSONObject result) {
//                if (success==1){
                    makeSnake("Your query has been submitted, we will coordinate with Gurudev and get back to you shortly");
                    //makeSnake("Message Successfully send");
                    sama.setText("");
                } else {
                }


            }
        });//.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public void makeSnake(String msg) {
        Snackbar snackbar1 = Snackbar.make(lay, msg, Snackbar.LENGTH_LONG);
        snackbar1.getView().setBackgroundColor(Color.parseColor("#774A2B"));
        snackbar1.show();

    }


}
