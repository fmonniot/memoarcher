package eu.monniot.memoArcher.ui;

import java.util.Locale;

import eu.monniot.memoArcher.Bow;
import eu.monniot.memoArcher.BowManager;
import eu.monniot.memoArcher.R;
import eu.monniot.memoArcher.ui.AddBowDialogFragment.OnDialogResultListener;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

public class MainActivity extends FragmentActivity implements 
		OnFragmentInteractionListener, OnDialogResultListener {

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
	 * The current bow
	 */
	private Bow mBow;
	
	private BowManager mBowManager;

	private SharedPreferences mPreferences;
	
	private OnNavigationListener mOnNavigationListener;
	
	private SpinnerAdapter mNavigationAdapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mBowManager = new BowManager(getApplication());
		mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		
		/*
		 * Define the navigation 
		 */
		mNavigationAdapter = new ArrayAdapter<Bow>(
				this, 
				R.layout.simple_spinner_dropdown_item, 
				mBowManager.getAllBow());
		
		mOnNavigationListener = new OnNavigationListener() {

			  @Override
			  public boolean onNavigationItemSelected(int position, long itemId) {
				  
				mSectionsPagerAdapter.notifyDataSetChanged();
			    return true;
			  }
			};
			
			ActionBar actionBar = getActionBar();
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			actionBar.setListNavigationCallbacks(mNavigationAdapter, mOnNavigationListener);
			actionBar.setDisplayShowTitleEnabled(false);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		/**
		 * 	Retrieve the default bow. If none, create one (it should not be possible
		 *  since {@link FirstActivity} is launched before {@link MainActivity})
		 */
		mBow = mBowManager.findOneById(Long.parseLong(mPreferences.getString("pref_default_bow", "1")));
		if(mBow == null) {
			startActivity(new Intent(this, FirstActivity.class));
		}
		
		setTitle(getBow().getName());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_add_bow:
	            showAddBowDialog();
	            return true;
	        case R.id.action_settings:
	    		startActivity(new Intent(this, SettingsActivity.class));
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public Bow getBow() {
		return mBow;
	}

	@Override
	public void onFragmentInteraction(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOkDialogClose(Editable name, Editable markUnit,
			Editable distanceUnit) {

		Bow bow = mBowManager.createBow(
								name.toString(),
								markUnit.toString(),
								distanceUnit.toString()
							);
		mBowManager.save(bow);
		
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
				return BowPreferenceFragment.newInstance(getBow());
			case 1:
				return NoteFragment.newInstance(getBow());
			case 0:
			default:
				return LandmarkFragment.newInstance();
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

	
	@SuppressLint("CommitTransaction")
	private void showAddBowDialog() {
	    

	    // DialogFragment.show() will take care of adding the fragment
	    // in a transaction.  We also want to remove any currently showing
	    // dialog, so make our own transaction and take care of that here.
	    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	    Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
	    if (prev != null) {
	        ft.remove(prev);
	    }
	    ft.addToBackStack(null);

	    // Create and show the dialog.
	    DialogFragment newFragment = AddBowDialogFragment.newInstance();
	    newFragment.show(ft, "dialog");
	}

}
