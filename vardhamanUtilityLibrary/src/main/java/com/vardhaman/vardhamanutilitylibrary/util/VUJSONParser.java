package com.vardhaman.vardhamanutilitylibrary.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class VUJSONParser {

	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";
	Context con;

	// constructor
	public VUJSONParser(Context c) {
		con = c;
	}

	// function get json from url
	// by making HTTP POST or GET mehtod
	public JSONObject makeHttpRequest(String url, String method,
			List<NameValuePair> params) {

		// Making HTTP request
		try {

			// check for request method
			if (method == "POST") {
				// request method is POST
				// defaultHttpClient
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(params));
				Log.e(getClass().getSimpleName(), "url: " + url + " params: " + params.toString());
				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();

			} else if (method == "GET") {
				// request method is GET
				DefaultHttpClient httpClient = new DefaultHttpClient();
				if(params!=null && params.size()>0){
				String paramString = URLEncodedUtils.format(params, "utf-8");
				url += "?" + paramString;
				}
				Log.e(getClass().getSimpleName(),"url: "+url);
				HttpGet httpGet = new HttpGet(url);

				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();

			} else if (method == "DELETE") {
				// request method is GET
				DefaultHttpClient httpClient = new DefaultHttpClient();
				if(params!=null && params.size()>0){
				String paramString = URLEncodedUtils.format(params, "utf-8");
				url += "?" + paramString;
				}
				Log.e(getClass().getSimpleName(),"url: "+url);
				HttpDelete httpDelete = new HttpDelete(url);

				HttpResponse httpResponse = httpClient.execute(httpDelete);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();

			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
			Log.e("JSON string: ", "" + json);
			jObj = new JSONObject(json);
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			if (json.startsWith("<")) {
				Log.e("JSON Parser contains HTML", "Error parsing data ");
				jObj=new JSONObject("{\"success\": \"-1\"}");}
//			} else
//			{
//				jObj = new JSONObject(json);
//			Log.e("JSONObject", "" + jObj);
//			}
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String
		return jObj;

	}
}