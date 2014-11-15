package guay.philippe.capstone;

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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Data.Quiz;
import android.os.AsyncTask;
import android.util.Log;


public class TaskGetNewQuiz extends AsyncTask<String, Void, List<Quiz>> {
	public IApiAccessResponse delegate=null;
	HttpClient mHttpclient = new DefaultHttpClient();
	HttpGet mHttpGet = new HttpGet("http://10.0.2.2:8080/quiz");
	
    protected List<Quiz> doInBackground(String... params) {
		 Log.d("MUTIBO", "GetNewQuizTask Execute HTTP Post Request");
		 HttpResponse response;
		 try {
			response  = mHttpclient.execute(mHttpGet);
		 } catch (ClientProtocolException e) {
			e.printStackTrace();
			response = null;
		 } catch (IOException e) {
			e.printStackTrace();
			response = null;
		 }
		 return (List<Quiz>) response;
	}

    protected void onPostExecute(HttpResponse response) {
    	String result = null;
    	Log.d("MUTIBO", "NewQuizTask::onPostExecute response " + response.toString());
    	if(delegate!=null)  {
	   		
			try {
				Log.d("MUTIBO", "NewQuizTask::onPostExecute StatusCode " + response.getStatusLine().getStatusCode());
				result = Utils.inputStreamToString(response.getEntity().getContent()).toString();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
//	       Log.d("MUTIBO", "Response String " + str);
//	       Boolean result = str.toString().equalsIgnoreCase("true");
//	       Log.d("MUTIBO", "AsyncTask result " + result);
           delegate.postResult(Utils.StringtoJSON(result));
        }
    	else {
            Log.e("ApiAccess", "You have not assigned IApiAccessResponse delegate");
        }
    }
    
   

}