package com.example.mobilevocabulary.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Intent;
import android.nfc.FormatException;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.mobilevocabulary.R;
import com.example.mobilevocabulary.DAL.DBHelper;
import com.example.mobilevocabulary.model.MultipleChoiceQuestion;
import com.example.mobilevocabulary.model.MultipleChoiceTopic;
import com.example.mobilevocabulary.util.CSVHelper;
import com.example.mobilevocabulary.util.Constant;
import com.example.mobilevocabulary.util.Dialogs;
import com.example.mobilevocabulary.util.FileChooserDialogFragment;
import com.example.mobilevocabulary.util.InitialData;
import com.example.mobilevocabulary.util.LoadingViewTask;

public class MainActivity extends ActionBarActivity implements
		FileChooserDialogFragment.NoticeDialogListener {
	private Button btnMeaning;
	private DBHelper dbHelper;
	private Button btnSetup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		addListenerOnButton();

		dbHelper = new DBHelper(getApplicationContext(),
				DBHelper.DATABASE_NAME, null, DBHelper.DATABASE_VERSION);
		if (dbHelper.getAllTopic().size() <= 0) {
			InitialData initialData = new InitialData();
			MultipleChoiceTopic topic = initialData.getInitialTopic();
			new LoadingViewTask(MainActivity.this, Constant.IMPORTING,
					topic.Text, true, topic.Questions.size()).execute(topic);
		}
	}

	private void addListenerOnButton() {
		btnMeaning = (Button) findViewById(R.id.btnMultiChoice);
		btnMeaning.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				goToTopicSelectionActivity();
			}
		});

		btnSetup = (Button) findViewById(R.id.btnSetup);
		btnSetup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FileChooserDialogFragment fcdf = new FileChooserDialogFragment();
				// fcdf.IsDirectoryChooser = true;
				// fcdf.FileType = ".csv";
				fcdf.setCancelable(true);
				fcdf.show(getSupportFragmentManager(),
						"FileChooserDialogFragment");
			}
		});

		registerForContextMenu(btnSetup);
	}

	private void goToTopicSelectionActivity() {
		Intent intent = new Intent(this, TopicSelectionActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.InfoNotification) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		FileChooserDialogFragment fcdf = (FileChooserDialogFragment) dialog;
		String filePath = fcdf.CanonicalFilePath;
		CSVHelper csvHelper = new CSVHelper();
		ArrayList<MultipleChoiceQuestion> questions = null;
		try {
			questions = csvHelper.getMultipleChoiceQuestion(filePath);
		} catch (FormatException e) {
			Dialogs.showFailureDailog(MainActivity.this, "Invalid...",
					e.getMessage());
			return;
		} catch (IOException e) {
			Dialogs.showFailureDailog(MainActivity.this, "Exception...",
					e.getMessage());
			return;
		}

		if (questions != null && questions.size() > 0) {
			File file = new File(filePath);
			MultipleChoiceTopic topic = new MultipleChoiceTopic();
			topic.Text = file.getName();
			topic.Questions = questions;
			new LoadingViewTask(MainActivity.this, "Importing...", filePath,
					false, topic.Questions.size()).execute(topic);
		}
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Choose a type");
		menu.add(0, v.getId(), 0,
				getString(R.string.button_text_multiple_choice));
		menu.add(0, v.getId(), 1, getString(R.string.button_text_listen));
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getTitle() == getString(R.string.button_text_multiple_choice)) {
//			Intent intent = new Intent(MainActivity.this, MultiChoiceManagerActivity.class);
//			startActivity(intent);
		} else if (item.getTitle() == getString(R.string.button_text_listen)) {

		} else {
			return false;
		}
		return true;
	}
}
