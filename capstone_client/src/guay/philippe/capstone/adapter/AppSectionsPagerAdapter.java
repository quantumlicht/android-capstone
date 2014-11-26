package guay.philippe.capstone.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;

public class AppSectionsPagerAdapter extends FragmentPagerAdapter {
	private List<ListFragment> fragments;
	private int count;

	public AppSectionsPagerAdapter(
			android.support.v4.app.FragmentManager fm,
			List<ListFragment> fragments, int count) {
		super(fm);
		this.fragments = fragments;
		this.count = count;
	}

	@Override
	public Fragment getItem(int position) {
		return this.fragments.get(position);
	}

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return "Section " + (position + 1);
	}
}