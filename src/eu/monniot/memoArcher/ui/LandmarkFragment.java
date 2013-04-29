package eu.monniot.memoArcher.ui;

import java.util.List;

import eu.monniot.memoArcher.Bow;
import eu.monniot.memoArcher.Landmark;
import eu.monniot.memoArcher.R;
import eu.monniot.memoArcher.loaders.LandmarksLoader;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the
 * ListView with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class LandmarkFragment extends Fragment implements
		OnDataChanged, LoaderCallbacks<List<Landmark>> {

	private static final int LANDMARKS_LOADER = 1;
	
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
	private ArrayAdapter<Landmark> mAdapter;
	
	private boolean mDataHasChanged = false;

	private Bow mBow;

	
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
		// TODO Change below to use an interface !
		try {
			mBow = ((MainActivity) activity).getBow();
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must be MainActivity");
		}
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
		// TODO: Change Adapter to display your content

		getLoaderManager().initLoader(LANDMARKS_LOADER, null, this);

		forceDataRefresh();
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_add_landmark:
	        	Toast.makeText(getActivity(), "Creating new landmark", Toast.LENGTH_SHORT).show();
	        	Landmark lm = new Landmark();
	        	lm.bow = mBow;
	        	lm.save();
	        	forceDataRefresh();
	        	
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_landmark, menu);
	}
	
	@Override
	public Loader<List<Landmark>> onCreateLoader(int id, Bundle args) {
	    switch (id) {
	        case LANDMARKS_LOADER:
	            return new LandmarksLoader(getActivity(), mBow);
	        default:
	            return null; // An invalid id was passed in
	    }

	}

	@Override
	public void onLoadFinished(Loader<List<Landmark>> loader, List<Landmark> data) {
		mAdapter.clear();
		mAdapter.addAll(data);
	}

	@Override
	public void onLoaderReset(Loader<List<Landmark>> loader) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dataHaveChanged() {
		mDataHasChanged = true;
	}
	
	@Override
	public void forceDataRefresh() {
		if(getActivity() == null)
			return;
		
		mAdapter = new LandmarksAdapter(getActivity(), R.layout.landmark_item);
		mDataHasChanged = false;
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
	
	private class LandmarksAdapter extends ArrayAdapter<Landmark> {

		LayoutInflater mInflater;
		
		public LandmarksAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
			mInflater = LayoutInflater.from(context);
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			Log.d(getTag(), "Call getView(int "+String.valueOf(position)+", View "+convertView);
			
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
			holder.markUnit.setText(landmark.bow.markUnit);
			holder.distanceValue.setText(String.valueOf(landmark.distance));
			holder.distanceUnit.setText(landmark.bow.distanceUnit);
			
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
