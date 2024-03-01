package edu.chnu.recruiting.front.components.questions;

import edu.chnu.recruiting.models.wizard.ValueType;

public class TextQuestion extends QuestionComponent {
	public TextQuestion() {
		super();
		this.field.setType(ValueType.TEXT);
		label.setText(label.getText() + ValueType.TEXT.toString());
	}
}