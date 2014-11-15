package detailViews;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import guay.philippe.capstone.FlowCreateQuizActivity;
import guay.philippe.capstone.R;
import guay.philippe.capstone.R.id;
import guay.philippe.capstone.R.layout;
import guay.philippe.capstone.R.menu;
import guay.philippe.capstone.Utils;
import Data.Quiz;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

public class DetailNewQuizActivity extends Activity implements View.OnClickListener {
	private Quiz quiz;
	private View mRatingGroup;
	private TextView mTitle;
	private TextView mDifficulty;
	private Button mMovie1;
	private Button mMovie2;
	private Button mMovie3;
	private Button mMovie4;
	private TextView mJustification;
	private Button mUpvote;
	private Button mDownvote;
	private Button mNotNow;
	private Button mAnswer;
	private WeakReference<TaskNewQuizDetail> asyncTaskWeakRef;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_new_quiz);
		
		Bundle bundle = getIntent().getExtras();
		quiz = bundle.getParcelable("quiz");
		ArrayList<String> movieSet = quiz.getMovieSet();
		
		mTitle = (TextView) findViewById(R.id.title);
		mDifficulty = (TextView) findViewById(R.id.new_quiz_difficulty);
		mMovie1 = (Button) findViewById(R.id.new_quiz_movie1);
		
		mMovie2 = (Button) findViewById(R.id.new_quiz_movie2);
		
		mMovie3 = (Button) findViewById(R.id.new_quiz_movie3);
		
		mMovie4 = (Button) findViewById(R.id.new_quiz_movie4);
		
		mJustification = (TextView) findViewById(R.id.new_quiz_justification);
		mUpvote = (Button) findViewById(R.id.upvote);
		mDownvote = (Button) findViewById(R.id.downvote);
		mNotNow = (Button) findViewById(R.id.notnow);
		
		mRatingGroup = (View) findViewById(R.id.rating_group);
		
		mTitle.setText(quiz.getName());
		mDifficulty.setText("Difficulty: " + quiz.getDifficulty());
		mMovie1.setText(movieSet.get(0));
		mMovie2.setText(movieSet.get(1));
		mMovie3.setText(movieSet.get(2));
		mMovie4.setText(movieSet.get(3));
		
		mJustification.setText(quiz.getJustification());
		switch(quiz.getUnrelatedMovie()){
			case 0:
				mAnswer = mMovie1;
				break;
			case 1:
				mAnswer = mMovie2;
				break;
			case 2:
				mAnswer = mMovie3;
				break;
			case 3:
				mAnswer = mMovie4;
				break;
		}
		
		mMovie1.setOnClickListener(this);
		mMovie2.setOnClickListener(this);
		mMovie3.setOnClickListener(this);
		mMovie4.setOnClickListener(this);
		
		Button.OnClickListener voteListener = new Button.OnClickListener(){
			public void onClick(View v){
				switch(v.getId()){
					case R.id.downvote:
						quiz.setRating(quiz.getRating()-1);
						startNewAsyncTask(quiz);
						break;
					case R.id.upvote:
						quiz.setRating(quiz.getRating()+1);
						startNewAsyncTask(quiz);
						break;
					case R.id.notnow:
						break;
				}
			}
		};
		

		mUpvote.setOnClickListener(voteListener);
		mDownvote.setOnClickListener(voteListener);
		mNotNow.setOnClickListener(voteListener);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_quiz_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.rating_group) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	 @Override
	 public void onClick(View v) {
	   mRatingGroup.setVisibility(View.VISIBLE);
	   mJustification.setVisibility(View.VISIBLE);
	   
       switch(v.getId()) {
           case R.id.new_quiz_movie1:
        	   if (quiz.getUnrelatedMovie()==1){
	        	 mMovie1.setBackgroundColor(Color.GREEN);
        	   }
        	   else {
        		   mMovie1.setBackgroundColor(Color.RED);
        		   mAnswer.setBackgroundColor(Color.GREEN);        		   
        	   }
        	   break;
           case R.id.new_quiz_movie2:
        	   if (quiz.getUnrelatedMovie()==2){
        		   mMovie2.setBackgroundColor(Color.GREEN);
          	   }
          	   else {
          		   mMovie2.setBackgroundColor(Color.RED);
          		   mAnswer.setBackgroundColor(Color.GREEN);
          	   }
           break;
           case R.id.new_quiz_movie3:
        	   if (quiz.getUnrelatedMovie()==3){
  	        	 mMovie3.setBackgroundColor(Color.GREEN);
          	   }
          	   else {
          		   mMovie3.setBackgroundColor(Color.RED);
          		   mAnswer.setBackgroundColor(Color.GREEN);
          	   }
	           break;
           case R.id.new_quiz_movie4:
        	   if (quiz.getUnrelatedMovie()==4){
  	        	 mMovie4.setBackgroundColor(Color.GREEN);
  	        	
          	   }
          	   else {
          		   mMovie4.setBackgroundColor(Color.RED);
          		   mAnswer.setBackgroundColor(Color.GREEN);
          	   }
	           break;
       }
       mMovie1.setOnClickListener(null);
       mMovie2.setOnClickListener(null);
       mMovie3.setOnClickListener(null);
       mMovie4.setOnClickListener(null);
	 }
	 
	 private void startNewAsyncTask(Quiz q) {
		 TaskNewQuizDetail asyncTask = new TaskNewQuizDetail(this);
		    this.asyncTaskWeakRef = new WeakReference<TaskNewQuizDetail>(asyncTask);
		    Log.d("MUTIBO", "DetailNewQuizActivity::startNewAsyncTask executing task");
		    
		    JSONObject jsonQuiz = Utils.QuiztoJSON(q);
		    asyncTask.execute(jsonQuiz);
	}
	 
	private class TaskNewQuizDetail extends AsyncTask<JSONObject, Void, HttpResponse> {
			private WeakReference<DetailNewQuizActivity> fragmentWeakRef;
			
			 private TaskNewQuizDetail (DetailNewQuizActivity activity) {
		            this.fragmentWeakRef = new WeakReference<DetailNewQuizActivity>(activity);
	        }
			
			@Override
			protected HttpResponse doInBackground(JSONObject... jsonQuiz) {
				HttpResponse response = null;
				try{
					
					Log.d("MUTIBO", "CreateQuizActivity Quiz PUT request");
					HttpClient client = new DefaultHttpClient();
					//TODO Try resetting timeout
					//HttpConnectionParams.setConnectionTimeout(client.getParams(), TIMEOUT_MILLISEC);
					//HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
					HttpPut put = new HttpPut("http://10.0.2.2:8080/quiz");
					
					StringEntity se = new StringEntity(jsonQuiz[0].toString(), "UTF-8");
					se.setContentType("application/json; charset=UTF-8");
					put.setEntity(se);
					response = client.execute(put);
					Log.d("MUTIBO", "Request " + put.toString());
					Log.d("MUTIBO", "entity " + put.getEntity().toString());
					Log.d("MUTIBO", "params " + put.getParams());
					Log.d("MUTIBO", "Server Response " + response.getStatusLine().getStatusCode());
				}
				catch (Throwable t){
					t.printStackTrace();
				}
				finally {
					Log.d("MUTIBO", "CreateQuizActivity Finally Block");
					return response;
				}

			}
			
			@Override
			protected void onPostExecute(HttpResponse response) {
	    			//super.onPostExecute(result);
					//mArrayAdapter.setItemList(result);
					Log.d("MUTIBO", "DetailNewQuizActivity::onPostExecute");
					//Boolean res = response.getStatusLine().getStatusCode() == 200;
					//Log.d("MUTIBO", "CreateQuizActivity::onPostExecute Status: " + response.getStatusLine());
//					if (res){
//						Intent intent = new Intent();
//						intent.putExtra("Quiz", mCreatedQuiz);
//						setResult(Activity.RESULT_OK, intent);
//						Log.d("MUTIBO", "Successfully created quiz");
//						finish();
//					}
				
					//Intent intent = new Intent();
					//new Intent (getActivity().getApplicationContext(), FlowCreateQuizActivity.class);
					//mArrayAdapter.notifyDataSetChanged();
			}
		}
}
