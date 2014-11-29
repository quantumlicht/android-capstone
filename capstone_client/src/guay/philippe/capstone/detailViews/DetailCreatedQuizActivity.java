package guay.philippe.capstone.detailViews;

import guay.philippe.capstone.R;
import guay.philippe.capstone.Utils;
import guay.philippe.capstone.auth.EasyHttpClient;
import guay.philippe.capstone.data.Player;
import guay.philippe.capstone.data.Quiz;
import guay.philippe.capstone.fragments.CreatedQuizFragment;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tasks.TaskDeleteQuiz;

import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class DetailCreatedQuizActivity extends Activity {

	private WeakReference<PutCreatedQuizTask> asyncTaskWeakRef;
	private TextView mTitle;
	private String title;
	
	private TextView mSeekBarValue;
	
	private TextView mJustification;
	private String justification;
	
	private TextView mMovie1;
	private String movie1;
	
	private TextView mMovie2;
	private String movie2;
	
	private TextView mMovie3;
	private String movie3;
	
	private TextView mMovie4;
	private String movie4;
	
	private SeekBar mDifficulty;
	private int difficulty;
	
	private RadioGroup mUnrelatedMovieGroup;
	private RadioButton mUnrelatedMovie;
	private int unrelatedMovieGroupId;
	private int unrelatedMovie;
	
	private Button mDelete;
	private Button mSubmit;
	private Context context;
	private int toast_duration = Toast.LENGTH_SHORT;
	private Toast toast;
	private ArrayList<String> movieSet = new ArrayList<String>();
	private Quiz createdQuiz;
	
	// PUBLIC
	//--------------------------------------------------------------------
	
	// OVERRIDES
	//--------------------------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_created_quiz);
		context = getApplicationContext();
		
		Bundle bundle = getIntent().getExtras();
		createdQuiz = bundle.getParcelable("quiz");
		
		mSubmit = (Button) findViewById(R.id.submit_update);
		mDelete = (Button) findViewById(R.id.delete);
		mTitle = (TextView) findViewById(R.id.quiz_title);
		mJustification = (TextView) findViewById(R.id.justification);
		mMovie1 = (TextView) findViewById(R.id.movie1);
		mMovie2 = (TextView) findViewById(R.id.movie2);
		mMovie3 = (TextView) findViewById(R.id.movie3);
		mMovie4 = (TextView) findViewById(R.id.movie4);
		mDifficulty = (SeekBar) findViewById(R.id.difficulty);
		mUnrelatedMovieGroup = (RadioGroup) findViewById(R.id.unrelated_movie);
		
		difficulty = mDifficulty.getProgress();
		mDifficulty.setIndeterminate(false);
		mSeekBarValue = (TextView) findViewById(R.id.seekBarValue);
		
		
		fillQuizForm(createdQuiz);
		
		mDifficulty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
			  	@Override
			    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			        mSeekBarValue.setText("Difficulty: " + String.valueOf(progress + 1));
			        difficulty = progress + 1;
			    }

			    @Override
			    public void onStartTrackingTouch(SeekBar seekBar) {

			    }

			    @Override
			    public void onStopTrackingTouch(SeekBar seekBar) {

			    }
		});
		
		mDelete.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v){
				Log.d("MUTIBO", "DetailCreatedQuizActivity::Delete Click listener");
				TaskDeleteQuiz task = new TaskDeleteQuiz(getBaseContext());
				task.execute(createdQuiz);
				//TODO: Add dialog to make sure to delete
			}
		});
		
		mSubmit.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				title = mTitle.getText().toString();
				justification = mJustification.getText().toString();
				movie1 = mMovie1.getText().toString();
				movie2 = mMovie2.getText().toString();
				movie3 = mMovie3.getText().toString();
				movie4 = mMovie4.getText().toString();
				unrelatedMovieGroupId = mUnrelatedMovieGroup.getCheckedRadioButtonId();
				mUnrelatedMovie = (RadioButton) findViewById(unrelatedMovieGroupId);
				unrelatedMovie = Integer.valueOf(mUnrelatedMovie.getText().toString());

				if ( title == "") {
					CharSequence text = "You need to define a title";
					toast = Toast.makeText(context, text, toast_duration);
					toast.show();
				}				
				else if (justification == "" ) {
					CharSequence text = "You need to provide an explanation";
					toast = Toast.makeText(context, text, toast_duration);
					toast.show();
				}
				else if ( movie1 == "" || movie2 == "" || movie3 == "" || movie4 == "") {
					CharSequence text = "You need to provide a complete movie set";
					toast = Toast.makeText(context, text, toast_duration);
					toast.show();
				}
				else{
					Log.d("MUTIBO", "DetailCreatedQuizActivity::mSubmit.setOnClickListener Valid Quiz. Sending to server");
					
					movieSet.add(movie1);
					movieSet.add(movie2);
					movieSet.add(movie3);
					movieSet.add(movie4);
					
					Log.d("MUTIBO", "DetailCreatedQuizActivity::onCreate difficulty: " + difficulty);
					Player p = Player.getCurrentPlayer(getApplicationContext());
					createdQuiz = new Quiz(title, p.getUsername(), difficulty, justification, unrelatedMovie, movieSet,createdQuiz.getRating());
					
					JSONObject jsonObj = Utils.QuiztoJSON(createdQuiz);
					startNewAsyncTask(jsonObj);
				}
			}
		});
	}
	// PRIVATE
	//--------------------------------------------------------------------
	private void startNewAsyncTask(JSONObject jsonQuiz) {
		PutCreatedQuizTask asyncTask = new PutCreatedQuizTask(this);
	    this.asyncTaskWeakRef = new WeakReference<PutCreatedQuizTask >(asyncTask);
	    Log.d("MUTIBO", "DetailCreatedQuizActivity::startNewAsyncTask executing task");
	    asyncTask.execute(jsonQuiz);
	}
	
	private void fillQuizForm(Quiz q){
		ArrayList<String> movieSet = q.getMovieSet();
		mTitle.setText(q.getName());
		mMovie1.setText(movieSet.get(0));
		mMovie2.setText(movieSet.get(1));
		mMovie3.setText(movieSet.get(2));
		mMovie4.setText(movieSet.get(3));
		mDifficulty.setProgress(q.getDifficulty() - 1); // 0-based index
		mSeekBarValue.setText(Integer.toString(q.getDifficulty()));
		mJustification.setText(q.getJustification());
		
		switch(q.getUnrelatedMovie()){
		case 1:
			mUnrelatedMovieGroup.check(((RadioButton)mUnrelatedMovieGroup.getChildAt(0)).getId());
			break;
		case 2:
			mUnrelatedMovieGroup.check(((RadioButton)mUnrelatedMovieGroup.getChildAt(1)).getId());
			break;
		case 3:
			mUnrelatedMovieGroup.check(((RadioButton)mUnrelatedMovieGroup.getChildAt(2)).getId());
			break;
		case 4:
			mUnrelatedMovieGroup.check(((RadioButton)mUnrelatedMovieGroup.getChildAt(3)).getId());
			break;
		}
	}
	
	// PRIVATE CLASS
	//--------------------------------------------------------------------
	private class PutCreatedQuizTask extends AsyncTask<JSONObject, Void, HttpResponse> {
		private WeakReference<DetailCreatedQuizActivity> fragmentWeakRef;
		
		 private PutCreatedQuizTask (DetailCreatedQuizActivity activity) {
	            this.fragmentWeakRef = new WeakReference<DetailCreatedQuizActivity>(activity);
        }
		
		@Override
		protected HttpResponse doInBackground(JSONObject... jsonQuiz) {
			HttpResponse response = null;
			try{
				
				Log.d("MUTIBO", "DetailCreatedQuizActivity Quiz PUT request");
				EasyHttpClient client = new EasyHttpClient();
				String url = getApplicationContext().getResources().getString(R.string.quiz_base_endpoint);
				HttpPut put = Utils.setToken(getBaseContext(), new HttpPut(url));
				
				StringEntity se = new StringEntity(jsonQuiz[0].toString(), "UTF-8");
				se.setContentType("application/json; charset=UTF-8");
				put.setEntity(se);
				response = client.execute(put);
				Log.d("MUTIBO", "DetailCreatedQuizActivity::doInBackground Server Response " + response.getStatusLine().getStatusCode());
			}
			catch (Throwable t){
				t.printStackTrace();
			}
			finally {
				Log.d("MUTIBO", "DetailCreatedQuizActivity::doInBackground Finally Block");
				return response;
			}

		}
		
		@Override
		protected void onPostExecute(HttpResponse response) {
    			//super.onPostExecute(result);
				//mArrayAdapter.setItemList(result);
			Log.d("MUTIBO", "DetailCreatedQuizActivity::onPostExecute");
			Log.d("MUTIBO", "DetailCreatedQuizActivity::onPostExecute statusCode: " +  response.getStatusLine().getStatusCode());
			Boolean res = response.getStatusLine().getStatusCode() == 200;
			Log.d("MUTIBO", "DetailCreatedQuizActivity::onPostExecute statusCode: " +  (response.getStatusLine().getStatusCode()==200));
			if (res){
				Intent intent = new Intent();
				intent.putExtra("Quiz", createdQuiz);
				setResult(Activity.RESULT_OK, intent);
				Log.d("MUTIBO", "DetailCreatedQuizActivity::onPostExecute Successfully created quiz");
				finish();
			}
			//mArrayAdapter.notifyDataSetChanged();
		}
	}
	
	private class TaskDeleteQuiz extends AsyncTask<Quiz, Void, HttpResponse> {
		private Context ctx;
		
		public TaskDeleteQuiz (Context ctx) {
		   this.ctx = ctx;
		}
		
		@Override
		protected HttpResponse doInBackground(Quiz... quizzes) {
			HttpResponse response = null;
			
			try{
				Log.d("MUTIBO", "TaskDeleteQuiz::doInBackground Quiz DELETE request");
				EasyHttpClient client = new EasyHttpClient();
				String strUrl = ctx.getResources().getString(R.string.quiz_base_endpoint) +  URLEncoder.encode(quizzes[0].getName(), "UTF-8");
				HttpDelete delete = Utils.setToken(ctx, new HttpDelete(strUrl));
				response = client.execute(delete);
				
				Log.d("MUTIBO", "TaskDeleteQuiz::doInBackground Server Response " + response.getStatusLine().getStatusCode());
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
//			Intent intent = new Intent (getApplicationContext(), CreatedQuizFragment.class);
//			startActivity(intent);
			Log.d("MUTIBO", "TaskDeleteQuiz::onPostExecute Status: " + response.getStatusLine());
			
			Intent intent = new Intent();
			intent.putExtra("Quiz", createdQuiz);
			intent.putExtra("deleted", true);
			setResult(Activity.RESULT_OK, intent);
			
			Log.d("MUTIBO", "TaskDeleteQuiz::onPostExecute Successfully deleted quiz");
			
//			Intent intent = new Intent("deleted-quiz");
//			intent.putExtra("deletedQuiz", quiz);
//			Log.d("MUTIBO", "TaskDeleteQuiz::onPostExecute Broadcasting event: 'd-player'");
//			LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
			
			finish();
			return;
			
			//Boolean res = response.getStatusLine().getStatusCode() == 200;		
		}
		
	}
	
}
