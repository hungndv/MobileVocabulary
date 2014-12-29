package com.example.mobilevocabulary.model;

public class Choice {
	public int Id;
	public String Text;
	public boolean IsTrue = false;
	
	public Choice(String text){
		this.Text = text;
	}
	
	public Choice(String text, boolean isTrue){
		this.Text = text;
		this.IsTrue = isTrue;
	}

	public Choice() {
	}
}
