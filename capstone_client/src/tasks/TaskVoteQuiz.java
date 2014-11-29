package tasks;

import guay.philippe.capstone.R;
import guay.philippe.capstone.Utils;
import guay.philippe.capstone.auth.EasyHttpClient;
import guay.philippe.capstone.detailViews.DetailNewQuizActivity;

import java.lang.ref.WeakReference;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class TaskVoteQuiz extends AsyncTask<JSONObject, Void, HttpResponse> {
//		private WeakReference<DetailNewQuizActivity> fragmentWeakRef;
		private Context ctx;
		
		public TaskVoteQuiz (Context context) {
			this.ctx = context;
//		            this.fragmentWeakRef = new WeakReference<DetailNewQuizActivity>(activity);
        }
					
		@Override
		protected HttpResponse doInBackground(JSONObject... jsonQuiz) {
			HttpResponse response = null;
			try{
				Log.d("MUTIBO", "TaskVoteQuiz::doInBackground PUT request");
				
				EasyHttpClient client = new EasyHttpClient();
				
				//TODO Try resetting timeout
				//HttpConnectionParams.setConnectionTimeout(client.getParams(), TIMEOUT_MILLISEC);
				//HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
				String strUrl = ctx.getResources().getString(R.string.quiz_base_endpoint);
				HttpPut put = Utils.setToken(ctx, new HttpPut(strUrl));
				
				StringEntity se = new StringEntity(jsonQuiz[0].toString(), "UTF-8");
				se.setContentType("application/json; charset=UTF-8");
				put.setEntity(se);
				response = client.execute(put);
				Log.d("MUTIBO", "TaskVoteQuiz::doInBackground Server Response " + response.getStatusLine().getStatusCode());
			}
			catch (Throwable t){
				t.printStackTrace();
			}
			finally {
				Log.d("MUTIBO", "TaskVoteQuiz::doInBackground Finally Block");
				return response;
			}

		}
			
		@Override
		protected void onPostExecute(HttpResponse response) {
			//super.onPostExecute(result);
			//mArrayAdapter.setItemList(result);
			Log.d("MUTIBO", "TaskVoteQuiz::onPostExecute Status: " + response.getStatusLine());
			Boolean res = response.getStatusLine().getStatusCode() == 200;
				
//			Intent intent = new Intent();
//			intent.putExtra("Quiz", quiz);
//			if (res){
//				setResult(Activity.RESULT_OK, intent);
//			}
//			else {
//				setResult(Activity.RESULT_CANCELED, intent);
//			}
//			finish();
		}
	}