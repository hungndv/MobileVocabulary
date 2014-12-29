/**
 * 
 */
package com.example.mobilevocabulary.util;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.view.View;

import com.example.mobilevocabulary.model.Choice;
import com.example.mobilevocabulary.model.MultipleChoiceQuestion;

/**
 * @author hungndv
 *
 */
@SuppressLint("DefaultLocale") public class Misc {
	@SuppressLint("DefaultLocale") public static String clockTextFromMilliseconds(long millisUntilFinished){
		long hours = millisUntilFinished / (1000*60*60);
		millisUntilFinished %= (1000*60*60);
		long minutes = millisUntilFinished / (1000*60);
		millisUntilFinished %= (1000*60);
		long seconds = millisUntilFinished / 1000;
		
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}
	
	public static int getCorrectAnswer(ArrayList<MultipleChoiceQuestion> multipleChoiceQuestions){			
		int corrects = 0;
		for(MultipleChoiceQuestion question : multipleChoiceQuestions){
			if (question.SelectedId != View.NO_ID)
				for(Choice choice : question.Choices)
					if (question.SelectedId == choice.Id && choice.IsTrue){
						corrects++;
						break;
					}
		}
		
		return corrects;
	}
}
