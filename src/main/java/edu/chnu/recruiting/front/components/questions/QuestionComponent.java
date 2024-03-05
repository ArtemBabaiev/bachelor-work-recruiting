package edu.chnu.recruiting.front.components.questions;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;

import edu.chnu.recruiting.front.views.company.CompanyModel;
import edu.chnu.recruiting.models.wizard.WizardField;

public abstract class QuestionComponent extends VerticalLayout{
	Binder<WizardField> binder = new BeanValidationBinder<WizardField>(WizardField.class);
	Span label = new Span("Input type: ");
	TextField question = new TextField("Question");
	WizardField field = new WizardField();
	Button removeBtn = new Button(new Icon(VaadinIcon.CLOSE));

	public QuestionComponent() {	
		binder.bindInstanceFields(this);
		getStyle().set("border", "solid");
		removeBtn.addClickListener(e -> remove());
		question.setValueChangeMode(ValueChangeMode.EAGER);
		binder.setBean(field);
		var hz = new HorizontalLayout(question, removeBtn);
		hz.setAlignItems(Alignment.BASELINE);
		add(label, hz);
	}
	
	public void remove() {
		this.removeFromParent();
	}
	
	public WizardField getField() {
		return this.binder.getBean();
	}
}
