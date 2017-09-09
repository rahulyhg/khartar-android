package com.vardhaman.vardhamanutilitylibrary.asynctasks;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.vardhaman.vardhamanutilitylibrary.util.VUJSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VUBaseAsyncTask extends AsyncTask<Void, String, JSONObject> {

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_DELETE = "DELETE";

    public static final String K_SUCCESS = "success";
    public static final String K_MESSAGE = "message";

    private Activity activity;
    private Fragment fragment;
    private boolean showProgressDialog;
    private ProgressDialog pDialog;
    private VUJSONParser jParser;
    private String[] keys, values;
    private String url, requestMethod;
    private VUIBaseAsyncTask _vUIBaseAsyncTask;

    public VUBaseAsyncTask(Activity activity, Fragment fragment, boolean showProgressDialog,
                           String url, String requestMethod, VUIBaseAsyncTask _vUIBaseAsyncTask) {
        this(activity, fragment, showProgressDialog, url, requestMethod, null, null, _vUIBaseAsyncTask);
    }

    public VUBaseAsyncTask(Activity activity, Fragment fragment, boolean showProgressDialog,
                           String url, String requestMethod, String[] keys, String[] values,
                           VUIBaseAsyncTask _vUIBaseAsyncTask) {
        this.activity = activity;
        this.fragment = fragment;
        this.showProgressDialog = showProgressDialog;
        this.url = url;
        this.keys = keys;
        this.values = values;
        this.requestMethod = requestMethod;
        this._vUIBaseAsyncTask = _vUIBaseAsyncTask;

        jParser = new VUJSONParser(activity);
    }

    @Override
    protected void onPreExecute() {
        if (showProgressDialog)
            pDialog = ProgressDialog.show(activity, activity.getApplicationInfo().name, "Loading..");

        super.onPreExecute();

    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        try {
            return getJsonFrom(url, requestMethod, getParameters(keys, values));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected JSONObject getJsonFrom(String url, String requestMethod, List<NameValuePair> params) {
        return jParser.makeHttpRequest(url, requestMethod != null ? requestMethod : METHOD_GET, params);
    }

    protected List<NameValuePair> getParameters(String[] keys, String[] values) throws Exception {
        if (keys == null || keys.length == 0)
            return new ArrayList<>();

        if (keys.length != values.length)
            throw new Exception("keys and values length must be equal");

        ArrayList<NameValuePair> params = new ArrayList<>();
        for (int i = 0; i < keys.length; i++) {
            params.add(new BasicNameValuePair(keys[i], values[i]));
        }

        return params;
    }


    @Override
    protected void onPostExecute(JSONObject result) {
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();

        super.onPostExecute(result);
        int success = 0;
        if (result != null && result.has(K_SUCCESS)) {
            try {
                success = result.getInt(K_SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
                success = 0;
            }
        }
        if (_vUIBaseAsyncTask != null)
            _vUIBaseAsyncTask.onTaskCompleted(success, result);
    }

    protected Activity getActivity() {
        return activity;
    }

    public VUIBaseAsyncTask getVUIBaseAsyncTaskListner() {
        return _vUIBaseAsyncTask;
    }


}
