package com.cthaeghya.anagrams;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

public class Anagrams extends Activity {
	
	public final static String EXTRA_VALUE = "com.cthaeghya.File_explorer.VALUE";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Select which word list to use
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setCancelable(true);
	    builder.setTitle("Pick Word List");
	    builder.setInverseBackgroundForced(true);
	    
	    // Option 1 - default included word list
	    builder.setPositiveButton("Use default",
	            new DialogInterface.OnClickListener() {
	                @Override
	                public void onClick(DialogInterface dialog,
	                        int which) {
	                    dialog.dismiss();
	                    
	                    // Pass the pointer to the included word list
	                	Intent intent = new Intent(getBaseContext(), Anagrams_Actual.class);
	                	intent.putExtra(EXTRA_VALUE,"");
	                	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                    getBaseContext().startActivity(intent);
	                }
	            });
	    
	    // Option 2 - select another file on the phoen
	    builder.setNegativeButton("Select File",
	            new DialogInterface.OnClickListener() {
	                @Override
	                public void onClick(DialogInterface dialog,
	                        int which) {
	                    dialog.dismiss();
	                    
	                    // Use the FileDisplay activity to select the desired file
	                    Intent intent = new Intent(getBaseContext(), FileDisplay.class);
	                	intent.putExtra(EXTRA_VALUE, Environment.getExternalStorageDirectory().getAbsolutePath());
	                	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                	getBaseContext().startActivity(intent);
	                }
	            });
	    AlertDialog alert = builder.create();
	    alert.show();
	    
	}
	

}
