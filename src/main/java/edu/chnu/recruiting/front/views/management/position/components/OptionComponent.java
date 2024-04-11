package edu.chnu.recruiting.front.views.management.position.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;

public class OptionComponent extends HorizontalLayout {
	Span option = new Span();
	Button removeBtn = new Button(new Icon(VaadinIcon.CLOSE));

	public OptionComponent(String text) {
		option.setText(text);
		this.setAlignItems(Alignment.CENTER);
		add(removeBtn, option);
	}

	public String getValue() {
		return option.getText();
	}
	
	public Registration setRemoveClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
		return removeBtn.addClickListener(listener);
	}
}
