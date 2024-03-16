package edu.chnu.recruiting.front.components.position;

import java.util.Arrays;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import edu.chnu.recruiting.front.components.questions.QuestionCard;
import edu.chnu.recruiting.models.wizard.ValueType;

public class FieldsToolbar extends VerticalLayout{
	public FieldsToolbar() {
		Arrays.stream(ValueType.values()).forEach(v -> add(new QuestionCard(v)));
	}
}
