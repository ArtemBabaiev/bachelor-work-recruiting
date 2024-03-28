package edu.chnu.recruiting.front.components.position;

import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.shared.Registration;

import edu.chnu.recruiting.front.components.questions.QuestionCard;
import edu.chnu.recruiting.front.components.questions.QuestionComponent;
import edu.chnu.recruiting.front.components.questions.QuestionComponent.DownQuestionEvent;
import edu.chnu.recruiting.front.components.questions.QuestionComponent.UpQuestionEvent;
import edu.chnu.recruiting.models.wizard.WizardStep;

public class SectionComponent extends VerticalLayout implements DropTarget<QuestionCard> {
	Binder<WizardStep> binder = new BeanValidationBinder<WizardStep>(WizardStep.class);
	Div box = new Div();
	WizardStep step = new WizardStep();
	TextField name = new TextField("Section name");
	HorizontalLayout controls;
	
	Button closeBtn = new Button(new Icon(VaadinIcon.CLOSE));
	Button upBtn = new Button(new Icon(VaadinIcon.ARROW_UP));
	Button downBtn = new Button(new Icon(VaadinIcon.ARROW_DOWN));
	public SectionComponent() {
		binder.bindInstanceFields(this);
		configureComponent();
		binder.setBean(step);

	}

	private void configureComponent() {
		setActive(true);
		getStyle().set("border", "6px dotted DarkOrange");
		addDropListener(e -> handleDrop(e));
		this.controls = this.getControls();
		closeBtn.addClickListener(e -> remove());
		upBtn.addClickListener(e -> fireEvent(new UpSectionEvent(this)));
		downBtn.addClickListener(e -> fireEvent(new DownSectionEvent(this)));
		
		HorizontalLayout hzl = new HorizontalLayout(name, controls);
		hzl.setWidthFull();
		hzl.expand(name);
		hzl.setAlignItems(Alignment.BASELINE);
		add(hzl, box);
	}
	
	private HorizontalLayout getControls() {
		HorizontalLayout controls = new HorizontalLayout();
		closeBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);
		upBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);
		downBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);
		controls.add(downBtn, upBtn, closeBtn);
		return controls;
	}

	private void handleDrop(DropEvent<QuestionCard> e) {
		var optSource = e.getDragSourceComponent();
		if (optSource.isPresent()) {
			var source = (QuestionCard) optSource.get();
			var qc = source.getQuestionComponent();
			qc.addUpListener(eUp -> handleUpClick(eUp));
			qc.addDownListener(eDown -> handleDownEvent(eDown));
			box.add(qc);
		}

	}

	private void handleDownEvent(DownQuestionEvent e) {
		var children = box.getChildren().collect(Collectors.toList());
		int size = children.size();
		int index = children.indexOf(e.getSource());
		if (index == size - 1 || index == -1) {
			return;
		}
		children.remove(index);
		children.add(index + 1, e.getSource());
		box.removeAll();
		box.add(children);
		return;
	}

	private void handleUpClick(UpQuestionEvent e) {
		var children = box.getChildren().collect(Collectors.toList());
		int index = children.indexOf(e.getSource());
		if (index == 0 || index == -1) {
			return;
		}
		children.remove(index);
		children.add(index - 1, e.getSource());
		box.removeAll();
		box.add(children);
		return;
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
	
	public Registration addUpListener(ComponentEventListener<UpSectionEvent> listener) {
		return addListener(UpSectionEvent.class, listener);
	}

	public Registration addDownListener(ComponentEventListener<DownSectionEvent> listener) {
		return addListener(DownSectionEvent.class, listener);
	}
	
	public static abstract class MoveSectionEvent extends ComponentEvent<SectionComponent> {
		protected MoveSectionEvent(SectionComponent source) {
			super(source, false);
		}
	}

	public static class UpSectionEvent extends MoveSectionEvent {

		UpSectionEvent(SectionComponent source) {
			super(source);
		}
	}

	public static class DownSectionEvent extends MoveSectionEvent {

		DownSectionEvent(SectionComponent source) {
			super(source);
		}
	}

}
