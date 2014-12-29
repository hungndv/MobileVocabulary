/**
 * 
 */
package com.example.mobilevocabulary.DAL;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mobilevocabulary.model.Choice;
import com.example.mobilevocabulary.model.MultipleChoiceQuestion;
import com.example.mobilevocabulary.model.MultipleChoiceTopic;

public class DBHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "MVDB.db";
	public static final int DATABASE_VERSION = 1;

	public static final String TOPIC_TABLE_NAME = "TOPIC";
	public static final String TOPIC_COLUMN_ID = "ID";
	public static final String TOPIC_COLUMN_TOPIC_TEXT = "TOPIC_TEXT";
	public static final String TOPIC_COLUMN_DESCRIPTION = "DESCRIPTION";

	public static final String QUESTION_TABLE_NAME = "QUESTION";
	public static final String QUESTION_COLUMN_ID = "ID";
	public static final String QUESTION_COLUMN_QUESTION_TEXT = "QUESTION_TEXT";
	public static final String QUESTION_COLUMN_TOPIC_ID = "TOPIC_ID";

	public static final String CHOICE_TABLE_NAME = "CHOICE";
	public static final String CHOICE_COLUMN_ID = "ID";
	public static final String CHOICE_COLUMN_CHOICE_TEXT = "CHOICE_TEXT";
	public static final String CHOICE_COLUMN_IS_TRUE = "IS_TRUE";
	public static final String CHOICE_COLUMN_QUESTION_ID = "QUESTION_ID";

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE IF NOT EXISTS " + TOPIC_TABLE_NAME + " ("
				+ TOPIC_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ TOPIC_COLUMN_TOPIC_TEXT + " TEXT NOT NULL, "
				+ TOPIC_COLUMN_DESCRIPTION + " TEXT NOT NULL);");
		db.execSQL("CREATE TABLE IF NOT EXISTS " + QUESTION_TABLE_NAME + " ("
				+ QUESTION_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ QUESTION_COLUMN_QUESTION_TEXT + " TEXT NOT NULL, "
				+ QUESTION_COLUMN_TOPIC_ID + "  INTEGER NOT NULL);");
		db.execSQL("CREATE TABLE IF NOT EXISTS " + CHOICE_TABLE_NAME + " ("
				+ CHOICE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ CHOICE_COLUMN_CHOICE_TEXT + " TEXT NOT NULL, "
				+ CHOICE_COLUMN_IS_TRUE + " INTEGER, "
				+ CHOICE_COLUMN_QUESTION_ID + " INTEGER NOT NULL);");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + CHOICE_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + QUESTION_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + TOPIC_TABLE_NAME);
		onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + CHOICE_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + QUESTION_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + TOPIC_TABLE_NAME);
		onCreate(db);
	}

	public long insertTopic(String topicText, String description) {
		long returned = -1;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();

		contentValues.put(TOPIC_COLUMN_TOPIC_TEXT, topicText);
		contentValues.put(TOPIC_COLUMN_DESCRIPTION, description);

		returned = db.insert(TOPIC_TABLE_NAME, null, contentValues);
		return returned;
	}

	public long insertQuestion(String questionText, long topicId) {
		long returned = -1;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();

		contentValues.put(QUESTION_COLUMN_QUESTION_TEXT, questionText);
		contentValues.put(QUESTION_COLUMN_TOPIC_ID, topicId);

		returned = db.insert(QUESTION_TABLE_NAME, null, contentValues);
		return returned;
	}

	public long insertChoice(String choiceText, boolean isTrue, long questionId) {
		long returned = -1;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();

		contentValues.put(CHOICE_COLUMN_CHOICE_TEXT, choiceText);
		contentValues.put(CHOICE_COLUMN_IS_TRUE, isTrue ? 1 : 0);
		contentValues.put(CHOICE_COLUMN_QUESTION_ID, questionId);

		returned = db.insert(CHOICE_TABLE_NAME, null, contentValues);
		return returned;
	}

	public Cursor getTopic(int topicId) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("SELECT * FROM " + TOPIC_TABLE_NAME
				+ " WHERE ID = " + topicId + "", null);
		return res;
	}

	public int numberOfTopics() {
		SQLiteDatabase db = this.getReadableDatabase();
		int numRows = (int) DatabaseUtils.queryNumEntries(db, TOPIC_TABLE_NAME);
		return numRows;
	}

	public boolean updateTopic(int id, String topicText, String description) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(TOPIC_COLUMN_TOPIC_TEXT, topicText);
		contentValues.put(TOPIC_COLUMN_DESCRIPTION, description);
		db.update(TOPIC_TABLE_NAME, contentValues, TOPIC_COLUMN_ID + " = ? ",
				new String[] { Integer.toString(id) });
		return true;
	}

	public Integer deleteTopic(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete(TOPIC_TABLE_NAME, "id = ? ",
				new String[] { Integer.toString(id) });
	}

	public ArrayList<MultipleChoiceTopic> getAllTopic() {
		ArrayList<MultipleChoiceTopic> array_list = new ArrayList<MultipleChoiceTopic>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select * from " + TOPIC_TABLE_NAME, null);
		if (res.moveToFirst()) {
			MultipleChoiceTopic topic;
			while (res.isAfterLast() == false) {
				topic = new MultipleChoiceTopic();
				topic.Id = res.getInt(res.getColumnIndex(TOPIC_COLUMN_ID));
				topic.Text = res.getString(res
						.getColumnIndex(TOPIC_COLUMN_TOPIC_TEXT));
				topic.Description = res.getString(res
						.getColumnIndex(TOPIC_COLUMN_DESCRIPTION));
				array_list.add(topic);
				res.moveToNext();
			}
		}
		return array_list;
	}

	public ArrayList<MultipleChoiceQuestion> getQuestionsOfTopic(
			MultipleChoiceTopic topic) {
		ArrayList<MultipleChoiceQuestion> questions = new ArrayList<MultipleChoiceQuestion>();

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("SELECT * FROM " + QUESTION_TABLE_NAME
				+ " WHERE " + QUESTION_COLUMN_TOPIC_ID + " = " + topic.Id + "",
				null);
		if (res.moveToFirst()) {
			MultipleChoiceQuestion question;
			while (res.isAfterLast() == false) {
				question = new MultipleChoiceQuestion();
				question.Id = res
						.getInt(res.getColumnIndex(QUESTION_COLUMN_ID));
				question.Text = res.getString(res
						.getColumnIndex(QUESTION_COLUMN_QUESTION_TEXT));
				questions.add(question);
				res.moveToNext();
			}
		}

		return questions;
	}

	public ArrayList<Choice> getChoicesOfQuestion(
			MultipleChoiceQuestion question) {
		ArrayList<Choice> choices = new ArrayList<Choice>();

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("SELECT * FROM " + CHOICE_TABLE_NAME
				+ " WHERE " + CHOICE_COLUMN_QUESTION_ID + " = " + question.Id
				+ "", null);
		if (res.moveToFirst()) {
			Choice choice;
			while (res.isAfterLast() == false) {
				choice = new Choice();
				choice.Id = res.getInt(res.getColumnIndex(CHOICE_COLUMN_ID));
				choice.Text = res.getString(res
						.getColumnIndex(CHOICE_COLUMN_CHOICE_TEXT));
				choice.IsTrue = res.getInt(res
						.getColumnIndex(CHOICE_COLUMN_IS_TRUE)) == 1 ? true
						: false;
				choices.add(choice);
				res.moveToNext();
			}
		}

		return choices;
	}

}