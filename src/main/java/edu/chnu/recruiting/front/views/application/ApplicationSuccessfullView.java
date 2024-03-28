package edu.chnu.recruiting.front.views.application;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;

import edu.chnu.recruiting.front.layouts.MainLayout;
import jakarta.annotation.security.PermitAll;

@Route(value = "Application Successfull", layout = MainLayout.class)
@PageTitle("application-success")
@PermitAll
public class ApplicationSuccessfullView extends VerticalLayout {
	public ApplicationSuccessfullView() {
		setHeightFull();
		addClassNames(Display.FLEX, JustifyContent.CENTER, AlignItems.CENTER);
		H2 message = new H2("Application was saved successfully");
		add(message);
	}
}
