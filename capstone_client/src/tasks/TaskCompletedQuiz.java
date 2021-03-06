package tasks;

import guay.philippe.capstone.R;
import guay.philippe.capstone.Utils;
import guay.philippe.capstone.auth.EasyHttpClient;
import guay.philippe.capstone.data.CompletedQuiz;

import java.lang.ref.WeakReference;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


public class TaskCompletedQuiz extends AsyncTask<JSONObject, Void, HttpResponse> {
	private Context ctx;
	private String httpMethod;
	private CompletedQuiz compQ;
	
	public TaskCompletedQuiz (Context ctx, String restMethod) {
		 this.ctx = ctx;
		 this.httpMethod = restMethod.toLowerCase(Locale.CANADA);
	}
	
	@Override
	protected HttpResponse doInBackground(JSONObject... jsonCompletedQuiz) {
		HttpResponse response = null;
		String strUrl = ctx.getResources().getString(R.string.completedquiz_base_endpoint);
		
		try {
			compQ = Utils.convertCompletedQuiz(jsonCompletedQuiz[0]);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		HttpPost postReq = null;
		HttpPut putReq = null;
		Log.d("MUTIBO", "TaskCompletedQuiz::doInBackground httpMethod: " + httpMethod);
		if (httpMethod.equals("post")) {
			postReq = Utils.setToken(ctx, new HttpPost(strUrl));
		}
		else if (httpMethod.equals("put")) {
			putReq = Utils.setToken(ctx, new HttpPut(strUrl));
		}
		else {
			Log.e("MUTIBO", "TaskCompletedQuiz::doInBackground Unknown Http Method: " + httpMethod);
		}
		
		try{
			Log.d("MUTIBO", "TaskPostCompletedQuiz" + httpMethod + " request");
			EasyHttpClient client = new EasyHttpClient();
			
			StringEntity se = new StringEntity(jsonCompletedQuiz[0].toString(), "UTF-8");
			se.setContentType("application/json; charset=UTF-8");
			if (postReq != null && putReq==null) {
				postReq.setEntity(se);
				response = client.execute(postReq);
				
			}
			else if (putReq != null && postReq==null) {
				putReq.setEntity(se);
				response = client.execute(putReq);
			}
			else {
				Log.e("MUTIBO", "TaskPostCompletedQuiz:: Error parsing request");
			}
			Log.d("MUTIBO", "TaskPostCompletedQuiz Server Response " + response.getStatusLine().getStatusCode());
			
		}
		catch (Throwable t){
			t.printStackTrace();
		}
		return response;
	}
	
	@Override
	protected void onPostExecute(HttpResponse response) {
		//super.onPostExecute(result);
		//mArrayAdapter.setItemList(result);
		Log.d("MUTIBO", "TaskUpdatTaskPostCompletedQuiz::onPostExecute Status: " + response.getStatusLine());
		Intent intent = new Intent("quiz-completed");
		intent.putExtra("completedquiz", compQ);
		intent.putExtra("replace", httpMethod.equals("put"));
		Log.d("MUTIBO", "DetailNewQuizActivity::onPostExecute Broadcasting event: 'quiz-completed'");
		LocalBroadcastManager.getInstance(ctx).sendBroadcast(intent);
		
		//Boolean res = response.getStatusLine().getStatusCode() == 200;		
	}
	
}