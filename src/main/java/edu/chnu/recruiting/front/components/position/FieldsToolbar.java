package edu.chnu.recruiting.front.components.position;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import edu.chnu.recruiting.front.components.questions.NumberQuestion;
import edu.chnu.recruiting.front.components.questions.QuestionCard;
import edu.chnu.recruiting.front.components.questions.TextQuestion;
import edu.chnu.recruiting.models.wizard.ValueType;

public class FieldsToolbar extends VerticalLayout{
	public FieldsToolbar() {
		add(
				new QuestionCard(ValueType.NUMBER), 
				new QuestionCard(ValueType.TEXT)
				);
	}
}
