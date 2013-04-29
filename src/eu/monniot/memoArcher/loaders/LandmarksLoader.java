package eu.monniot.memoArcher.loaders;

import java.util.List;

import eu.monniot.memoArcher.Bow;
import eu.monniot.memoArcher.Landmark;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class LandmarksLoader extends AsyncTaskLoader<List<Landmark>>{
	  // We hold a reference to the Loader’s data here.
	  private List<Landmark> mData;
	  private Bow mBow;
	 
	  public LandmarksLoader(Context ctx, Bow bow) {
	    // Loaders may be used across multiple Activitys (assuming they aren't
	    // bound to the LoaderManager), so NEVER hold a reference to the context
	    // directly. Doing so will cause you to leak an entire Activity's context.
	    // The superclass constructor will store a reference to the Application
	    // Context instead, and can be retrieved with a call to getContext().
	    super(ctx);
	    mBow = bow;
	  }
	 
	  /****************************************************/
	  /** (1) A task that performs the asynchronous load **/
	  /****************************************************/
	 
	  @Override
	  public List<Landmark> loadInBackground() {
	    // This method is called on a background thread and should generate a
	    // new set of data to be delivered back to the client.
		  return mBow.landmarks();
	  }
	 
	  /********************************************************/
	  /** (2) Deliver the results to the registered listener **/
	  /********************************************************/
	 
	  @Override
	  public void deliverResult(List<Landmark> data) {
	    if (isReset()) {
	      // The Loader has been reset; ignore the result and invalidate the data.
	      releaseResources(data);
	      return;
	    }
	 
	    // Hold a reference to the old data so it doesn't get garbage collected.
	    // We must protect it until the new data has been delivered.
	    List<Landmark> oldData = mData;
	    mData = data;
	 
	    if (isStarted()) {
	      // If the Loader is in a started state, deliver the results to the
	      // client. The superclass method does this for us.
	      super.deliverResult(data);
	    }
	 
	    // Invalidate the old data as we don't need it any more.
	    if (oldData != null && oldData != data) {
	      releaseResources(oldData);
	    }
	  }
	 
	  /*********************************************************/
	  /** (3) Implement the Loader’s state-dependent behavior **/
	  /*********************************************************/
	 
	  @Override
	  protected void onStartLoading() {
	    if (mData != null) {
	      // Deliver any previously loaded data immediately.
	      deliverResult(mData);
	    }
	 
	    if (takeContentChanged() || mData == null) {
	      // When the observer detects a change, it should call onContentChanged()
	      // on the Loader, which will cause the next call to takeContentChanged()
	      // to return true. If this is ever the case (or if the current data is
	      // null), we force a new load.
	      forceLoad();
	    }
	  }
	 
	  @Override
	  protected void onStopLoading() {
	    // The Loader is in a stopped state, so we should attempt to cancel the
	    // current load (if there is one).
	    cancelLoad();
	 
	    // Note that we leave the observer as is. Loaders in a stopped state
	    // should still monitor the data source for changes so that the Loader
	    // will know to force a new load if it is ever started again.
	  }
	 
	  @Override
	  protected void onReset() {
	    // Ensure the loader has been stopped.
	    onStopLoading();
	 
	    // At this point we can release the resources associated with 'mData'.
	    if (mData != null) {
	      releaseResources(mData);
	      mData = null;
	    }
	  }
	 
	  @Override
	  public void onCanceled(List<Landmark> data) {
	    // Attempt to cancel the current asynchronous load.
	    super.onCanceled(data);
	 
	    // The load has been canceled, so we should release the resources
	    // associated with 'data'.
	    releaseResources(data);
	  }
	 
	  private void releaseResources(List<Landmark> data) {
	    // For a simple List, there is nothing to do. For something like a Cursor, we
	    // would close it in this method. All resources associated with the Loader
	    // should be released here.
	  }
	}