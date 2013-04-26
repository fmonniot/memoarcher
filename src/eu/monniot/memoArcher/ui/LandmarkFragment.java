package eu.monniot.memoArcher.ui;

import eu.monniot.memoArcher.Bow;
import eu.monniot.memoArcher.Landmark;
import eu.monniot.memoArcher.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the
 * ListView with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class LandmarkFragment extends Fragment implements OnDataChanged {

	@SuppressWarnings("unused")
	private OnFragmentInteractionListener mListener;

	/**
	 * The fragment's ListView/GridView.
	 */
	private AbsListView mListView;

	/**
	 * The Adapter which will be used to populate the ListView/GridView with
	 * Views.
	 */
	private ListAdapter mAdapter;
	
	private boolean mDataHasChanged = false;

	
	public static LandmarkFragment newInstance() {
		LandmarkFragment fragment = new LandmarkFragment();
		
		return fragment;
	}

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public LandmarkFragment() {
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}

		mAdapter = new LandmarkAdapter((MainActivity) getActivity());
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		inflater.inflate(R.menu.fragment_landmark, menu);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
		// TODO: Change Adapter to display your content
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_landmark, container,
				false);
		
		if(mDataHasChanged) {
			forceDataRefresh();
		}

		// Set the adapter
		mListView = (AbsListView) view.findViewById(android.R.id.list);
		((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

		return view;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
		mAdapter = null;
	}

	/**
	 * The default content for this Fragment has a TextView that is shown when
	 * the list is empty. If you would like to change the text, call this method
	 * to supply the text it should use.
	 */
	public void setEmptyText(CharSequence emptyText) {
		View emptyView = mListView.getEmptyView();

		if (emptyText instanceof TextView) {
			((TextView) emptyView).setText(emptyText);
		}
	}

	@Override
	public void dataHaveChanged() {
		mDataHasChanged = true;
	}
	
	@Override
	public void forceDataRefresh() {
		if(getActivity() == null)
			return;
		
		mAdapter = new LandmarkAdapter((MainActivity) getActivity());
		mDataHasChanged = false;
	}

	private class LandmarkAdapter extends BaseAdapter {
		LayoutInflater mInflater;
		Bow mBow;

		public LandmarkAdapter(MainActivity activity) {
			mInflater = LayoutInflater.from(activity);
			mBow = activity.getBow();
			Log.d(getTag(), "LandmarkAdapter has been created with bow "+mBow.toString());
		}
		
		@Override
		public int getCount() {
			return mBow.landmarksCount();
		}

		@Override
		public Landmark getItem(int position) {
			return mBow.landmark(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LandmarkViewHolder holder;
			if(convertView == null) {
				holder = new LandmarkViewHolder();
				convertView = mInflater.inflate(R.layout.landmark_item, null);
				
				holder.markValue = (EditText) convertView.findViewById(R.id.markValue);
				holder.distanceValue = (EditText) convertView.findViewById(R.id.distanceValue);
				holder.markUnit = (TextView) convertView.findViewById(R.id.markUnit);
				holder.distanceUnit = (TextView) convertView.findViewById(R.id.distanceUnit);
				
				convertView.setTag(holder);
			} else {
				holder = (LandmarkViewHolder) convertView.getTag();
			}
			
			Landmark landmark = getItem(position);
			holder.markValue.setText(String.valueOf(landmark.mark));
			holder.markUnit.setText(mBow.markUnit);
			holder.distanceValue.setText(String.valueOf(landmark.distance));
			holder.distanceUnit.setText(mBow.distanceUnit);
			
			return convertView;
		}
		
		public class LandmarkViewHolder {
			EditText markValue;
			TextView markUnit;
			EditText distanceValue;
			TextView distanceUnit;
		}
		
	}

}
