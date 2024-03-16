package edu.chnu.recruiting.front.components.questions;

import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

import edu.chnu.recruiting.models.wizard.ValueType;

public class QuestionCard extends HorizontalLayout implements DragSource<QuestionCard>, Cloneable {
	Span label = new Span();
	private ValueType type;

	public QuestionCard(ValueType type) {
		this.type = type;
		setWidthFull();
		setDraggable(true);
		addClassName("question-card");
		addClassNames(LumoUtility.Padding.MEDIUM);
		getStyle().set("border", "thick double #32a1ce");
		label.setText("Input type: " + type.toString());
		add(label);
	}

	public QuestionComponent getQuestionComponent() {
		return switch (this.type) {
		case NUMBER -> new QuestionComponent(ValueType.NUMBER);
		case TEXT -> new QuestionComponent(ValueType.TEXT);
		case SELECTION_SINGLE -> new QuestionComponent(ValueType.SELECTION_SINGLE);
		case SELECTION_MULTIPLE -> new QuestionComponent(ValueType.SELECTION_MULTIPLE);
		case UPLOAD -> new QuestionComponent(ValueType.UPLOAD);
		case AUDIO -> new QuestionComponent(ValueType.AUDIO);
		case DATE -> new QuestionComponent(ValueType.DATE);
		default -> null;
		};
	}
}
