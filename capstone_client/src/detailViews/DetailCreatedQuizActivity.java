package detailViews;

import guay.philippe.capstone.R;
import guay.philippe.capstone.R.id;
import guay.philippe.capstone.R.layout;
import guay.philippe.capstone.R.menu;
import Data.Quiz;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailCreatedQuizActivity extends Activity {
	private Quiz quiz;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_new_quiz);
		
		Bundle bundle = getIntent().getExtras();
		quiz = bundle.getParcelable("quiz");
		TextView tv = (TextView) findViewById(R.id.intro_text);
		tv.setText(quiz.getName());
	}
}
