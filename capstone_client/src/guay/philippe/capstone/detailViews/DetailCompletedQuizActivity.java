package guay.philippe.capstone.detailViews;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import tasks.TaskCompletedQuiz;
import tasks.TaskVoteQuiz;


import guay.philippe.capstone.R;
import guay.philippe.capstone.Utils;
import guay.philippe.capstone.R.id;
import guay.philippe.capstone.R.layout;
import guay.philippe.capstone.R.menu;
import guay.philippe.capstone.auth.EasyHttpClient;
import guay.philippe.capstone.data.CompletedQuiz;
import guay.philippe.capstone.data.Player;
import guay.philippe.capstone.data.Quiz;
import guay.philippe.capstone.fragments.CompletedQuizFragment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailCompletedQuizActivity extends Activity {
	private CompletedQuiz completedQuiz;
	private Quiz quiz;
	private View mRatingGroup;
	private Boolean mHasVoted;
	private TextView mTitle;
	private TextView mDifficulty;
	private Button mMovie1;
	private Button mMovie2;
	private Button mMovie3;
	private Button mMovie4;
	private TextView mJustification;
	//TODO: Display Answer in text + a visual highlight for correct and wrong answer
	//private Button mUpvote;
	//private Button mDownvote;
	//private Button mNotNow;
	
	//PUBLIC
	//-------------------------------------------------
	
	//OVERRIDES
	//--------------------------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_completed_quiz);
		
		Bundle bundle = getIntent().getExtras();
		completedQuiz = bundle.getParcelable("completedQuiz");
		mHasVoted = bundle.getBoolean("hasVoted");
		
		mTitle = (TextView) findViewById(R.id.title);
		mDifficulty = (TextView) findViewById(R.id.completed_quiz_difficulty);
		mMovie1 = (Button) findViewById(R.id.completed_quiz_movie1);
		mMovie2 = (Button) findViewById(R.id.completed_quiz_movie2);
		mMovie3 = (Button) findViewById(R.id.completed_quiz_movie3);
		mMovie4 = (Button) findViewById(R.id.completed_quiz_movie4);
		
		mJustification = (TextView) findViewById(R.id.completed_quiz_justification);
		Button mUpvote = (Button) findViewById(R.id.upvote);
		Button mDownvote = (Button) findViewById(R.id.downvote);
		Button mNotNow = (Button) findViewById(R.id.notnow);
		
		mRatingGroup = (View) findViewById(R.id.rating_group);
		
		Button.OnClickListener voteListener = new Button.OnClickListener(){
			public void onClick(View v){
				switch(v.getId()){
					case R.id.downvote:
						quiz.setRating(quiz.getRating()-1);
						startNewVoteAsyncTask(quiz);
						mHasVoted = true;
						break;
					case R.id.upvote:
						quiz.setRating(quiz.getRating()+1);
						startNewVoteAsyncTask(quiz);
						mHasVoted = true;
						break;
					case R.id.notnow:
//						Intent intent = new Intent();
//						intent.putExtra("Quiz", quiz);
//						setResult(Activity.RESULT_OK, intent);
						mHasVoted = false;
				}
				mRatingGroup.setVisibility(View.INVISIBLE);
				
				completedQuiz.setHasVoted(mHasVoted);
				if (mHasVoted) {
					startUpdateCompletedQuizAsyncTask(completedQuiz);
				}
			}
			
		};
		if (!mHasVoted) {
			mUpvote.setOnClickListener(voteListener);
			mDownvote.setOnClickListener(voteListener);
			mNotNow.setOnClickListener(voteListener);
		}
		else {
			Log.d("MUTIBO", "DetailCompletedQuizActivity::onCreate User has voted, disabling clickListener on voting buttons");
			mRatingGroup.setVisibility(View.INVISIBLE);
		}
		TaskGetQuizByQuizName asyncTask = new TaskGetQuizByQuizName(this);
	    asyncTask.execute();
	} 
	
	//PRIVATE
	//--------------------------------------------------------------------
	
	private void setAnswers() {
		int real_answer = quiz.getUnrelatedMovie();
		int player_answer = completedQuiz.getAnswer();
		TextView mRealAnswer = null;
		TextView mPlayerAnswer = null;
		
		switch(quiz.getUnrelatedMovie()){
			case 1:
				mRealAnswer = mMovie1;
				break;
			case 2:
				mRealAnswer = mMovie2;
				break;
			case 3:
				mRealAnswer = mMovie3;
				break;
			case 4:
				mRealAnswer = mMovie4;
				break;
		}
		
		switch(completedQuiz.getAnswer()){
		case 1:
			mPlayerAnswer = mMovie1;
			break;
		case 2:
			mPlayerAnswer = mMovie2;
			break;
		case 3:
			mPlayerAnswer = mMovie3;
			break;
		case 4:
			mPlayerAnswer = mMovie4;
			break;
		}
		if (real_answer == player_answer){
			mRealAnswer.setBackgroundColor(Color.GREEN);
		}
		else {
			mRealAnswer.setBackgroundColor(Color.GREEN);
			mPlayerAnswer.setBackgroundColor(getResources().getColor(R.color.choice_color));
		}
	}
	
	private void startNewVoteAsyncTask(Quiz q) {
		TaskVoteQuiz asyncTask = new TaskVoteQuiz(this);
		//this.asyncTaskWeakRef = new WeakReference<TaskVoteQuiz>(asyncTask);
	    Log.d("MUTIBO", "DetailCompletedQuizActivity::startNewAsyncTask executing task");
	    JSONObject jsonQuiz = Utils.QuiztoJSON(q);
	    asyncTask.execute(jsonQuiz);
	}
	
	private void startUpdateCompletedQuizAsyncTask(CompletedQuiz q) {
		TaskCompletedQuiz asyncTask = new TaskCompletedQuiz(this, "PUT");
	    Log.d("MUTIBO", "DetailCompletedQuizActivity::startUpdateCompletedQuizAsyncTask ");
	    JSONObject jsonCompletedQuiz = Utils.CompletedQuiztoJSON(q);
	    asyncTask.execute(jsonCompletedQuiz);
	}
	
	private class TaskGetQuizByQuizName extends AsyncTask<String, Void, Quiz> {
		private WeakReference<DetailCompletedQuizActivity> fragmentWeakRef;
		
		private TaskGetQuizByQuizName (DetailCompletedQuizActivity fragment) {
	            this.fragmentWeakRef = new WeakReference<DetailCompletedQuizActivity>(fragment);
        }
		
		@Override
		protected Quiz doInBackground(String... params) {
			Quiz result = null;
			HttpResponse response = null;
			try {
				EasyHttpClient client = new EasyHttpClient();
				Player p = Player.getCurrentPlayer(getApplicationContext());
				String strUrl = getResources().getString(R.string.quiz_by_name_endpoint) +  URLEncoder.encode(completedQuiz.getQuizName(), "UTF-8");
				Log.d("MUTIBO", "DetailCompletedQuizActivity::TaskGetQuizByQuizName CompletedQuiz Url: " + strUrl);
				HttpGet request = Utils.setToken(getApplicationContext(), new HttpGet());
				request.setURI(new URI(strUrl));
				request.addHeader("Content-Type", "application/json");
				
//				URL u = new URL(strUrl);
//				HttpURLConnection conn = (HttpURLConnection) u.openConnection();
//	            conn.setRequestMethod("GET");
//	            conn.connect();
//	            Log.d("MUTIBO", "DetailCompletedQuizActivity::TaskGetQuizByQuizName Getting Connection");
//	            InputStream is = conn.getInputStream();
//	            // Read the stream
//	            byte[] b = new byte[1024];
//	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//	            while ( is.read(b) != -1)
//	            baos.write(b);
//	            String JSONResp = new String(baos.toByteArray());
//	            JSONArray arr = new JSONArray(JSONResp);
				response = client.execute(request);
	            JSONObject obj = new JSONObject(EntityUtils.toString(response.getEntity()));
	            result = Utils.convertQuiz(obj);
//	            for (int i=0; i < arr.length(); i++) {
//	                result.add(Utils.convertCompletedQuiz(arr.getJSONObject(i)));
//	            }
	            Log.d("MUTIBO", "DetailCompletedQuizActivity::GetCompletedQuizByQuizNameTask Returning result->" + result.toString());
//	            conn.disconnect();
	        	return result;
			}
			catch (Throwable t) {
				t.printStackTrace();
			}
			finally {
				Log.d("MUTIBO", "Finally Block");
				return result;
			}

		}
		
		@Override
		protected void onPostExecute(Quiz q) {
			quiz = q;
			ArrayList<String> movieSet = quiz.getMovieSet();
			mTitle.setText(quiz.getName());
			mDifficulty.setText("Difficulty: " + quiz.getDifficulty());
			mMovie1.setText(movieSet.get(0));
			mMovie2.setText(movieSet.get(1));
			mMovie3.setText(movieSet.get(2));
			mMovie4.setText(movieSet.get(3));
			mJustification.setText(quiz.getJustification());
			setAnswers();
			
			//super.onPostExecute(result);	
			//mArrayAdapter.setItemList(result);
			//mArrayAdapter.notifyDataSetChanged();
		}
	}
}
