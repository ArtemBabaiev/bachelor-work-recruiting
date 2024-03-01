package edu.chnu.recruiting.front.components.questions;

import edu.chnu.recruiting.models.wizard.ValueType;

public class NumberQuestion extends QuestionComponent {
	public NumberQuestion() {
		super();
		this.field.setType(ValueType.NUMBER);
		label.setText(label.getText() + ValueType.NUMBER.toString());
	}
}
