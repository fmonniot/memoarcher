package eu.monniot.memoArcher.ui;

import eu.monniot.memoArcher.Bow;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BowPreferenceFragment extends Fragment {

	private static final String ARG_BOW_ID = "bow_id";

	@SuppressWarnings("unused")
	private Bow mBow;

	public static BowPreferenceFragment newInstance(int bowId) {
		BowPreferenceFragment fragment = new BowPreferenceFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_BOW_ID, bowId);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null) {
			mBow = Bow.instanciateFromId(getArguments().getInt(ARG_BOW_ID));
		}
	}
}
