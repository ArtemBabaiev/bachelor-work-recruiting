package edu.chnu.recruiting.front.components.position;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class OptionComponent extends HorizontalLayout {
	Span option = new Span();
	Button removeBtn = new Button(new Icon(VaadinIcon.CLOSE));

	public OptionComponent(String text) {
		option.setText(text);
		removeBtn.addClickListener(e -> this.removeFromParent());
		this.setAlignItems(Alignment.CENTER);
		add(removeBtn, option);
	}

	public String getValue() {
		return option.getText();
	}
}
