package edu.chnu.recruiting.front.components.questions;

import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.html.Div;
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
	
	public ValueType getType() {
		return this.type;
	}
}
