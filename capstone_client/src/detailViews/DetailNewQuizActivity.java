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

import tasks.TaskCompletedQuiz;
import tasks.TaskUpdatePlayer;

import guay.philippe.capstone.FlowCreateQuizActivity;
import guay.philippe.capstone.R;
import guay.philippe.capstone.R.id;
import guay.philippe.capstone.R.layout;
import guay.philippe.capstone.R.menu;
import guay.philippe.capstone.Utils;
import Data.CompletedQuiz;
import Data.Player;
import Data.Quiz;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
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
	private Boolean mHasVoted = false;
	private BroadcastReceiver mMessageReceiver;
	private WeakReference<TaskVoteQuiz> asyncTaskWeakRef;
	
	// OVERRIDE
    //--------------------------------------------------------------------------
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
		Log.d("MUTIBO", "DetailNewQuizActivity::onCreate unrelatedMovie: " + quiz.getUnrelatedMovie());
		switch(quiz.getUnrelatedMovie()){
			case 1:
				mAnswer = mMovie1;
				break;
			case 2:
				mAnswer = mMovie2;
				break;
			case 3:
				mAnswer = mMovie3;
				break;
			case 4:
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
						mHasVoted = true;
						break;
					case R.id.upvote:
						quiz.setRating(quiz.getRating()+1);
						startNewAsyncTask(quiz);
						mHasVoted = true;
						break;
					case R.id.notnow:
						Intent intent = new Intent();
						intent.putExtra("Quiz", quiz);
						setResult(Activity.RESULT_OK, intent);
						mHasVoted = false;
						finish();
				}
			}
		};
		
		mUpvote.setOnClickListener(voteListener);
		mDownvote.setOnClickListener(voteListener);
		mNotNow.setOnClickListener(voteListener);
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.new_quiz_detail, menu);
//		return true;
//	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.rating_group) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
	
	@Override
	public void onClick(View v) {
		mRatingGroup.setVisibility(View.VISIBLE);
		mJustification.setVisibility(View.VISIBLE);
		int answer = 0;
		boolean success = false;
		switch(v.getId()) {
   			case R.id.new_quiz_movie1:
   				answer = 1;
   				success = quiz.getUnrelatedMovie()==1;
   				animateButton(success, mMovie1);
//   				if (quiz.getUnrelatedMovie()==1){
////   					mMovie1.setBackgroundColor(Color.GREEN);
//   					success = true;
//   					animateButton(success, mMovie1);
//   					
//   				}
//   				else {
//   					suc
////        		   mMovie1.setBackgroundColor(Color.RED);
////        		   mAnswer.setBackgroundColor(Color.GREEN);        		   
//   				}
   				break;
   			case R.id.new_quiz_movie2:
   				answer = 2;
   				success = quiz.getUnrelatedMovie()==2;
   				animateButton(success, mMovie2);
//   				if (quiz.getUnrelatedMovie()==2){
//   					success = true;
////   					mMovie2.setBackgroundColor(Color.GREEN);
//   				}
//   				else {
////   					mMovie2.setBackgroundColor(Color.RED);
////   					mAnswer.setBackgroundColor(Color.GREEN);
//   				}
   				break;
   			case R.id.new_quiz_movie3:
   				answer = 3;
   				success = quiz.getUnrelatedMovie()==3;
   				animateButton(success, mMovie3);
//   				if (quiz.getUnrelatedMovie()==3){
//   					success = true;
////   					mMovie3.setBackgroundColor(Color.GREEN);
//   				}
//   				else {
////   					mMovie3.setBackgroundColor(Color.RED);
////   					mAnswer.setBackgroundColor(Color.GREEN);
//   				}
   				break;
       		case R.id.new_quiz_movie4:
       			answer = 4;
       			success = quiz.getUnrelatedMovie()==4;
   				animateButton(success, mMovie4);
//       			if (quiz.getUnrelatedMovie()==4){
//       				success = true;
////        	   			mMovie4.setBackgroundColor(Color.GREEN);
//       			}
//       			else {
////           				mMovie4.setBackgroundColor(Color.RED);
////           				mAnswer.setBackgroundColor(Color.GREEN);
//       			}
       			break;
		}
		
		Player p = Player.getCurrentPlayer(getApplicationContext());
		
		CompletedQuiz compQ = new CompletedQuiz(quiz.getName(), p.getUsername(), answer, success);
		compQ.setHasVoted(mHasVoted);
		JSONObject jsonCompletedQuiz = Utils.CompletedQuiztoJSON(compQ);
		TaskCompletedQuiz postCompletedQuizTask = new TaskCompletedQuiz(getApplicationContext(), "POST");
		postCompletedQuizTask.execute(jsonCompletedQuiz);
		
		if (success) {
			SharedPreferences prefs = Utils.getStorage(getApplicationContext());
        	SharedPreferences.Editor editor = prefs.edit();
        	int score = p.getScore() + quiz.getDifficulty();
        	Log.d("MUTIBO", "Writing score to sharedPrefs: " + score);
        	editor.putInt("score", score);
        	editor.commit();
			p.setScore(score);
			publishPlayerScoreUpdate(p);
		}
		
		TaskUpdatePlayer updatePlayerTask = new TaskUpdatePlayer(getApplicationContext());
	    //this.asyncTaskWeakRef = new WeakReference<TaskPostQuizResult>(asyncTask);
	    Log.d("MUTIBO", "DetailNewQuizActivity::startNewAsyncTask executing task");
	    JSONObject jsonPlayer = Utils.PlayertoJSON(p);
	    updatePlayerTask.execute(jsonPlayer);
		
		mMovie1.setOnClickListener(null);
		mMovie2.setOnClickListener(null);
		mMovie3.setOnClickListener(null);
		mMovie4.setOnClickListener(null);
	}
	
	// PRIVATE
	private void animateButton(boolean success, final Button button){
		
		//StateListDrawable draw = mAnswer.getBackground();
		//draw.getState();
		int colorFrom =  getResources().getColor(R.color.button_color);
		int colorToSuccess = Color.GREEN;
		int colorToFail = Color.RED;
		
		ValueAnimator colorAnimationSuccess = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorToSuccess);
		ValueAnimator colorAnimationFail = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorToFail);
		colorAnimationFail.setDuration(1500);
		colorAnimationSuccess.setDuration(1500);
		if (success) {
			colorAnimationSuccess.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			    @Override
			    public void onAnimationUpdate(ValueAnimator animator) {
			        mAnswer.setBackgroundColor((Integer)animator.getAnimatedValue());
			    }

			});
			colorAnimationSuccess.start();
		}
		else{
			colorAnimationFail.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			    @Override
			    public void onAnimationUpdate(ValueAnimator animator) {
			        button.setBackgroundColor((Integer)animator.getAnimatedValue());
			    }

			});
			
			colorAnimationSuccess.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			    @Override
			    public void onAnimationUpdate(ValueAnimator animator) {
			        mAnswer.setBackgroundColor((Integer)animator.getAnimatedValue());
			    }

			});
			colorAnimationSuccess.start();
			colorAnimationFail.start();
		}
		
		
	}
	
    //--------------------------------------------------------------------------
	private void startNewAsyncTask(Quiz q) {
		TaskVoteQuiz asyncTask = new TaskVoteQuiz(this);
		    this.asyncTaskWeakRef = new WeakReference<TaskVoteQuiz>(asyncTask);
		    Log.d("MUTIBO", "DetailNewQuizActivity::startNewAsyncTask executing task");
		    
		    JSONObject jsonQuiz = Utils.QuiztoJSON(q);
		    asyncTask.execute(jsonQuiz);
	}
	
	private void publishPlayerScoreUpdate(Player p){
		Intent intent = new Intent("update-player");
		  // add data
		intent.putExtra("score", p.getScore());
		intent.putExtra("username", p.getUsername());
		Log.d("MUTIBO", "DetailNewQuizActivity::publishPlayerScoreUpdate Broadcasting event: 'update-player'");
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}
	
	// PRIVATE CLASS
    //--------------------------------------------------------------------------
	private class TaskVoteQuiz extends AsyncTask<JSONObject, Void, HttpResponse> {
		private WeakReference<DetailNewQuizActivity> fragmentWeakRef;
			
		private TaskVoteQuiz (DetailNewQuizActivity activity) {
		            this.fragmentWeakRef = new WeakReference<DetailNewQuizActivity>(activity);
        }
			
		@Override
		protected HttpResponse doInBackground(JSONObject... jsonQuiz) {
			HttpResponse response = null;
			try{
				Log.d("MUTIBO", "DetailNewQuizActivity::TaskVoteQuiz::doInBackground Quiz PUT request");
				HttpClient client = new DefaultHttpClient();
				
				HttpPut put = new HttpPut(getResources().getString(R.string.quiz_base_endpoint));
				
				StringEntity se = new StringEntity(jsonQuiz[0].toString(), "UTF-8");
				se.setContentType("application/json; charset=UTF-8");
				put.setEntity(se);
				response = client.execute(put);
				Log.d("MUTIBO", "Server Response " + response.getStatusLine().getStatusCode());
			}
			catch (Throwable t){
				t.printStackTrace();
			}
			finally {
				Log.d("MUTIBO", "DetailNewQuizActivity::TaskVoteQuiz::doInBackground Finally Block");
				return response;
			}

		}
			
		@Override
		protected void onPostExecute(HttpResponse response) {
			//super.onPostExecute(result);
			//mArrayAdapter.setItemList(result);
			Log.d("MUTIBO", "DetailNewQuizActivity::TaskVoteQuiz::onPostExecute Status: " + response.getStatusLine());
			Boolean res = response.getStatusLine().getStatusCode() == 200;
				
			Intent intent = new Intent();
			intent.putExtra("Quiz", quiz);
			if (res){
				setResult(Activity.RESULT_OK, intent);
			}
			else {
				setResult(Activity.RESULT_CANCELED, intent);
			}
			finish();
		}
	}
}
