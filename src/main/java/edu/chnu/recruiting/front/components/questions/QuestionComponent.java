package edu.chnu.recruiting.front.components.questions;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import edu.chnu.recruiting.models.wizard.WizardField;

public abstract class QuestionComponent extends VerticalLayout{
	Span label = new Span("Input type: ");
	TextField question = new TextField("Question");
	WizardField field = new WizardField();
	Button removeBtn = new Button(new Icon(VaadinIcon.CLOSE));

	public QuestionComponent() {		
		getStyle().set("border", "solid");
		removeBtn.addClickListener(e -> remove());
		add(label, question, removeBtn);
	}
	
	public void remove() {
		this.removeFromParent();
	}
}
