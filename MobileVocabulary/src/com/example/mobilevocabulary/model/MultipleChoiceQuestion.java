/**
 * 
 */
package com.example.mobilevocabulary.model;

import java.util.ArrayList;

import android.view.View;

/**
 * @author hungndv
 *
 */
public class MultipleChoiceQuestion {
	public int Id;
	public String Text;
	public ArrayList<Choice> Choices;
	public int SelectedId = View.NO_ID;
	
	public MultipleChoiceQuestion(String text){
		this.Text = text;
	}
	
	public MultipleChoiceQuestion() {
	}

	public Choice getCorrect(){
		Choice returned = null;
		for(Choice choice : Choices){
			if (choice.IsTrue){
				returned = choice;
				break;
			}
		}
		return returned;
	}
}
