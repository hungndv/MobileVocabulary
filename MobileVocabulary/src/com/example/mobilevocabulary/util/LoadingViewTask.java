/**
 * 
 */
package com.example.mobilevocabulary.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.example.mobilevocabulary.R;
import com.example.mobilevocabulary.DAL.DBHelper;
import com.example.mobilevocabulary.model.Choice;
import com.example.mobilevocabulary.model.MultipleChoiceQuestion;
import com.example.mobilevocabulary.model.MultipleChoiceTopic;

/**
 * @author hungndv
 * 
 */
public class LoadingViewTask extends
		AsyncTask<MultipleChoiceTopic, Integer, String> {
	private ProgressDialog progressDialog;
	private DBHelper dbHelper;
	private boolean isIndeterminate;
	private Context context;
	private String progressDialogTitle;
	private String progressDialogMessage; 
	private int progressTotal;

	public LoadingViewTask() {
	}

	public LoadingViewTask(Context context, String progressDialogTitle, String progressDialogMessage,
			boolean isIndeterminate, int progressTotal) {
		this.isIndeterminate = isIndeterminate;
		this.dbHelper = new DBHelper(context, DBHelper.DATABASE_NAME, null,
				DBHelper.DATABASE_VERSION);
		this.context = context;
		this.progressDialogTitle = progressDialogTitle;
		this.progressDialogMessage = progressDialogMessage;
		this.progressTotal = progressTotal;
	}

	// Before running code in separate thread
	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(context);
		if (isIndeterminate){
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		} else {
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setMax(progressTotal);
			progressDialog.setButton(AlertDialog.BUTTON_NEUTRAL, context
					.getResources().getString(R.string.button_ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int buttonId) {
							dialog.dismiss();
						}
					});
		}
		progressDialog.setTitle(progressDialogTitle);
		progressDialog.setMessage(progressDialogMessage);
		progressDialog.setCancelable(false);
		progressDialog.setIndeterminate(isIndeterminate);
		progressDialog.show();
		if (!isIndeterminate)
			progressDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setEnabled(false);
	}

	// The code to be executed in a background thread.
	@Override
	protected String doInBackground(MultipleChoiceTopic... params) {
		// Get the current thread's token
		synchronized (this) {
			MultipleChoiceTopic topic = params[0];
			if (topic == null) {
				return Constant.TOPIC_IS_NULL;
			}

			if (topic.Text == null || topic.Text.trim() == "") {
				return Constant.TOPIC_NAME_IS_NULL_OR_EMPTY;
			}

			if (topic.Questions == null || topic.Questions.size() <= 0) {
				return Constant.QUESTIONS_ARE_NULL_OR_EMPTY;
			}

			long topicId = dbHelper.insertTopic(topic.Text, "");
			int progressCurrent = 1;
			for (MultipleChoiceQuestion q : topic.Questions) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				long questionId = dbHelper.insertQuestion(q.Text, topicId);
				for (Choice c : q.Choices) {
					dbHelper.insertChoice(c.Text, c.IsTrue, questionId);
				}
				if (!isIndeterminate) {
					publishProgress(progressCurrent);
					progressCurrent++;
				}
			}

		}
		return Constant.IMPORT_TOPIC_SUCCESSFULLY;
	}

	// Update the progress
	@Override
	protected void onProgressUpdate(Integer... values) {
		// set the current progress of the progress dialog
		progressDialog.setProgress(values[0]);
	}

	// after executing the code in the thread
	@Override
	protected void onPostExecute(String message) {
		// close the progress dialog
		if (isIndeterminate)
			progressDialog.dismiss();
		else {
			progressDialog.setCancelable(true);
			progressDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setEnabled(true);
		}
	}
}
