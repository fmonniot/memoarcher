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

	private Bow mBow;

	public static NoteFragment newInstance(Bow bow) {
		NoteFragment fragment = new NoteFragment();
		fragment.setBow(bow);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(android.R.layout.simple_list_item_1, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		TextView tv = (TextView) view.findViewById(android.R.id.text1);
		tv.setText(mBow.toString());
	}
	
	private void setBow(Bow bow) {
		mBow = bow;
	}

	@Override
	public void dataHaveChanged() {
		forceDataRefresh();
	}

	@Override
	public void forceDataRefresh() {
		if(getActivity() != null) {
			mBow = ((MainActivity)getActivity()).getBow();
			Log.d(getTag(), "NoteFragment has changed its bow to "+mBow.toString());
		}
	}
}
