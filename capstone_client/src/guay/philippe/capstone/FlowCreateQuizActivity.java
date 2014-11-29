package guay.philippe.capstone;

import guay.philippe.capstone.auth.EasyHttpClient;
import guay.philippe.capstone.data.Player;
import guay.philippe.capstone.data.Quiz;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
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

import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class FlowCreateQuizActivity extends Activity {

	private WeakReference<PostCreatedQuizTask> asyncTaskWeakRef;
	private int TIMEOUT_MILLISEC = 10000; 
	private TextView mTitle;
	private String title;
	
	private TextView mSeekBarValue;
	
	private TextView mExplanation;
	private String explanation;
	
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
	
	private Button mSubmit;
	private Context context;
	private int toast_duration = Toast.LENGTH_SHORT;
	private Toast toast;
	private ArrayList<String> movieSet = new ArrayList<String>();
	private Quiz mCreatedQuiz;
	
	// PUBLIC
	//--------------------------------------------------------------------
	
	// OVERRIDES
	//--------------------------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_quiz);
		context = getApplicationContext();
		mSubmit = (Button) findViewById(R.id.submit_update);
		mTitle = (TextView) findViewById(R.id.quiz_title);
		mExplanation = (TextView) findViewById(R.id.justification);
		mMovie1 = (TextView) findViewById(R.id.movie1);
		mMovie2 = (TextView) findViewById(R.id.movie2);
		mMovie3 = (TextView) findViewById(R.id.movie3);
		mMovie4 = (TextView) findViewById(R.id.movie4);
		mDifficulty = (SeekBar) findViewById(R.id.difficulty);
		mUnrelatedMovieGroup = (RadioGroup) findViewById(R.id.unrelated_movie);
		
		difficulty = mDifficulty.getProgress();
		mDifficulty.setIndeterminate(false);
		
		
		mSeekBarValue = (TextView) findViewById(R.id.seekBarValue);
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
		
		mSubmit.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				title = mTitle.getText().toString();
				explanation = mExplanation.getText().toString();
				movie1 = mMovie1.getText().toString();
				movie2 = mMovie2.getText().toString();
				movie3 = mMovie3.getText().toString();
				movie4 = mMovie4.getText().toString();
				unrelatedMovieGroupId = mUnrelatedMovieGroup.getCheckedRadioButtonId();
				mUnrelatedMovie = (RadioButton) findViewById(unrelatedMovieGroupId);
				unrelatedMovie = Integer.valueOf(mUnrelatedMovie.getTag().toString());

				if ( title.isEmpty()) {
					CharSequence text = "You need to define a title";
					toast = Toast.makeText(context, text, toast_duration);
					toast.show();
				}				
				else if (explanation.isEmpty()) {
					CharSequence text = "You need to provide an explanation";
					toast = Toast.makeText(context, text, toast_duration);
					toast.show();
				}
				else if ( movie1.isEmpty() || movie2.isEmpty() || movie3.isEmpty() || movie4.isEmpty() ) {
					CharSequence text = "You need to provide a complete movie set";
					toast = Toast.makeText(context, text, toast_duration);
					toast.show();
				}
				else {
					Log.d("MUTIBO", "FlowCreateQuizActivity::mSubmit.setOnClickListener Valid Quiz. Sending to server");
					
					movieSet.add(movie1);
					movieSet.add(movie2);
					movieSet.add(movie3);
					movieSet.add(movie4);
					
					Log.d("MUTIBO", "FlowCreateQuizActivity::onCreate difficulty: " + difficulty);
					Player p = Player.getCurrentPlayer(getApplicationContext());
					mCreatedQuiz = new Quiz(title, p.getUsername(), difficulty, explanation, unrelatedMovie, movieSet,0);
					
					JSONObject jsonObj = Utils.QuiztoJSON(mCreatedQuiz);
					startNewAsyncTask(jsonObj);
				}
			}
		});
	}
	// PRIVATE
	//--------------------------------------------------------------------
	private void startNewAsyncTask(JSONObject jsonQuiz) {
		PostCreatedQuizTask asyncTask = new PostCreatedQuizTask(this);
	    this.asyncTaskWeakRef = new WeakReference<PostCreatedQuizTask >(asyncTask);
	    Log.d("MUTIBO", "FlowCreateQuizActivity::startNewAsyncTask executing task");
	    asyncTask.execute(jsonQuiz);
	}
	
	// PRIVATE CLASS
	//--------------------------------------------------------------------
	private class PostCreatedQuizTask extends AsyncTask<JSONObject, Void, HttpResponse> {
		private WeakReference<FlowCreateQuizActivity> fragmentWeakRef;
		
		 private PostCreatedQuizTask (FlowCreateQuizActivity activity) {
			Log.d("MUTIBO", "PostCreatedQuizTask::PostCreatedQuizTask");
            this.fragmentWeakRef = new WeakReference<FlowCreateQuizActivity>(activity);
        }
		
		@Override
		protected HttpResponse doInBackground(JSONObject... jsonQuiz) {
			HttpResponse response = null;
			try{
				
				Log.d("MUTIBO", "FlowCreateQuizActivity::doInBackground Quiz POST request");
				EasyHttpClient client = new EasyHttpClient();
				String url = getApplicationContext().getResources().getString(R.string.quiz_base_endpoint);
				HttpPost post = Utils.setToken(getApplicationContext(), new HttpPost(url));
				
				StringEntity se = new StringEntity(jsonQuiz[0].toString(), "UTF-8");
				se.setContentType("application/json; charset=UTF-8");
				post.setEntity(se);
				response = client.execute(post);
				Log.d("MUTIBO", "Request " + post.toString());
				Log.d("MUTIBO", "entity " + post.getEntity().toString());
				Log.d("MUTIBO", "params " + post.getParams());
				Log.d("MUTIBO", "FlowCreateQuizActivity::doInBackground Server Response " + response.getStatusLine().getStatusCode());
			}
			catch (Throwable t){
				t.printStackTrace();
			}
			finally {
				Log.d("MUTIBO", "FlowCreateQuizActivity::doInBackground Finally Block");
				return response;
			}

		}
		
		@Override
		protected void onPostExecute(HttpResponse response) {
//    			super.onPostExecute(response);
				//mArrayAdapter.setItemList(result);
				Log.d("MUTIBO", "FlowCreateQuizActivity::onPostExecute");
				Boolean res = response.getStatusLine().getStatusCode() == 201;
				if (res){
					Intent intent = new Intent();
					intent.putExtra("Quiz", mCreatedQuiz);
					setResult(Activity.RESULT_OK, intent);
					Log.d("MUTIBO", "Successfully created quiz");
					finish();
				}
				//mArrayAdapter.notifyDataSetChanged();
		}
	}
	
}
