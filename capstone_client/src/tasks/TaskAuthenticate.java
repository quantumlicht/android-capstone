package tasks;

import guay.philippe.capstone.IApiAccessResponse;
import guay.philippe.capstone.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


public class TaskAuthenticate extends AsyncTask<String, Integer, HttpResponse> {
	
	private String endpoint;
	public IApiAccessResponse delegate=null;
	private HttpClient mHttpclient = new DefaultHttpClient();
	private HttpPost mHttppost;
	public TaskAuthenticate (String endp) {
		this.endpoint = endp;
	}

    protected HttpResponse doInBackground(String... params) {
    	 mHttppost = new HttpPost(endpoint);
   		 Log.d("MUTIBO", "TaskAuthenticate::onPostExecute AuthenticateTaskparams " + params.toString());
		 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		 nameValuePairs.add(new BasicNameValuePair("username", params[0]));
		 nameValuePairs.add(new BasicNameValuePair("password", params[1]));
        
		 try {
			mHttppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    	 
		Log.d("MUTIBO", "TaskAuthenticate::onPostExecute Execute HTTP Post Request");
        HttpResponse response;
		try {
			response  = mHttpclient.execute(mHttppost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			response = null;
		} catch (IOException e) {
			e.printStackTrace();
			response = null;
		}
		return response;
    }

    protected void onProgressUpdate(Integer... progress) {
        publishProgress(progress[0]);
    }

    protected void onPostExecute(HttpResponse response) {
    	Boolean result = null;
    	Log.d("MUTIBO", "TaskAuthenticate::onPostExecute response:" +  response.toString());
    	if(delegate!=null)  {
	   		
			try {
				Log.d("MUTIBO", "TaskAuthenticate::onPostExecute Status Code " + response.getStatusLine().getStatusCode());
				result = response.getStatusLine().getStatusCode() == 200;
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
//	       Log.d("MUTIBO", "Response String " + str);
//	       Boolean result = str.toString().equalsIgnoreCase("true");
//	       Log.d("MUTIBO", "AsyncTask result " + result);
           delegate.postResult(result);
        }
    	else {
            Log.e("ApiAccess", "You have not assigned IApiAccessResponse delegate");
        }
    }
    
    private StringBuilder inputStreamToString(InputStream is) {
        String line = "";
        StringBuilder total = new StringBuilder();
        // Wrap a BufferedReader around the InputStream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        // Read response until the end
        try {
         while ((line = rd.readLine()) != null) { 
           total.append(line); 
         }
        } catch (IOException e) {
         e.printStackTrace();
        }
        // Return full string
        return total;
    }

}