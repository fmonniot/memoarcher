package eu.monniot.memoArcher;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class NoteFragment extends Fragment {

	private static final String ARG_BOW_ID = "bow_id";

	@SuppressWarnings("unused")
	private Bow mBow;

	public static NoteFragment newInstance(int bowId) {
		NoteFragment fragment = new NoteFragment();
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
