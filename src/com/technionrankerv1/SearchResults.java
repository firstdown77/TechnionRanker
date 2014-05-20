package com.technionrankerv1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.serverapi.TechnionRankerAPI;

public class SearchResults extends ActionBarActivity {
	TechnionRankerAPI db = new TechnionRankerAPI();
	//@override
	public void onCreate(Bundle savedInstance){

		super.onCreate(savedInstance);
		setContentView(R.layout.search_results);
		Log.d(getLocalClassName(), "In Searchable");
		Intent intent = getIntent();
		String query = null;
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			query = intent.getStringExtra(SearchManager.QUERY);
		}
		
		String[] professorsAndCourses = null;
		// Course c1 = new Course(new Long(1), null, null, null, null, false);
		// Course c = new TechnionRankerAPI().getCourse(c1);
		// Log.d(getLocalClassName(), c.toString());
		try {
			professorsAndCourses = concat(capsFix(parseCourses()), parseProfessors());
		} catch (Exception e) {
			e.printStackTrace();
		}
		SearchableAdapter adapt = new SearchableAdapter(query,SearchResults.this, professorsAndCourses);
		final ListView view = (ListView) findViewById(R.id.list);
		view.setAdapter(adapt);
		
		view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View v,
					int position, long arg3) {
				//Log.d(getLocalClassName(), v.getItemAtPosition(position).toString());
				//String value = view.getItemAtPosition(position).toString();
			    LinearLayout parent = (LinearLayout) v;
				TextView name = (TextView) parent.findViewById(R.id.lblListItem);
				String value = name.getText().toString();
				if (Character.isDigit(value.charAt(0))) {
					Log.d(getLocalClassName(), value);
					Log.d(getLocalClassName(), value.charAt(0)+"");
					String[] splitted = value.split(" - ");
					String courseNumber = splitted[0];
					String courseName = splitted[1];
					Intent i = new Intent(SearchResults.this, CourseView.class);
					i.putExtra("courseNumber", courseNumber);
					i.putExtra("courseName", courseName);
					startActivity(i);
				}
				else if (value.equals("No Results")) {
					return;
				}
				else {
					Intent i = new Intent(SearchResults.this, ProfessorView.class);
					i.putExtra("professorName", value);
					startActivity(i);
				}
				// assuming string and if you want to get the value on click of
				// list item
				// do what you intend to do on click of listview row
			}
		});
	}
	
	public String[] parseProfessors() throws Exception {
		ArrayList<String> profList = new ArrayList<String>();
		String inputLine = "";
		String[] temp;
		String[] profListArray = null;
		String[] professorFiles = getAssets().list("ProfessorListings");
		for (int i = 0; i < professorFiles.length; i++) {
			BufferedReader infile = new BufferedReader(new InputStreamReader(
					getAssets().open("ProfessorListings/" + professorFiles[i])));
			while (infile.ready()) {// while more info exists
				inputLine = infile.readLine();
				if (inputLine.startsWith("<td><a href=")) {
					/*
					 * This would parse the professor's ID #. int start =
					 * inputLine.indexOf("code=") + 5; int end =
					 * inputLine.indexOf(" rel") - 1; String id =
					 * inputLine.substring(start, end); Log.d(professorFiles[i],
					 * id);
					 */
					inputLine = inputLine.substring(1, inputLine.length() - 9);
					temp = inputLine.split(">");
					String[] splittedOnSpace = temp[2].split(" ");
					String firstNameLastName;
					if (splittedOnSpace.length == 2) {
						firstNameLastName = "" + splittedOnSpace[1] + " " + splittedOnSpace[0];
						//Log.d(splittedOnSpace[0], splittedOnSpace[1]);
					}
					else {
						firstNameLastName = "" + splittedOnSpace[0];
						//Log.d(getLocalClassName(), splittedOnSpace[0]);
					}
					profList.add(firstNameLastName);
				}
			}
			profListArray = profList.toArray(new String[profList.size()]);
			infile.close();
		}
		return profListArray;
	}

	public String[] parseCourses() throws Exception {
		// create Hashmap, where the numbers are the keys and the Titles are the
		// values

		HashMap<String, String> map = new HashMap<String, String>();
		HashSet<String> numberAndName = new HashSet<String>();
		String inputLine = "";
		String[] temp;

		String[] courseFiles = getAssets().list("CourseListings");

		for (int i = 0; i < courseFiles.length; i++) {
			int lineNumber = 0;
			BufferedReader infile = new BufferedReader(new InputStreamReader(
					getAssets().open("CourseListings/" + courseFiles[i])));
			while (infile.ready()) {// while more info exists
				if (lineNumber >= 13 && lineNumber % 3 == 1) { // only take
																// numbers 13
																// and up for
																// every 3
					temp = inputLine.split(" - ");
					for (int t = 0; t < temp.length; t++) {
						String number = temp[0].trim();// number;
						String name = temp[1].replaceAll("</A>", "").trim();// name
						map.put(number, name); // trim and place only the number
												// and name in
						numberAndName.add("" + number + " - " + capsFix2(name));
					} // for temp
				} // if
				inputLine = infile.readLine(); // read the next line of the text
				lineNumber++;
			} // while infile
			infile.close();
		} // for courseFiles

		Object[] courseNumberObjectArray = map.keySet().toArray();
		Object[] courseNameObjectArray = map.values().toArray();
		String[] allCourses = Arrays.copyOf(courseNameObjectArray,
				courseNameObjectArray.length, String[].class);
		String[] allNumbers = Arrays.copyOf(courseNumberObjectArray,
				courseNumberObjectArray.length, String[].class);
		Object[] allNumbersAndNames = numberAndName.toArray();
		String[] numbersAndNamesToReturn = Arrays.copyOf(allNumbersAndNames,
				allNumbersAndNames.length, String[].class);
		//Code to populate database:
		//ClientAsync as = new ClientAsync();
		//as.execute(numbersAndNamesToReturn);
		return numbersAndNamesToReturn;
		//return concat(allCourses, allNumbers);
	} // parse()

	String[] concat(String[] a, String[] b) {
		int aLen = a.length;
		int bLen = b.length;
		String[] c = new String[aLen + bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
	}
	
	public String[] capsFix(String[] s) {
		for (int z = 0; z < s.length; z++) {
			String[] temp = s[z].split(" ");

			for (int t = 0; t < temp.length; t++) {
				if (t == 0 && temp[t].equals("A")) {
					t++;
				}
				if (temp[t].equals("A") || temp[t].equals("FOR")
						|| temp[t].equals("THE") || temp[t].equals("OF")
						|| temp[t].equals("AND") || temp[t].equals("IN")
						|| temp[t].equals("AT") || temp[t].equals("AN")) {
					temp[t] = temp[t].toLowerCase(Locale.ENGLISH);
				}
				String firstLetter = temp[t].substring(0, 1); // take the first
																// letter
				temp[t] = temp[t].toLowerCase(Locale.ENGLISH); // make the word
																// lowercase
				String end = temp[t].substring(1, temp[t].length()); // get ride
																		// of
																		// the
																		// first
																		// letter
				temp[t] = firstLetter + end; // add firstletter and the rest of
												// the word
			}
			s[z] = "";
			for (int q = 0; q < temp.length; q++) {
				s[z] = s[z] + temp[q] + " ";
			}
		}
		return s;
	}
	
	public String capsFix2(String s) {
			String[] temp = s.split(" ");

			for (int t = 0; t < temp.length; t++) {
				if (t == 0 && temp[t].equals("A")) {
					t++;
				}
				if (temp[t].equals("A") || temp[t].equals("FOR")
						|| temp[t].equals("THE") || temp[t].equals("OF")
						|| temp[t].equals("AND") || temp[t].equals("IN")
						|| temp[t].equals("AT") || temp[t].equals("AN")) {
					temp[t] = temp[t].toLowerCase(Locale.ENGLISH);
				}
				String firstLetter = temp[t].substring(0, 1); // take the first
																// letter
				temp[t] = temp[t].toLowerCase(Locale.ENGLISH); // make the word
																// lowercase
				String end = temp[t].substring(1, temp[t].length()); // get ride
																		// of
																		// the
																		// first
																		// letter
				temp[t] = firstLetter + end; // add firstletter and the rest of
												// the word
			}
			s = "";
			for (int q = 0; q < temp.length; q++) {
				s = s + temp[q] + " ";
			}
		return s;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		// Get the SearchView and set the searchable configuration
		MenuItem searchItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) MenuItemCompat
				.getActionView(searchItem);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		// SearchView searchView = (SearchView)
		// menu.findItem(R.id.action_search)
		// .getActionView();
		// Assumes current activity is the searchable activity
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false); // Do not iconify the
		// widget;
		// expand it by default
		// searchView.setSubmitButtonEnabled(true);

		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		Log.w("MyApp", "In options");

		switch (item.getItemId()) {
		case R.id.action_logout:
			// openLoginPage(item);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private class ClientAsync extends AsyncTask<String, Void, String> {

		public ClientAsync() {
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//tvStatus.setText("wait..");
		}

		@Override
		protected String doInBackground(String... params) {
			Log.d(getLocalClassName(), String.valueOf(params.length));
			String result = null;
			for (int i = 100; i < params.length; i++) {
				String[] splitted = params[i].split(" - ");
				String number = splitted[0];
				String name = splitted[1];
				Log.d(number, name);
				Course c = new Course(null, name, number, null, null, true);
				result = db.insertCourse(c).toString();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String res) {
			if (res == null)
				//tvStatus.setText("null");
				Log.d(getLocalClassName(), "unsuccessful");
			else {
				//tvStatus.setText(res);
				Log.d(getLocalClassName(), res);
			}
		}
	}
}


