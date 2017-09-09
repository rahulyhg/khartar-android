package com.vardhaman.vardhamanutilitylibrary.asynctasks;

import org.json.JSONObject;

public interface VUIBaseAsyncTask {

	public void onTaskCompleted(int success, JSONObject result);
}
