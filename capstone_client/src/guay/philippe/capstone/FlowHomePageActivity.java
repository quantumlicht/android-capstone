package guay.philippe.capstone;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.FragmentManager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ActionBar.Tab;
//import android.app.ListFragment;
//import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import guay.philippe.capstone.adapter.AppSectionsPagerAdapter;
import guay.philippe.capstone.data.Quiz;
import guay.philippe.capstone.fragments.CompletedQuizFragment;
import guay.philippe.capstone.fragments.CreatedQuizFragment;
import guay.philippe.capstone.fragments.NewQuizFragment;

public class FlowHomePageActivity extends FragmentActivity implements
		ActionBar.TabListener {
	private static ArrayList<Quiz> mArray_list = new ArrayList<Quiz>();
	
	private ViewPager mViewPager;
	private AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	private ActionBar mActionBar;
	private BroadcastReceiver mMessageReceiver;
	private TextView mPlayerName;
	private TextView mPlayerScore;
	private Button mButtonSync;
	private int score;
	private String username;
	
	private CompletedQuizFragment completedQuizFrag = CompletedQuizFragment.newInstance();
	private NewQuizFragment newQuizFrag = NewQuizFragment.newInstance();
	private CreatedQuizFragment createdQuizFrag = CreatedQuizFragment.newInstance();
		
	private String[] tabNames = {"New Quizzes", "Completed Quizzes",
			"Created Quizzes"};
	
	static final int NUM_ITEMS = 3;
	//PUBLIC
	//--------------------------------------------------------------------
	
	//OVERRIDES
	//--------------------------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);

		mPlayerName = (TextView) findViewById(R.id.player_name);
		mPlayerScore = (TextView) findViewById(R.id.player_score);
		mActionBar = getActionBar();
		mActionBar.setHomeButtonEnabled(false);
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mButtonSync = (Button) findViewById(R.id.sync);
		
		final List<ListFragment> fragments = getFragments();
		
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager(), fragments, NUM_ITEMS);
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		
		SharedPreferences prefs = Utils.getStorage(getApplicationContext());
		username = prefs.getString("username", "");
		score = prefs.getInt("score", 0);
		SetPlayerInfo(username, score);
		
		mMessageReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent){
				username = intent.getStringExtra("username");
				score = intent.getIntExtra("score", 0);
				Log.d("MUTIBO", "FlowHomePageActivity::MessageReceiver username: " + username + " score: " + score);
				SetPlayerInfo(username, score);
			}
		};
		
		mButtonSync.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				sync();
			}
		});
		
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				mActionBar.setSelectedNavigationItem(position);
			}
		});

		// Create a tab listener that is called when the user changes tabs.
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
				mViewPager.setCurrentItem(tab.getPosition());
			}
			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			}

			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
			}
		};

		// Add 3 tabs, specifying the tab's text and TabListener
		for (int i = 0; i < 3; i++) {
			mActionBar.addTab(mActionBar.newTab().setText(tabNames[i])
					.setTabListener(tabListener));
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.rating_group) {
			logout();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}
	
	@Override
	public void onResume() {
	  super.onResume();
	  // Register mMessageReceiver to receive messages.
	  Log.d("MUTIBO", "FlowHomePageActivity::onResume registerLocalBroadCastReceiver to event 'update-player'");
	  LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
	      new IntentFilter("update-player"));
	}
	
	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}
		
	// PRIVATE
	//--------------------------------------------------------------------
	
	private void sync() {
		completedQuizFrag.refresh();
		newQuizFrag.refresh();
		createdQuizFrag.refresh();
	}
	
	private void logout(){
		Log.d("MUTIBO", "FlowHomePageActivity::logout clearing shared Prefs");
		Utils.clearStorageAndCache(getApplicationContext());
		Intent intent = new Intent(FlowHomePageActivity.this, MainActivity.class);
		startActivity(intent);
	}
	
	private List<ListFragment> getFragments() {
		Log.d("MUTIBO", "HomePageActivity::getFragments Getting Fragment List");
		List<ListFragment> fList = new ArrayList<ListFragment>();

		fList.add(newQuizFrag);
		fList.add(completedQuizFrag);
		fList.add(createdQuizFrag);
		Log.d("MUTIBO", "HomePageActivity::returning Fragment List");
		return fList;
	}
	
	private void SetPlayerInfo(String username, int score) {
		mPlayerName.setText(username);
		mPlayerScore.setText(Integer.toString(score));
	}
}
