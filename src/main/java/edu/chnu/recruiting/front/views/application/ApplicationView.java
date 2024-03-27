package edu.chnu.recruiting.front.views.application;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import edu.chnu.recruiting.front.layouts.MainLayout;
import edu.chnu.recruiting.services.UnitOfWork;

@PageTitle("Application")
@Route(value = "application/:id", layout = MainLayout.class)
@AnonymousAllowed
public class ApplicationView  extends VerticalLayout implements BeforeEnterObserver {
	String uuid;
	
	public ApplicationView(UnitOfWork uow) {
		
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		this.uuid = event.getRouteParameters().get("id").get();
		add(new H2(this.uuid));
	}
}
