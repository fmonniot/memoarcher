package android.support.v4.view;

import android.database.DataSetObserver;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public abstract class FragmentPagerAdapterCompat extends FragmentPagerAdapter {
	public FragmentPagerAdapterCompat(FragmentManager fm) { super(fm); }
	
	
	public static void setDataSetObserver(PagerAdapter adapter, DataSetObserver observer) {
		adapter.registerDataSetObserver(observer);
	}


}
