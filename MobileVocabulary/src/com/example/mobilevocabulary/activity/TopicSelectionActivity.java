package com.example.mobilevocabulary.activity;

import java.util.ArrayList;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.example.mobilevocabulary.R;
import com.example.mobilevocabulary.DAL.DBHelper;
import com.example.mobilevocabulary.model.MultipleChoiceTopic;

public class TopicSelectionActivity extends ActionBarActivity {
	public final static String TOPIC = "com.example.englishvocabulary.TOPIC";
	private ProgressDialog progressDialog;
	private ArrayList<MultipleChoiceTopic> topics = new ArrayList<MultipleChoiceTopic>();
	TopicAdapter topicAdapter = null;
	DBHelper dbHelper = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic_selection);

		// Initialize a LoadViewTask object and call the execute() method
		if (topics == null || topics.size() <= 0)
			new LoadViewTask().execute();
		else
			initializeData();
	}

	private void initializeData() {
		// create an ArrayAdaptar from the String Array
		topicAdapter = new TopicAdapter(this, R.layout.topic_list_view_item,
				topics);
		ListView lvTopic = (ListView) findViewById(R.id.lvTopic);
		// Assign adapter to ListView
		lvTopic.setAdapter(topicAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.topic_selection, menu);
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

	private class TopicAdapter extends ArrayAdapter<MultipleChoiceTopic> {

		private ArrayList<MultipleChoiceTopic> multipleChoiceTopics;

		public TopicAdapter(Context context, int textViewResourceId,
				ArrayList<MultipleChoiceTopic> multipleChoiceTopics) {
			super(context, textViewResourceId, multipleChoiceTopics);
			this.multipleChoiceTopics = new ArrayList<MultipleChoiceTopic>();
			this.multipleChoiceTopics.addAll(multipleChoiceTopics);
		}

		private class ViewHolder {
			TextView Text;
			TextView Description;
			Button Start;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;

			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.topic_list_view_item, null);

				holder = new ViewHolder();
				holder.Text = (TextView) convertView.findViewById(R.id.tvTopic);
				holder.Description = (TextView) convertView
						.findViewById(R.id.tvDescription);
				holder.Start = (Button) convertView.findViewById(R.id.btnStart);
				convertView.setTag(holder);

				holder.Start.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Button btnStart = (Button) v;
						final MultipleChoiceTopic topic = (MultipleChoiceTopic) (btnStart
								.getTag());

						// custom dialog
						final Dialog dialog = new Dialog(
								TopicSelectionActivity.this);
						dialog.setContentView(R.layout.settings_dialog);
						dialog.setTitle("Settings");
						final SeekBar sbTimeAmount = (SeekBar) dialog
								.findViewById(R.id.sbTimeAmount);
						sbTimeAmount.setProgress((int) (sbTimeAmount.getMax() / 2));
						sbTimeAmount.setEnabled(false);
						final TextView tvTimeAmount = (TextView) dialog
								.findViewById(R.id.tvTimeAmount);
						
						RadioGroup rgSettings = (RadioGroup)dialog.findViewById(R.id.rgSettings);
						
						rgSettings.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
							
							@Override
							public void onCheckedChanged(RadioGroup group, int checkedId) {
								sbTimeAmount.setEnabled(checkedId == R.id.radExam);
								tvTimeAmount.setVisibility(checkedId == R.id.radExam ? View.VISIBLE
													: View.INVISIBLE);
								tvTimeAmount.setText(sbTimeAmount.getProgress() + "/"
										+ sbTimeAmount.getMax()
										+ " minute(s)");
							}
						});

						
						tvTimeAmount.setVisibility(View.INVISIBLE);
						tvTimeAmount.setText(sbTimeAmount.getProgress() + "/"
								+ sbTimeAmount.getMax() + " minute(s)");

						sbTimeAmount
								.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

									@Override
									public void onStopTrackingTouch(
											SeekBar seekBar) {

									}

									@Override
									public void onStartTrackingTouch(
											SeekBar seekBar) {

									}

									@Override
									public void onProgressChanged(
											SeekBar seekBar, int progress,
											boolean fromUser) {
										if (progress <= 0) {
											progress = 1;
											seekBar.setProgress(progress);
										}
										tvTimeAmount.setText(progress + "/"
												+ seekBar.getMax()
												+ " minute(s)");
									}
								});

						Button btnBack = (Button) dialog
								.findViewById(R.id.btnBack);
						// if button is clicked, close the custom dialog
						btnBack.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});

						Button btnOK = (Button) dialog.findViewById(R.id.btnOK);
						btnOK.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent();
								if (sbTimeAmount.isEnabled()){
									intent.setClass(getApplicationContext(),
											ExamMultipleChoiceSwipeActivity.class);
									topic.Time = sbTimeAmount.getProgress() * 60 * 1000;
								}
								else{
									intent.setClass(getApplicationContext(),
											LearnMultipleChoiceSwipeActivity.class);
								}
								intent.putExtra(TOPIC, topic);
								startActivity(intent);
							}
						});

						dialog.show();
					}
				});
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			MultipleChoiceTopic topic = topics.get(position);
			holder.Text.setText(topic.Text);
			holder.Description
					.setText(topic.Description);
			holder.Start.setTag(topic);

			return convertView;

		}

	}

	// To use the AsyncTask, it must be subclassed
	private class LoadViewTask extends AsyncTask<Void, Integer, Void> {
		// Before running code in separate thread
		@Override
		protected void onPreExecute() {
			// Create a new progress dialog
			progressDialog = new ProgressDialog(TopicSelectionActivity.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setTitle("Loading...");
			progressDialog
					.setMessage("Loading topics, please wait...");
			progressDialog.setCancelable(false);
			progressDialog.setIndeterminate(false);
			progressDialog.setMax(100);
			progressDialog.setProgress(0);
			progressDialog.show();
		}

		// The code to be executed in a background thread.
		@Override
		protected Void doInBackground(Void... params) {
			synchronized (this) {
				dbHelper = new DBHelper(getApplicationContext(),
						DBHelper.DATABASE_NAME, null, DBHelper.DATABASE_VERSION);
				topics = dbHelper.getAllTopic();
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
