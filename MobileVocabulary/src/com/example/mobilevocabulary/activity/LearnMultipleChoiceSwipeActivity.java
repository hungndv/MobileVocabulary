package com.example.mobilevocabulary.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import android.R.color;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilevocabulary.R;
import com.example.mobilevocabulary.DAL.DBHelper;
import com.example.mobilevocabulary.model.Choice;
import com.example.mobilevocabulary.model.MultipleChoiceQuestion;
import com.example.mobilevocabulary.model.MultipleChoiceTopic;
import com.example.mobilevocabulary.util.Misc;

@SuppressLint("ShowToast") public class LearnMultipleChoiceSwipeActivity extends ActionBarActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	public final static String RESULT = "com.example.englishvocabulary.RESULT";

	private static ArrayList<MultipleChoiceQuestion> questions = new ArrayList<MultipleChoiceQuestion>();
	private MultipleChoiceTopic currentTopic;

	private ProgressDialog progressDialog;
	private DBHelper dbHelper;
	private MultipleChoiceQuestion currentQuestion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multiple_choice_swipe);

		Intent intent = getIntent();
		MultipleChoiceTopic newTopic = (MultipleChoiceTopic) (intent
				.getSerializableExtra(TopicSelectionActivity.TOPIC));

		if (newTopic == null) {
			// can't get newTopicId
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					mViewPager.getContext());
			alertDialogBuilder.setTitle("Error happen!");
			alertDialogBuilder
					.setMessage("Sorry! Back to main.")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									Intent intent = new Intent(
											getApplicationContext(),
											MainActivity.class);
									startActivity(intent);
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
			return;
		}

		if (currentTopic == null || currentTopic.Id != newTopic.Id) {
			currentTopic = newTopic;
			new LoadViewTask().execute();
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void setActionBarTitle(String timeString) {
		ActionBar ab = getActionBar();
		ab.setTitle(timeString);
	}

	public void initializeData() {
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		currentQuestion = questions.get(0);		
		
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				currentQuestion = questions.get(position);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {				
			}
		});
	}

	private void gotoResultActivity(MultipleChoiceTopic topic) {
		topic.CorrectCount = Misc.getCorrectAnswer(questions);

		Intent intent = new Intent(getApplicationContext(),
				ResultActivity.class);
		intent.putExtra(RESULT, topic);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.learn_multiple_choice_swipe, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_answer) {			
			for(Choice choice : currentQuestion.Choices){
				if (choice.IsTrue){
					Toast.makeText(getApplicationContext(), choice.Text, 2000).show();
					break;
				}
			}
			return true;
		} else if (id == R.id.action_end) {

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					mViewPager.getContext());
			alertDialogBuilder.setTitle("Confirm");
			alertDialogBuilder
					.setMessage("Could you want to end the exam? No to return.")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									gotoResultActivity(currentTopic);
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// if this button is clicked, just close
									// the dialog box and do nothing
									dialog.cancel();
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			return PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return questions.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";
		private TextView tvQuestionText;
		private RadioGroup rgChoices;

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final View rootView = inflater.inflate(
					R.layout.fragment_learn_multiple_choice_swipe, container,
					false);
			TextView currentPage = (TextView) (rootView
					.findViewById(R.id.section_label));
			currentPage.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER))
					+ "/" + String.valueOf(questions.size()));

			MultipleChoiceQuestion currentQuestion = questions.get(getArguments()
					.getInt(ARG_SECTION_NUMBER) - 1);

			tvQuestionText = (TextView) (rootView
					.findViewById(R.id.tvQuestionText));
			tvQuestionText.setText(currentQuestion.Text);

			rgChoices = (RadioGroup) (rootView.findViewById(R.id.rgChoices));
			RadioButton rdo;
			for (Choice choice : currentQuestion.Choices) {
				rdo = new RadioButton(rootView.getContext());
				rdo.setId(choice.Id);
				rdo.setText(choice.Text);
				rdo.setChecked(currentQuestion.SelectedId != View.NO_ID
						&& currentQuestion.SelectedId == rdo.getId());
				// rdo.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_background));
				rgChoices.addView(rdo);
				View ruler = new View(rootView.getContext());
				ruler.setBackgroundColor(color.background_dark);
				rgChoices.addView(ruler, new ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, 2));
			}

			rgChoices.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					MultipleChoiceQuestion question = questions
							.get(getArguments().getInt(ARG_SECTION_NUMBER) - 1);
					question.SelectedId = checkedId;
				}
			});

			return rootView;
		}

	}

	// To use the AsyncTask, it must be subclassed
	private class LoadViewTask extends AsyncTask<Void, Integer, Void> {
		// Before running code in separate thread
		@Override
		protected void onPreExecute() {
			// Create a new progress dialog
			progressDialog = new ProgressDialog(
					LearnMultipleChoiceSwipeActivity.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setTitle("Loading...");
			progressDialog
					.setMessage("Loading topic data, please wait...");
			progressDialog.setCancelable(false);
			progressDialog.setIndeterminate(false);
			progressDialog.setMax(100);
			progressDialog.setProgress(0);
			progressDialog.show();
		}

		// The code to be executed in a background thread.
		@Override
		protected Void doInBackground(Void... params) {
			// Get the current thread's token
			synchronized (this) {
				questions.clear();
				dbHelper = new DBHelper(getApplicationContext(),
						DBHelper.DATABASE_NAME, null, DBHelper.DATABASE_VERSION);
				questions = dbHelper.getQuestionsOfTopic(currentTopic);
				int questionCount = questions.size();

				MultipleChoiceQuestion question;
				for (int i = 0; i < questionCount; i++) {
					question = questions.get(i);
					question.Choices = new ArrayList<Choice>();
					question.Choices.addAll(dbHelper
							.getChoicesOfQuestion(question));
					Collections.shuffle(question.Choices);

					int progress = (int) ((float) (i + 1) / questionCount);
					publishProgress(progress);
				}

				currentTopic.QuestionCount = questionCount;
				Collections.shuffle(questions);
			}
			return null;
		}

		// Update the progress
		@Override
		protected void onProgressUpdate(Integer... values) {
			// set the current progress of the progress dialog
			progressDialog.setProgress(values[0]);
		}

		// after executing the code in the thread
		@Override
		protected void onPostExecute(Void result) {
			// close the progress dialog
			progressDialog.dismiss();

			initializeData();
		}
	}

}
