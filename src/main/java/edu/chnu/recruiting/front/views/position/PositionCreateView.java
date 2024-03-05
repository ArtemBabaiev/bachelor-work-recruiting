package edu.chnu.recruiting.front.views.position;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import edu.chnu.recruiting.front.components.position.FieldsToolbar;
import edu.chnu.recruiting.front.components.position.FormСreationComponent;
import edu.chnu.recruiting.front.components.position.SectionComponent;
import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.models.wizard.WizardField;
import edu.chnu.recruiting.services.PositionService;

@PageTitle("Create Position")
@Route(value = "position-create", layout = MainLayout.class)
@AnonymousAllowed
public class PositionCreateView extends VerticalLayout {
	Binder<Position> binder = new BeanValidationBinder<Position>(Position.class);
	Position model = new Position();
	
	TextField name = new TextField("Position name");
	
	FieldsToolbar toolbar = new FieldsToolbar();
	FormСreationComponent form = new FormСreationComponent();
	
	Button addSectionBtn = new Button("Add section");
	Button createPositionBtn = new Button("Create position");

	PositionService positionService;
	
	public PositionCreateView(PositionService positionService) {
		this.positionService = positionService;
		binder.bindInstanceFields(this);
		HorizontalLayout content = new HorizontalLayout(form, toolbar);
		toolbar.setWidth("350px");
        content.addClassNames("content");
        content.setSizeFull();
		add(new HorizontalLayout(addSectionBtn, createPositionBtn), name, content);
		name.setValueChangeMode(ValueChangeMode.EAGER);
		addSectionBtn.addClickListener(e -> form.add(new SectionComponent()));
		createPositionBtn.addClickListener(e -> this.positionService.createPosition(binder.getBean(), form));
		binder.setBean(model);
		
		form.getChildren().filter(child -> child instanceof SectionComponent);
	}
}
