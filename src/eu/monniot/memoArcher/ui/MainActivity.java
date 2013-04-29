package eu.monniot.memoArcher.ui;

import java.util.Locale;

import eu.monniot.memoArcher.Bow;
import eu.monniot.memoArcher.R;
import eu.monniot.memoArcher.loaders.BowsCursorLoader;
import eu.monniot.memoArcher.ui.EditBowDialogFragment.OnDialogResultListener;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends FragmentActivity implements 
		OnFragmentInteractionListener, OnDialogResultListener, LoaderCallbacks<Cursor> {

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

	private SharedPreferences mPreferences;
	
	private OnNavigationListener mOnNavigationListener;
	
	private SimpleCursorAdapter mNavigationAdapter;
	
	private static final int LOADER_ACTIONBAR = 2;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		
		/*
		 * Define the navigation 
		 */
		mNavigationAdapter = new SimpleCursorAdapter(getApplication(), R.layout.simple_spinner_dropdown_item,
				null, new String[]{"name"}, new int[] {android.R.id.text1}, 0);
		getSupportLoaderManager().initLoader(LOADER_ACTIONBAR, null, this);
		
		mOnNavigationListener = new OnNavigationListener() {
			@Override
			public boolean onNavigationItemSelected(int position, long itemId) {
				mBow = Bow.loadFromCursor((Cursor) mNavigationAdapter.getItem(position));
				mViewPager.getAdapter().notifyDataSetChanged();
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
		
		Long id = Long.parseLong(mPreferences.getString("pref_default_bow", "1"));
		mBow = Bow.load(Bow.class, id);
		if(mBow == null) {
			startActivity(new Intent(this, FirstActivity.class));
		}
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		mViewPager.setOnPageChangeListener(mSectionsPagerAdapter);
		
		//getActionBar().setSelectedNavigationItem(((ArrayAdapter<Bow>) mNavigationAdapter).getPosition(mBow));
		
		setTitle(getBow().name);
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

		Bow bow = new Bow(	name.toString(),
							markUnit.toString(),
							distanceUnit.toString()
						);
		bow.save();
		
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
	    DialogFragment newFragment = EditBowDialogFragment.newInstance();
	    newFragment.show(ft, "dialog");
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	private class SectionsPagerAdapter extends FragmentPagerAdapter implements
			ViewPager.OnPageChangeListener {
		
		private boolean[] mDataSetHasChanged = new boolean[]{false,false,false};
		
		private int mCurrentlyViewedPage = 0;
		
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			
		}
		
		@Override
		public Fragment getItem(int position) {
			Fragment f = new Fragment();
			switch (position) {
			case 2:
				f = BowPreferenceFragment.newInstance();
				break;
			case 1:
				f = NoteFragment.newInstance();
				break;
			case 0:
				f = LandmarkFragment.newInstance();
				break;
			}
			return f;
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
		
		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			
			Log.v("SectionsPagerAdapter", "call notifyDataSetChanged(); currentPage="+String.valueOf(mCurrentlyViewedPage));
			mDataSetHasChanged = new boolean[]{true, true, true};
			
			((OnDataChanged)getItem(mCurrentlyViewedPage)).forceDataRefresh();
		}
		
		@Override
	    public int getItemPosition(Object item) {
			Fragment f = (Fragment) item;

    		Log.d("SectionsPagerAdapter", "getItemPosition("+f.getClass().toString()+")");
	        return POSITION_NONE;
	        
	    }

	    @Override
	    public Object instantiateItem(ViewGroup container, int position) {
    		Log.d("SectionsPagerAdapter", "instantiateView("+String.valueOf(position)+")");
	        Object o = super.instantiateItem(container, position);
	        
	    	if(mDataSetHasChanged[position]) {
	    		mDataSetHasChanged[position] = false;
		        ((OnDataChanged) o).dataHaveChanged();
	    	}
	    	return o;
	    }

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {}

		@Override
		public void onPageSelected(int position) {
			mCurrentlyViewedPage = position;
		}

		@Override
		public void onPageScrollStateChanged(int state) {}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
	    switch (id) {
        case LOADER_ACTIONBAR:
            return new BowsCursorLoader(this);
        default:
            return null; // An invalid id was passed in
    }
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		mNavigationAdapter.changeCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mNavigationAdapter.swapCursor(null);
		
	}

	

}
