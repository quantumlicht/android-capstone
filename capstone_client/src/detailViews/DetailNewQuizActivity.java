package detailViews;

import java.util.ArrayList;

import guay.philippe.capstone.R;
import guay.philippe.capstone.R.id;
import guay.philippe.capstone.R.layout;
import guay.philippe.capstone.R.menu;
import Data.Quiz;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
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
	private TextView mUnrelatedMovie;
	private TextView mJustification;
	private Button mUpvote;
	private Button mDownvote;
	private Button mNotNow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_new_quiz);
		
		Bundle bundle = getIntent().getExtras();
		quiz = bundle.getParcelable("quiz");
		ArrayList<String> movieSet = quiz.getMovieSet();
		
		mTitle = (TextView) findViewById(R.id.title);
		mDifficulty = (TextView) findViewById(R.id.difficulty);
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
		mMovie1.setText(movieSet.get(1));
		mMovie2.setText(movieSet.get(2));
		mMovie3.setText(movieSet.get(3));
		mMovie4.setText(movieSet.get(4));
		
		mJustification.setText(quiz.getJustification());
		
		mMovie1.setOnClickListener(this);
		mMovie2.setOnClickListener(this);
		mMovie3.setOnClickListener(this);
		mMovie4.setOnClickListener(this);
		
		mUpvote.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				//TODO: Send Request to server
			}
		});
		
		mUpvote.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				//TODO: Send Request to server
			}
		});
		
		mNotNow.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				//TODO: Send Request to server
			}
		});
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
        	   }
        	   break;
           case R.id.new_quiz_movie2:
        	   if (quiz.getUnrelatedMovie()==2){
  	        	 mMovie2.setBackgroundColor(Color.GREEN);
          	   }
          	   else {
          		   mMovie2.setBackgroundColor(Color.RED);
          	   }
           break;
           case R.id.new_quiz_movie3:
        	   if (quiz.getUnrelatedMovie()==3){
  	        	 mMovie3.setBackgroundColor(Color.GREEN);
          	   }
          	   else {
          		   mMovie3.setBackgroundColor(Color.RED);
          	   }
	           break;
           case R.id.new_quiz_movie4:
        	   if (quiz.getUnrelatedMovie()==4){
  	        	 mMovie4.setBackgroundColor(Color.GREEN);
          	   }
          	   else {
          		   mMovie4.setBackgroundColor(Color.RED);
          	   }
	           break;
       }
	 }
}
