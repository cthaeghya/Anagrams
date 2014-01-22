package com.cthaeghya.anagrams;

import java.io.FileFilter;
import java.io.File;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/*
 * Allows the user to select a file from the directory
 * and then passes that file to the program
 */
public class FileDisplay extends ListActivity {
	
	// List of endings the viewed files can have
	public final static String[] endings = {"txt"};
	
	File f;
	File[] files;
	
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get the current directory to display
		Intent intent = getIntent();
		String file_name = intent.getStringExtra(Anagrams.EXTRA_VALUE);
		
		// Get a list of all files in the directory
		f = new File(file_name);
		files = f.listFiles(new displayFilter());
		
		// List all the files, adding ../ to go back a folder
		String[] list;
		if (files != null) {
			list = new String[files.length + 1];
			list[0] = "../";
			for (int i = 0; i < files.length; i++) {
				list[i+1] = files[i].getName();
			}
		} else {
			list = new String[1];
			list[0] = "../";
		}
		
		// Display the list of files / folders
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
		setListAdapter(adapter);
	}
	
	// When a file / folder is clicked
	public void onListItemClick (ListView l, View v, final int position, long id) {
		if (position < 1) {
			// If selected back
			File parent = f.getParentFile();
			if (parent != null) {
				makeNewList(parent);
			}
		} else if (files[position - 1].isDirectory()) {
			// If selected a directory
			makeNewList(files[position - 1]);
		} else {
			// If selected file
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setCancelable(true);
	        builder.setTitle("Are you sure you want to open this file?");
	        builder.setInverseBackgroundForced(true);
	        
	        // If confirmed
	        builder.setPositiveButton("Yes",
	                new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog,
	                            int which) {
	                        dialog.dismiss();
	                        // Pass that file to the main program
	                    	Intent intent = new Intent(getBaseContext(), Anagrams_Actual.class);
	                    	intent.putExtra(Anagrams.EXTRA_VALUE, files[position - 1].getAbsolutePath());
	                    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                        getBaseContext().startActivity(intent);
	                    }
	                });
	        
	        // If not confirmed
	        builder.setNegativeButton("No",
	                new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog,
	                            int which) {
	                        dialog.dismiss();
	                    }
	                });
	        AlertDialog alert = builder.create();
	        alert.show();
		}
	}
	
	// Create a new list, the files/folders in selected folder
	private void makeNewList (File f) {
		Intent intent = new Intent(this, FileDisplay.class);
		intent.putExtra(Anagrams.EXTRA_VALUE, f.getAbsolutePath());
		startActivity(intent);
	}
	
	// Only display folders and files with the appropriate endings
	public class displayFilter implements FileFilter {

		@Override
		public boolean accept(File file) {
			if (file.isDirectory())
				return true;
			String name = file.getName();
			int j = name.lastIndexOf('.');
			String end = name.substring(j+1);
			for (int i = 0; i < endings.length; i++) {
				if (end.equals(endings[i]))
					return true;
			}
			return false;
		}
		
	}
	
}
