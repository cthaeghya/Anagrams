package com.cthaeghya.anagrams;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class Anagrams_Actual extends Activity {

	// Word list is the fixed word list
	private List<Word> word_list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anagrams);
		
		// Get the location of the passed word list file
		Intent intent = getIntent();
		String file_name = intent.getStringExtra(Anagrams.EXTRA_VALUE);
		
		// Create a dummy list for if the word list isn't parsable
	    word_list = create_dummy_word_list();

	    // If passed a file on the phone
		if (file_name.length() > 0) {
			try {
				InputStream in = new FileInputStream(file_name);
				word_list = create_word_list(in);
			} catch (IOException e) {
			}
			
			// Otherwise, load the default file
		} else {
			try {
				InputStream in = getResources().getAssets().open("word_list.txt");
				word_list = create_word_list(in);
			} catch (IOException e) {
			}
		}
		
		// Display the list using the modified adaptor to account for color
	    ListView profile = (ListView) findViewById(R.id.word_list);
	    NewListAdapter adapter = new NewListAdapter(getBaseContext(), word_list);
	    profile.setAdapter(adapter);
	    
	    // Set the search button
	    set_button();
	    
	}
	
	
	// Set the search button
	private void set_button () {
		Button button = (Button) findViewById(R.id.search_button);
		button.setOnClickListener(new OnClickListener () {
			@Override
			public void onClick(View v) {
				
				// Grab the currently entered search term
				EditText text = (EditText) findViewById(R.id.search_term);
				String search_term = text.getText().toString();
				
				// Find the first word that is equal or higher lexigraphically to the search term
				Word search_word = new Word(search_term);
				int start = 0;
				while (start < word_list.size() && word_list.get(start).compareTo(search_word) < 0) {
					start++;
				}
				
				// From that point, continue until the words no longer include the search term
				int end = start;
				while (end < word_list.size() && word_list.get(end).word.startsWith(search_term)) {
					end++;
				}
				
				// Display the sublist of the main list with those endpoints
				List<Word> list = word_list.subList(start, end);
				NewListAdapter adapter = new NewListAdapter(getBaseContext(), list);
				ListView profile = (ListView) findViewById(R.id.word_list);
				profile.setAdapter(adapter);
			}
		});
	}
	
	// Create a word list from a file
	private List<Word> create_word_list (InputStream in) throws IOException {
		List<Word> list = new ArrayList<Word>();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String next_word = br.readLine();
		
		// This assumes that words are one to a line, can be modified
		// for other input styles
		while (next_word != null) {
			list.add(new Word(next_word));
			next_word = br.readLine();
		}
		
		// Sort the resulting list, and give each word its' rank
		Collections.sort(list);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).rank = i+1;
		}
		return list;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.anagrams, menu);
		return true;
	}
	
	
	// Dummy list of words for when the passed list fails to parse
	// Also useful for debugging
	private List<Word> create_dummy_word_list () {
		List<Word> list = new ArrayList<Word>();
		list.add(new Word("apple"));
		list.add(new Word("kimchi"));
		list.add(new Word("banana"));
		list.add(new Word("carrot"));
		list.add(new Word("danish"));
		list.add(new Word("ginger"));
		list.add(new Word("eggplant"));
		list.add(new Word("fennel"));
		list.add(new Word("ham"));
		list.add(new Word("ice cream"));
		list.add(new Word("jerky"));
		list.add(new Word("lime"));
		list.add(new Word("melon"));
		list.add(new Word("noodle"));
		list.add(new Word("orange"));
		list.add(new Word("arroct"));
		Collections.sort(list);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).rank = i+1;
		}
		return list;
	}
	
	// Modified adapter for list display
	private class NewListAdapter extends ArrayAdapter<Word> {

	    List<Word> mIdMap = new ArrayList<Word>();
	    private final Context context;

	    public NewListAdapter(Context context,  List<Word> objects) {
	    	super(context, R.layout.listview_item, objects);
	    	this.context = context;
	    	mIdMap = objects;
	    }
	    
	    // Method for a single item in the list
	    @Override
	    public View getView (final int position, View convertView, ViewGroup parent) {
	    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    	View rowView = inflater.inflate(R.layout.listview_item, parent, false);
	    	TextView text = (TextView) rowView.findViewById(R.id.textView);
	    	Word word = mIdMap.get(position);
	    	
	    	// Text is the word and its rank
	    	text.setText(word.word + " : " + word.rank);
	    	
	    	// Color is determined by rank
	    	int color = word.word_color();
	    	if (color == 0) {
	    		rowView.setBackgroundColor(Color.MAGENTA);
	    	} else if (color == 1) {
	    		rowView.setBackgroundColor(Color.RED);
	    	} else if (color == 2) {
	    		rowView.setBackgroundColor(Color.CYAN);
	    	}
	    	rowView.setOnClickListener(new OnClickListener() {
				  @Override
				  public void onClick(View v) {
					  List<Word> list = new ArrayList<Word>();
					  
					  // Iterate through all words, finding those that are anagrams
					  for (int i = 0; i < word_list.size(); i++) {
						  if (word_list.get(i).equal_anagrams(mIdMap.get(position))) {
							  list.add(word_list.get(i));
						  }
					  }
					  
					  // Replace the list of all words with the list of all anagrams of the clicked word
					  ListView profile = (ListView) findViewById(R.id.word_list);
					  NewListAdapter adapter = new NewListAdapter(getBaseContext(), list);
					  profile.setAdapter(adapter);
				  }
			  });
	    	return rowView;
	    }

	    @Override
	    public long getItemId(int position) {
	 //   	String item = getItem(position);
	    	return position;
	    }

	    @Override
	    public boolean hasStableIds() {
	    	return true;
	    }

	}

	
	
}
