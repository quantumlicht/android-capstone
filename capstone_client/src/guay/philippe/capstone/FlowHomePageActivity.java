package guay.philippe.capstone;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Data.Quiz;
import android.app.FragmentManager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ActionBar.Tab;
//import android.app.ListFragment;
//import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import fragments.CompletedQuizFragment;
import fragments.CreatedQuizFragment;
import fragments.NewQuizFragment;

public class FlowHomePageActivity extends FragmentActivity implements
		ActionBar.TabListener {
	private static ArrayList<Quiz> mArray_list = new ArrayList<Quiz>();
	private ViewPager mViewPager;
	private static Bundle mNewQuizArgs = new Bundle();
	private AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	private ActionBar mActionBar;

	private String[] tabNames = { "New Quizzes", "Completed Quizzes",
			"Created Quizzes" };
	static final int NUM_ITEMS = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);

		mActionBar = getActionBar();
		mActionBar.setHomeButtonEnabled(false);
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		List<ListFragment> fragments = getFragments();
		Log.d("MUTIBO",
				"HomePageActivity::onCreate Instantiating appSectionsAdapter");
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager(), fragments);
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		Log.d("MUTIBO", "HomePageActivity::onCreate appSectionsAdapter is set");

		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						// When swiping between different app sections, select
						// the corresponding tab.
						// We can also use ActionBar.Tab#select() to do this if
						// we have a reference to the
						// Tab.
						mActionBar.setSelectedNavigationItem(position);
					}
				});

		// Create a tab listener that is called when the user changes tabs.
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
				mViewPager.setCurrentItem(tab.getPosition());
			}

			public void onTabUnselected(ActionBar.Tab tab,
					FragmentTransaction ft) {
				// hide the given tab
			}

			public void onTabReselected(ActionBar.Tab tab,
					FragmentTransaction ft) {
				// probably ignore this event
			}
		};

		// Add 3 tabs, specifying the tab's text and TabListener
		for (int i = 0; i < 3; i++) {
			mActionBar.addTab(mActionBar.newTab().setText(tabNames[i])
					.setTabListener(tabListener));
		}
	}

	private List<ListFragment> getFragments() {
		Log.d("MUTIBO", "HomePageActivity::getFragments Getting Fragment List");
		List<ListFragment> fList = new ArrayList<ListFragment>();

		fList.add(NewQuizFragment.newInstance("Fragment 1"));
		fList.add(CompletedQuizFragment.newInstance("Fragment 2"));
		fList.add(CreatedQuizFragment.newInstance("Fragment 3"));
		Log.d("MUTIBO", "HomePageActivity::returning Fragment List");
		return fList;
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
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
		// private final Context mContext;
		// private final ActionBar mActionBar;
		// private final ViewPager mViewPager;
		private List<ListFragment> fragments;

		public AppSectionsPagerAdapter(
				android.support.v4.app.FragmentManager fm,
				List<ListFragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int position) {
			return this.fragments.get(position);
		}

		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "Section " + (position + 1);
		}
	}

	public static class ArrayListFragment extends
			android.support.v4.app.ListFragment {
		int mNum;

		static ArrayListFragment newInstance(int num) {
			Log.d("MUTIBO", "ArrayListFragment::newInstance");
			ArrayListFragment f = new ArrayListFragment();
			// Supply num input as an argument.
			Bundle args = new Bundle();
			args.putInt("num", num);
			f.setArguments(args);

			return f;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			Log.d("MUTIBO", "ArrayListFragment::onCreate");
			super.onCreate(savedInstanceState);
			mNum = getArguments() != null ? getArguments().getInt("num") : 1;
		}

		/**
		 * The Fragment's UI is just a simple text view showing its instance
		 * number.
		 */
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			Log.d("MUTIBO", "ArrayListFragment::onCreateView");
			View v = inflater.inflate(R.layout.fragment_new_quiz, container,
					false);
			View tv = v.findViewById(R.id.username);
			((TextView) tv).setText("Fragment #" + mNum);
			Log.d("MUTIBO", "ArrayListFragment::onCreateView returning view");
			return v;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			Log.d("MUTIBO", "ArrayListFragment::onActivityCreated");
			setListAdapter(new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1));
			Log.d("MUTIBO",
					"ArrayListFragment::onActivityCreated setListAdapter Called");
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			Log.i("FragmentList", "Item clicked: " + id);
		}
	}
	// @Override
	// protected void onSaveInstanceState(Bundle outState) {
	// super.onSaveInstanceState(outState);
	// outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
	// }
}
