package edu.chnu.recruiting.front.components.position;

import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class FormComponent extends VerticalLayout {
	public FormComponent() {
		add(new SectionComponent());
	}
}
