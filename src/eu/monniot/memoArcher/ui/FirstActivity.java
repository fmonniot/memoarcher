package eu.monniot.memoArcher.ui;

import eu.monniot.memoArcher.Bow;
import eu.monniot.memoArcher.BowManager;
import eu.monniot.memoArcher.R;
import eu.monniot.memoArcher.ui.AddBowDialogFragment.OnDialogResultListener;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;

public class FirstActivity extends FragmentActivity implements OnDialogResultListener {

	BowManager mBowManager;
	Bow mBow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.title_activity_first);

		// Retrieve the default bow. If none, create one
		mBowManager = new BowManager(this);
		mBow = mBowManager.findOneById(1);
		if(mBow == null) {
			showAddBowDialog();
		} else {
			startActivity(new Intent(this, MainActivity.class));
		}
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
	    AddBowDialogFragment newFragment = AddBowDialogFragment.newInstance();
	    newFragment.setMandatory(true);
	    newFragment.show(ft, "dialog");
	}

	@Override
	public void onOkDialogClose(Editable name, Editable markUnit,
			Editable distanceUnit) {
		Bow bow = mBowManager.createBow(
								name.toString(),
								markUnit.toString(),
								distanceUnit.toString()
							);
		mBowManager.save(bow);
		
	}
	
}
