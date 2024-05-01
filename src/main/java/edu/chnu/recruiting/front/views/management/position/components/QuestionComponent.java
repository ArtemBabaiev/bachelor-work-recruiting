package edu.chnu.recruiting.front.views.management.position.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageInputI18n;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.LumoUtility;

import edu.chnu.recruiting.models.wizard.ValueType;
import edu.chnu.recruiting.models.wizard.WizardField;

public class QuestionComponent extends VerticalLayout {
	Binder<WizardField> binder = new BeanValidationBinder<WizardField>(WizardField.class);
	WizardField field;

	ComboBox<ValueType> type = new ComboBox<>("Type");
	TextField question = new TextField("Question");
	Checkbox required = new Checkbox("Required");

	VirtualList<String> optionsList = new VirtualList<>();
	MessageInput optionInput = new MessageInput();

	Checkbox textToSpeech = new Checkbox("Use Text-to-Speech");

	HorizontalLayout controls = new HorizontalLayout();
	VerticalLayout info = new VerticalLayout();
	VerticalLayout extra = new VerticalLayout();

	Button upBtn = new Button(new Icon(VaadinIcon.ARROW_UP));
	Button downBtn = new Button(new Icon(VaadinIcon.ARROW_DOWN));
	Button closeBtn = new Button(new Icon(VaadinIcon.CLOSE));

	public QuestionComponent(WizardField field) {
		this.field = field;
		field.setType(field.getType());
		binder.bindInstanceFields(this);
		addClassNames(LumoUtility.Background.BASE, LumoUtility.BorderRadius.LARGE);
		configureComponent();
		binder.setBean(field);

		configureDisplayed();
	}

	private void configureComponent() {
		type.addValueChangeListener(e -> setExtra(e.getOldValue(), e.getValue()));

		closeBtn.addClickListener(e -> remove());
		upBtn.addClickListener(e -> handleUpClick(e));
		downBtn.addClickListener(e -> handleDownClick(e));
		question.setValueChangeMode(ValueChangeMode.EAGER);

		type.setItems(ValueType.values());
		type.setItemLabelGenerator(v -> v.getLabel());
		type.setRenderer(new ComponentRenderer<Component, ValueType>(v -> {
			var icon = v.getIcon().create();
			icon.getStyle().set("padding", "var(--lumo-space-xs)");
			return new Span(icon, new Span(v.getLabel()));
		}));
		MessageInputI18n ms = new MessageInputI18n();
		ms.setMessage("Option");
		ms.setSend("Add");
		optionInput.setI18n(ms);
		optionInput.setClassName(LumoUtility.Padding.NONE);
		optionInput.addSubmitListener(e -> {
			addOption(e.getValue());
		});
		optionsList.setHeight("150px");
		optionsList.setWidthFull();
		optionsList.setRenderer(new ComponentRenderer<>(option -> {
			HorizontalLayout row = new HorizontalLayout();
			row.setAlignItems(Alignment.BASELINE);

			Span opt = new Span(option);
			Button removeBtn = new Button(new Icon(VaadinIcon.CLOSE));
			removeBtn.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
			removeBtn.addClickListener(e -> removeOption(option));
			row.add(removeBtn, opt);
			row.addClassNames(LumoUtility.Border.BOTTOM);
			return row;
		}));
	}

	private void configureDisplayed() {
		this.setSpacing(false);
		this.addClassNames(LumoUtility.Padding.NONE, LumoUtility.Margin.NONE);

		controls.addClassNames(LumoUtility.Padding.NONE, LumoUtility.Margin.NONE);
		controls.setWidthFull();
		controls.setJustifyContentMode(JustifyContentMode.END);
		controls.add(downBtn, upBtn, closeBtn);
		controls.setSpacing(false);

		closeBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR,
				ButtonVariant.LUMO_TERTIARY);
		upBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
		downBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);

		HorizontalLayout hzl = new HorizontalLayout(question, type);
		hzl.addClassNames(LumoUtility.Padding.NONE, LumoUtility.Margin.NONE);
		hzl.setWidthFull();
		// hzl.expand(question);
		hzl.setFlexGrow(2, question);
		hzl.setFlexGrow(1, type);

		info.addClassNames(LumoUtility.Padding.Top.NONE);
		info.add(hzl, required);

		extra.addClassNames(LumoUtility.Padding.Top.NONE);

		add(controls, info, extra);
	}

	private void setExtra(ValueType oldValue, ValueType newValue) {
		extra.removeAll();
		if (oldValue != null) {
			binder.getBean().resetOptional();
		}
		updateOptions();
		switch (binder.getBean().getType()) {
		case SELECTION_MULTIPLE:
		case SELECTION_SINGLE:
			extra.add(new Text("Options"), optionInput, optionsList);
			break;
		case AUDIO:
			extra.add(textToSpeech);
			break;
		default:
			break;
		}
	}

	public void remove() {
		this.removeFromParent();
	}

	private void handleUpClick(ClickEvent<Button> e) {
		fireEvent(new UpQuestionEvent(this));
	}

	private void handleDownClick(ClickEvent<Button> e) {
		fireEvent(new DownQuestionEvent(this));
	}

	private void removeOption(String option) {
		binder.getBean().getOptions().remove(option);
		updateOptions();
	}

	private void addOption(String option) {
		binder.getBean().addOption(option);
		updateOptions();
	}

	private void updateOptions() {
		optionsList.setItems(binder.getBean().getOptions());
	}

	public WizardField getField() {
		return this.binder.getBean();
	}

	public Registration addUpListener(ComponentEventListener<UpQuestionEvent> listener) {
		return addListener(UpQuestionEvent.class, listener);
	}

	public Registration addDownListener(ComponentEventListener<DownQuestionEvent> listener) {
		return addListener(DownQuestionEvent.class, listener);
	}

	public static abstract class MoveQuestionEvent extends ComponentEvent<QuestionComponent> {
		protected MoveQuestionEvent(QuestionComponent source) {
			super(source, false);
		}
	}

	public static class UpQuestionEvent extends MoveQuestionEvent {

		UpQuestionEvent(QuestionComponent source) {
			super(source);
		}
	}

	public static class DownQuestionEvent extends MoveQuestionEvent {

		DownQuestionEvent(QuestionComponent source) {
			super(source);
		}
	}
}
