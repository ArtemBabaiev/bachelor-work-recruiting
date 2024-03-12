package edu.chnu.recruiting.front.components.questions;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.theme.lumo.LumoUtility;

import edu.chnu.recruiting.models.wizard.ValueType;
import edu.chnu.recruiting.models.wizard.WizardField;

public class QuestionComponent extends VerticalLayout {
	Binder<WizardField> binder = new BeanValidationBinder<WizardField>(WizardField.class);
	WizardField field = new WizardField();

	ComboBox<ValueType> type = new ComboBox<>("Type");
	TextField question = new TextField("Question");
	Checkbox required = new Checkbox("Required");

	ListBox<String> optionsList = new ListBox<>();
	TextField optionField = new TextField();
	Button addOptionButton = new Button("Add");

	// TODO: no bind
	Checkbox isTextToSpeech = new Checkbox("Use Text-to-Speech");

	HorizontalLayout header = new HorizontalLayout();
	VerticalLayout info = new VerticalLayout();
	VerticalLayout extra = new VerticalLayout();

	Button upBtn = new Button(new Icon(VaadinIcon.ARROW_UP));
	Button downBtn = new Button(new Icon(VaadinIcon.ARROW_DOWN));
	Button closeBtn = new Button(new Icon(VaadinIcon.CLOSE));

	public QuestionComponent(ValueType type) {
		field.setType(type);
		binder.bindInstanceFields(this);
		getStyle().set("border", "solid");
		configureComponent();
		binder.setBean(field);
		setExtra();
		add(configureAndGetBox());
	}

	private void configureComponent() {
		type.addValueChangeListener(e -> setExtra());
		
		closeBtn.addClickListener(e -> remove());
		question.setValueChangeMode(ValueChangeMode.EAGER);
		question.setWidth("315px");
		
		type.setItems(ValueType.values());
		type.setItemLabelGenerator(v -> v.toString());
		
		addOptionButton.addClickListener(click -> {
			addOption(optionField.getValue());
			optionField.clear();
		});
		
		optionsList.setReadOnly(true);
		optionsList.setRenderer(new ComponentRenderer<>(option -> {
		    HorizontalLayout row = new HorizontalLayout();
		    row.setAlignItems(Alignment.BASELINE);
		    
		    Span opt = new Span(option);
		    Button removeBtn = new Button(new Icon(VaadinIcon.CLOSE));
		    removeBtn.addClickListener(e -> removeOption(option));
		    row.add(opt, removeBtn);
		    return row;
		}));
	}

	private Component configureAndGetBox() {
		VerticalLayout box = new VerticalLayout();
		header.add(type, getControls());
		header.setAlignItems(Alignment.BASELINE);
		header.setJustifyContentMode(JustifyContentMode.BETWEEN);

		info.add(question, required);

		this.removeSpacing(box, header, info, extra);
		this.setFullSize(box, header, info, extra);

		box.add(header, info, extra);

		return box;
	}

	private Component getControls() {
		HorizontalLayout controls = new HorizontalLayout();
		closeBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);
		upBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);
		downBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);
		controls.add(downBtn, upBtn, closeBtn);
		return controls;
	}

	private void setExtra() {
		extra.removeAll();
		binder.getBean().resetOptional();
		updateOptions();
		switch (binder.getBean().getType()) {
		case SELECTION_MULTIPLE:
		case SELECTION_SINGLE:
			extra.add(new Text("Options"), new HorizontalLayout(optionField, addOptionButton), optionsList);
			break;
		case AUDIO:
			extra.add(isTextToSpeech);
			break;
		}
	}

	private void removeSpacing(HasStyle... components) {
		for (HasStyle hasStyle : components) {
			hasStyle.addClassNames(LumoUtility.Padding.NONE);
			hasStyle.addClassNames(LumoUtility.Margin.NONE);
		}
	}

	private void setFullSize(HasSize... components) {
		for (HasSize has : components) {
			has.setSizeFull();
		}
	}

	public void remove() {
		this.removeFromParent();
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
}
