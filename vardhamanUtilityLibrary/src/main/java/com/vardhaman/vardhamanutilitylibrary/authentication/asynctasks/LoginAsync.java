package com.vardhaman.vardhamanutilitylibrary.authentication.asynctasks;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

import com.vardhaman.vardhamanutilitylibrary.asynctasks.VUBaseAsyncTask;
import com.vardhaman.vardhamanutilitylibrary.asynctasks.VUIBaseAsyncTask;

public class LoginAsync extends VUBaseAsyncTask{


    public LoginAsync(Activity activity, Fragment fragment, boolean showProgressDialog, String url, String requestMethod, String userName,String password, VUIBaseAsyncTask _vUIBaseAsyncTask) {
        super(activity, fragment, showProgressDialog, url, requestMethod, new String[]{userName}, new String[]{userName,password}, _vUIBaseAsyncTask);
    }
}
