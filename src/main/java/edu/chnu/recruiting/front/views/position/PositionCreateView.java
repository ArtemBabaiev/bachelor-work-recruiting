package edu.chnu.recruiting.front.views.position;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import edu.chnu.recruiting.front.components.position.FieldsToolbar;
import edu.chnu.recruiting.front.components.position.FormComponent;
import edu.chnu.recruiting.front.components.position.SectionComponent;
import edu.chnu.recruiting.front.layouts.MainLayout;

@PageTitle("Create Position")
@Route(value = "position-create", layout = MainLayout.class)
@AnonymousAllowed
public class PositionCreateView extends VerticalLayout {
	FieldsToolbar toolbar = new FieldsToolbar();
	TextField positionName = new TextField("Position name");
	FormComponent form = new FormComponent();
	Button addSectionBtn = new Button("Add section");
	Button createPositionBtn = new Button("Create position");

	public PositionCreateView() {
		HorizontalLayout content = new HorizontalLayout(form, toolbar);
		toolbar.setWidth("350px");
        content.addClassNames("content");
        content.setSizeFull();
		add(new HorizontalLayout(addSectionBtn, createPositionBtn), positionName, content);
		addSectionBtn.addClickListener(e -> form.add(new SectionComponent()));
		
		form.getChildren().filter(child -> child instanceof SectionComponent);
	}
}
