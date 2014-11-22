package tasks;

import guay.philippe.capstone.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class TaskUpdatePlayer extends AsyncTask<JSONObject, Void, HttpResponse> {
	private Context ctx;
	
	public TaskUpdatePlayer (Context ctx) {
	   this.ctx = ctx;
	}
	
	@Override
	protected HttpResponse doInBackground(JSONObject... jsonPlayer) {
		HttpResponse response = null;
		
		try{
			Log.d("MUTIBO", "TaskUpdatePlayer::doInBackground Player PUT request");
			HttpClient client = new DefaultHttpClient();
			
			HttpPut put = new HttpPut(ctx.getResources().getString(R.string.player_base_endpoint));
			
			StringEntity se = new StringEntity(jsonPlayer[0].toString(), "UTF-8");
			se.setContentType("application/json; charset=UTF-8");
			put.setEntity(se);
			response = client.execute(put);
			Log.d("MUTIBO", "TaskUpdatePlayer::doInBackground Server Response " + response.getStatusLine().getStatusCode());
		}
		catch (Throwable t){
			t.printStackTrace();
		}
		finally {
			Log.d("MUTIBO", "TaskUpdatePlayer::doInBackground Finally Block");
			return response;
		}
	}
	
	@Override
	protected void onPostExecute(HttpResponse response) {
		//super.onPostExecute(result);
		//mArrayAdapter.setItemList(result);
		Log.d("MUTIBO", "TaskUpdatePlayer::onPostExecute Status: " + response.getStatusLine());
		//Boolean res = response.getStatusLine().getStatusCode() == 200;		
	}
	
}