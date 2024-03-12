package edu.chnu.recruiting.front.components.position;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dnd.DropEvent;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;

import edu.chnu.recruiting.front.components.questions.QuestionCard;
import edu.chnu.recruiting.front.components.questions.QuestionComponent;
import edu.chnu.recruiting.models.wizard.WizardStep;

public class SectionComponent extends VerticalLayout implements DropTarget<QuestionCard> {
	Binder<WizardStep> binder = new BeanValidationBinder<WizardStep>(WizardStep.class);
	Div box = new Div();
	WizardStep step = new WizardStep();
	TextField name = new TextField("Section name");
	Button removeBtn = new Button(new Icon(VaadinIcon.CLOSE));

	public SectionComponent() {
		binder.bindInstanceFields(this);
		configureComponent();
		binder.setBean(step);

	}

	private void configureComponent() {
		setActive(true);
		getStyle().set("border", "6px dotted DarkOrange");
		addDropListener(e -> handleDrop(e));

		removeBtn.addClickListener(e -> remove());

		HorizontalLayout hzl = new HorizontalLayout(name, removeBtn);
		hzl.setAlignItems(Alignment.BASELINE);
		add(hzl, box);
	}

	private void handleDrop(DropEvent<QuestionCard> e) {
		var optSource = e.getDragSourceComponent();
		if (optSource.isPresent()) {
			var source = (QuestionCard) optSource.get();
			box.add(source.getQuestionComponent());
		}

	}

	public void remove() {
		this.removeFromParent();
	}

	public WizardStep getStep() {
		return this.binder.getBean();
	}

	public List<QuestionComponent> getQuestionsComponents() {
		return box.getChildren().filter(c -> c instanceof QuestionComponent).map(c -> (QuestionComponent) c).toList();
	}

}
