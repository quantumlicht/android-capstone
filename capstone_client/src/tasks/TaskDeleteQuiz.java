package tasks;

import guay.philippe.capstone.R;
import guay.philippe.capstone.Utils;
import guay.philippe.capstone.auth.EasyHttpClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class TaskDeleteQuiz extends AsyncTask<JSONObject, Void, HttpResponse> {
	private Context ctx;
	
	public TaskDeleteQuiz (Context ctx) {
	   this.ctx = ctx;
	}
	
	@Override
	protected HttpResponse doInBackground(JSONObject... jsonPlayer) {
		HttpResponse response = null;
		
		try{
			Log.d("MUTIBO", "TaskDeleteQuiz::doInBackground Player PUT request");
			EasyHttpClient client = new EasyHttpClient();
			
			HttpDelete delete = Utils.setToken(ctx, new HttpDelete(ctx.getResources().getString(R.string.quiz_base_endpoint)));
			response = client.execute(delete);
			Log.d("MUTIBO", "TaskDeleteQuiz::doInBackground Server Response " + response.getStatusLine().getStatusCode());
		}
		catch (Throwable t){
			t.printStackTrace();
		}
		finally {
			Log.d("MUTIBO", "TaskDeleteQuiz::doInBackground Finally Block");
			return response;
		}
	}
	
	@Override
	protected void onPostExecute(HttpResponse response) {
		//super.onPostExecute(result);
		//mArrayAdapter.setItemList(result);
		Log.d("MUTIBO", "TaskDeleteQuiz::onPostExecute Status: " + response.getStatusLine());
		//Boolean res = response.getStatusLine().getStatusCode() == 200;		
	}
	
}
