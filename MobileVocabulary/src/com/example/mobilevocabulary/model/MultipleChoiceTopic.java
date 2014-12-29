/**
 * 
 */
package com.example.mobilevocabulary.model;

import java.io.Serializable;
import java.util.ArrayList;

import android.view.View;


/**
 * @author hungndv
 *
 */
@SuppressWarnings("serial")
public class MultipleChoiceTopic implements Serializable {
	public int Id;
	public String Text;
	public String Description;
	public long Time = View.NO_ID;
	public long RemainingTime = View.NO_ID;
	public int CorrectCount = 0;
	public int QuestionCount = 0;
	public ArrayList<MultipleChoiceQuestion> Questions;
}
