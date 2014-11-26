package tasks;

import guay.philippe.capstone.IApiAccessResponse;
import guay.philippe.capstone.R;
import guay.philippe.capstone.Utils;
import guay.philippe.capstone.auth.AuthenticationParameters;
import guay.philippe.capstone.auth.EasyHttpClient;
import guay.philippe.capstone.auth.UnsafeHttpsClient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;


public class TaskAuthenticate extends AsyncTask<String, Integer, JSONArray> {
	
//	private String endpoint;
	private Context ctx;
	public IApiAccessResponse delegate=null;
//	private HttpClient mHttpclient = UnsafeHttpsClient.getNewHttpClient();
	private HttpPost mHttppost;
	private String client_string = "mobile:";
	public TaskAuthenticate (Context ctx) {
		this.ctx = ctx;
	}
	
	
    protected JSONArray doInBackground(String... params) {
    	
		 String strTokenUrl = ctx.getResources().getString(R.string.oauth2_endpoint);
		 EasyHttpClient client = new EasyHttpClient();
		 
		 mHttppost = new HttpPost();
		 try {
			 mHttppost.setURI(new URI(strTokenUrl));
			 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			 nameValuePairs.add(new BasicNameValuePair("username", params[0]));
			 nameValuePairs.add(new BasicNameValuePair("password", params[1]));
			 nameValuePairs.add(new BasicNameValuePair("grant_type", "password"));
			 nameValuePairs.add(new BasicNameValuePair("client_id", "mobile"));
			 nameValuePairs.add(new BasicNameValuePair("client_secret",""));
			 // NO_WRAP bitmask is important because post request should not have end of line characters in header. For get requests it is ok..
			 String header = "Basic " + Base64.encodeToString(client_string.getBytes("UTF-8"), Base64.URL_SAFE|Base64.NO_WRAP);
			mHttppost.setHeader("Authorization", header);
			mHttppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			mHttppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		 }
		 catch (URISyntaxException e1) {
			 e1.printStackTrace();
		 } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		  
		String response  = client.post(mHttppost);
		JSONArray result = new JSONArray();
		try {
			result.put(new JSONObject(response));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
//		try {
//			//Log.d("MUTIBO", "TaskAuthenticate::doInBackground entity: " + Utils.inputStreamToString(response.getEntity().getContent()));
//			InputStream is = response.getEntity().getContent();
//			//result = new JSONArray(EntityUtils.toString(response.getEntity()));
//			
//			// Read the stream
//            byte[] b = new byte[1024];
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            while ( is.read(b) != -1)
//            baos.write(b);
//            String JSONResp = new String(baos.toByteArray());
//			Log.d("MUTIBO", "TaskAuthenticate::doInbackground str: " + JSONResp);
////			if(respEntity!=null) {
////				String res = EntityUtils.toString(respEntity);
////				Log.d("MUTIBO", "TaskAuthenticate::doInBackground entity is NOT null: " + res);
////			}
////			else {
////				Log.d("MUTIBO", "TaskAuthenticate::doInBackground entity is null");
////			}
//		} catch (ParseException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		Log.d("MUTIBO", "TaskAuthenticate::doInBackground Status Code: " + response.getStatusLine().getStatusCode());
		Log.d("MUTIBO", "TaskAuthenticate::doInBackground response: " + response);
		return result;

    }

    protected void onPostExecute(JSONArray response) {
    	String result = null;
    	if(delegate!=null)  {
			delegate.postResult(response);
        }
    	else {
            Log.e("ApiAccess", "You have not assigned IApiAccessResponse delegate");
        }
    }
}