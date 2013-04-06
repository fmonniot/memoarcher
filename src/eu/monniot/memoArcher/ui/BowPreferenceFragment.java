package eu.monniot.memoArcher.ui;

import eu.monniot.memoArcher.Bow;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BowPreferenceFragment extends Fragment {
	
	@SuppressWarnings("unused")
	private Bow mBow;

	public static BowPreferenceFragment newInstance(Bow bow) {
		BowPreferenceFragment fragment = new BowPreferenceFragment();
		fragment.setBow(bow);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	private void setBow(Bow bow) {
		mBow = bow;
	}
}
