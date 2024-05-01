package edu.chnu.recruiting.front.views.management.position.components;

import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.LumoUtility;

import edu.chnu.recruiting.front.views.management.position.components.QuestionComponent.DownQuestionEvent;
import edu.chnu.recruiting.front.views.management.position.components.QuestionComponent.UpQuestionEvent;
import edu.chnu.recruiting.models.wizard.WizardField;
import edu.chnu.recruiting.models.wizard.WizardStep;

public class SectionComponent extends VerticalLayout {
	Binder<WizardStep> binder = new BeanValidationBinder<WizardStep>(WizardStep.class);
	VerticalLayout box = new VerticalLayout();
	WizardStep step;
	TextField name = new TextField("Section name");

	Button addQuestionBtn = new Button("Add question");

	Button closeBtn = new Button(new Icon(VaadinIcon.CLOSE));
	Button upBtn = new Button(new Icon(VaadinIcon.ARROW_UP));
	Button downBtn = new Button(new Icon(VaadinIcon.ARROW_DOWN));
	
	public SectionComponent(WizardStep step) {
		this.step = step;
		binder.bindInstanceFields(this);

		this.setWidthFull();
		this.addClassNames(LumoUtility.Background.CONTRAST_5, LumoUtility.BorderRadius.LARGE, LumoUtility.Padding.NONE);
		this.setSpacing(false);

		HorizontalLayout controls = new HorizontalLayout();
		controls.addClassNames(LumoUtility.Padding.NONE, LumoUtility.Margin.NONE);
		controls.setWidthFull();
		controls.setJustifyContentMode(JustifyContentMode.END);
		controls.add(downBtn, upBtn, closeBtn);
		controls.setSpacing(false);

		closeBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR,
				ButtonVariant.LUMO_TERTIARY);
		upBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
		downBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);

		closeBtn.addClickListener(e -> remove());
		upBtn.addClickListener(e -> fireEvent(new UpSectionEvent(this)));
		downBtn.addClickListener(e -> fireEvent(new DownSectionEvent(this)));
		addQuestionBtn.addClickListener(e -> handleCreateQuestionClick(e));

		VerticalLayout content = new VerticalLayout(name, box, addQuestionBtn);
		content.addClassNames(LumoUtility.Padding.Top.NONE);
		content.setSizeFull();
		content.setAlignItems(Alignment.CENTER);

		name.setWidthFull();
		box.setWidthFull();
		box.addClassNames(LumoUtility.Padding.NONE, LumoUtility.Margin.NONE);

		binder.setBean(step);

		add(controls, content);
		if (!this.step.getFields().isEmpty()) {
			for (var field : this.step.getFields()) {
				QuestionComponent qst = new QuestionComponent(field);
				qst.addUpListener(eUp -> handleUpClick(eUp));
				qst.addDownListener(eDown -> handleDownEvent(eDown));

				box.add(qst);
			}
		}

	}

	private void handleCreateQuestionClick(ClickEvent<Button> e) {
		QuestionComponent qst = new QuestionComponent(new WizardField());
		qst.addUpListener(eUp -> handleUpClick(eUp));
		qst.addDownListener(eDown -> handleDownEvent(eDown));

		box.add(qst);
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
