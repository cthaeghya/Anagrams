package com.cthaeghya.anagrams;

import android.annotation.SuppressLint;

public class Word implements Comparable<Word>{

	public String word;
	public int rank;
	public int[] letters;
	
	
	// Get the word, and create the array holding the count of letters
	@SuppressLint("DefaultLocale")
	public Word (String word) {
		this.word = word.toLowerCase();
		letters = new int[27];
		for (int i = 0; i < 27; i++) {
			letters[i] = 0;
		}
		for (int i = 0; i < word.length(); i++) {
			char c = Character.toLowerCase(word.charAt(i));
			if (c >= 'a' && c <= 'z') {
				letters['z' - c] += 1;
			} else {
				letters[26] += 1;
			}
		}
	}
	
	// What color the word should be, based on rank
	public int word_color () {
		if (rank % 3 == 0 && rank % 5 == 0)
			return 0;
		if (rank % 3 == 0)
			return 1;
		if (rank % 5 == 0)
			return 2;
		return 3;
	}
	
	// Whether two words are anagrams of each other
	public boolean equal_anagrams (Word other_word) {
		for (int i = 0; i < 27; i++) {
			if (letters[i] != other_word.letters[i])
				return false;
		}
		return true;
	}

	@Override
	// Compares two words based on basic char at a time comparison
	public int compareTo(Word another) {
		for (int i = 0; i < word.length(); i++) {
			if (another.word.length() <= i)
				return 1;
			if (word.charAt(i) > another.word.charAt(i))
				return 1;
			if (word.charAt(i) < another.word.charAt(i))
				return -1;
		}
		if (another.word.length() > word.length())
			return -1;
		return 0;
	}
	
}
