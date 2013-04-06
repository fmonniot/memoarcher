package eu.monniot.memoArcher.ui;

import java.util.Locale;

import eu.monniot.memoArcher.R;
import eu.monniot.memoArcher.R.id;
import eu.monniot.memoArcher.R.layout;
import eu.monniot.memoArcher.R.string;
import eu.monniot.memoArcher.ui.LandmarkFragment.OnFragmentInteractionListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity implements OnFragmentInteractionListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	
	/**
	 * The ID of the current bow
	 */
	private int mBowId = 1;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	@Override
	public void onFragmentInteraction(int id) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	private class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			switch (position) {
			case 2:
				return BowPreferenceFragment.newInstance(mBowId);
			case 1:
				return NoteFragment.newInstance(mBowId);
			case 0:
			default:
				return LandmarkFragment.newInstance(mBowId);
			}
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_fragment_landmark).toUpperCase(l);
			case 1:
				return getString(R.string.title_fragment_notes).toUpperCase(l);
			case 2:
				return getString(R.string.title_fragment_preferences).toUpperCase(l);
			}
			return null;
		}
	}


}
