package eu.monniot.memoArcher.ui;

import eu.monniot.memoArcher.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class EditBowDialogFragment extends DialogFragment {
	
	private AlertDialog mAlertDialog;
	
	private boolean mMandatory = false; 

	static EditBowDialogFragment newInstance() {
		EditBowDialogFragment f = new EditBowDialogFragment();
		
		return f;
	}

	public void setMandatory(boolean value) {
		mMandatory = value;
		setCancelable(!value);
	}
	
	public boolean isMandatory() {
		return mMandatory;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	// If not created by the {@link AlertDialog.Builder}, we have to create the view (used in a fragment mode)
		if(!getShowsDialog()) {
			View view = inflater.inflate(R.layout.dialog_add_bow, container, false);
			return view;
		}

		return null;
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_add_bow, null, false))
               .setTitle(R.string.title_fragment_add_bow)
               .setPositiveButton(getString(android.R.string.ok), null);  

        if(isCancelable() && !isMandatory()) {
        
        	builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        }
        
        return mAlertDialog = builder.create();
    }
    
	@Override
	public void onStart() {
		super.onStart();
		
		mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
						.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Editable name = ((EditText) (mAlertDialog.findViewById(R.id.name))).getText();
				
				if(name.length() == 0) {
					// TODO Should display an alert inside the dialog
					Log.w(getTag(), "Empty bow name");
					return;
				}
				
				Editable markUnit = ((EditText) (mAlertDialog.findViewById(R.id.markUnit))).getText();
				Editable distanceUnit = ((EditText) (mAlertDialog.findViewById(R.id.distanceUnit))).getText();
				
				((OnDialogResultListener) getActivity()).onOkDialogClose(name, markUnit, distanceUnit);
				
				mAlertDialog.dismiss();
				
			}
		});
		
		if(isMandatory()) {
			mAlertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
				
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if(keyCode == KeyEvent.KEYCODE_BACK) {
						getActivity().finish();
					}
					return false;
				}
			});
		}
	}


    public interface OnDialogResultListener{
    	public void onOkDialogClose(Editable name, Editable markUnit, Editable distanceUnit);
    }
}
