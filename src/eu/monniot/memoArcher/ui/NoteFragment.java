package eu.monniot.memoArcher.ui;

import eu.monniot.memoArcher.Bow;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NoteFragment extends Fragment implements OnDataChanged {

	public static NoteFragment newInstance() {
		NoteFragment fragment = new NoteFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(android.R.layout.simple_list_item_1, container, false);

		TextView tv = (TextView) view.findViewById(android.R.id.text1);
		Bow b = getBow();
		
		if(b != null)
			tv.setText(getBow().toString());
		
		return view;
	}

	@Override
	public void dataHaveChanged() {
		forceDataRefresh();
	}

	@Override
	public void forceDataRefresh() {
		if(getActivity() != null) {
			Log.d(getTag(), "NoteFragment has changed its bow to "+getBow().toString());
		}
	}

	private Bow getBow() {
		return ((MainActivity)getActivity()).getBow();
	}
}
