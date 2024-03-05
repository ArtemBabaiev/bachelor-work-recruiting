package edu.chnu.recruiting.front.components.questions;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import edu.chnu.recruiting.front.components.position.OptionComponent;
import edu.chnu.recruiting.models.wizard.ValueType;
import edu.chnu.recruiting.models.wizard.WizardField;

public class RadioQuestion extends QuestionComponent {
	VerticalLayout optionsList = new VerticalLayout();
	TextField optionField = new TextField();
	Button addButton = new Button("Add");
	Span optionTxt = new Span("Options");

	public RadioQuestion() {
		super();
		this.field.setType(ValueType.SELECTION_RADIO);
		label.setText(ValueType.SELECTION_RADIO.toString());
		addButton.addClickListener(click -> {
			optionsList.add(new OptionComponent(optionField.getValue()));
			optionField.clear();
		});
		add(new HorizontalLayout(optionField, addButton), optionsList);
	}

	@Override
	public WizardField getField() {
		optionsList.getChildren().map(c -> (OptionComponent) c).forEach(c -> this.field.addOption(c.getValue()));
		return super.getField();
	}
}
