package com.example.mobilevocabulary.util;

import java.util.ArrayList;

import com.example.mobilevocabulary.model.Choice;
import com.example.mobilevocabulary.model.MultipleChoiceQuestion;
import com.example.mobilevocabulary.model.MultipleChoiceTopic;

public class InitialData {
	public MultipleChoiceTopic getInitialTopic(){
		MultipleChoiceTopic topic = new MultipleChoiceTopic();
		topic.Text = "600 toeic words";
		ArrayList<MultipleChoiceQuestion> questions = new ArrayList<MultipleChoiceQuestion>();

		MultipleChoiceQuestion question;

		question = new MultipleChoiceQuestion("learn");
		question.Choices = new ArrayList<Choice>();
		question.Choices.add(new Choice("study", true));
		question.Choices.add(new Choice("work"));
		question.Choices.add(new Choice("do"));
		question.Choices.add(new Choice("sleep"));
		questions.add(question);

		question = new MultipleChoiceQuestion("get");
		question.Choices = new ArrayList<Choice>();
		question.Choices.add(new Choice("take", true));
		question.Choices.add(new Choice("work"));
		question.Choices.add(new Choice("do"));
		question.Choices.add(new Choice("sleep"));
		questions.add(question);
		
		topic.Questions = questions;
		return topic;
	}
}
