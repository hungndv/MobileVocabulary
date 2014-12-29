/**
 * 
 */
package com.example.mobilevocabulary.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.nfc.FormatException;
import au.com.bytecode.opencsv.CSVReader;

import com.example.mobilevocabulary.model.Choice;
import com.example.mobilevocabulary.model.MultipleChoiceQuestion;

/**
 * @author hungndv
 * 
 */
public class CSVHelper {
	@SuppressWarnings("resource")
	public ArrayList<MultipleChoiceQuestion> getMultipleChoiceQuestion(
			String filePath) throws FormatException, IOException {
		ArrayList<MultipleChoiceQuestion> questions = new ArrayList<MultipleChoiceQuestion>();

		FileReader fileReader = new FileReader(filePath);
		CSVReader csvReader = new CSVReader(fileReader, ',', '"', 0);

		String[] nextLine;
		int line = 1;
		while ((nextLine = csvReader.readNext()) != null) {
			if (nextLine.length <= 1) {
				throw new FormatException(String.format(
						"File [%s], line [%d] is invalid.", filePath, line));
			} else {
				// valid line format
				MultipleChoiceQuestion question = new MultipleChoiceQuestion();
				question.Text = nextLine[0];
				ArrayList<Choice> choices = new ArrayList<Choice>();
				for (int i = 1; i < nextLine.length; i++) {
					Choice choice = new Choice();

					if (nextLine[i].endsWith("&&")) {
						choice.IsTrue = true;
						choice.Text = nextLine[i].split("&&")[0];
					} else {
						choice.Text = nextLine[i];
					}

					choices.add(choice);
				}
				question.Choices = choices;
				questions.add(question);

			}

			line++;
		}
		
		csvReader.close();
		fileReader.close();
		return questions;
	}
}
