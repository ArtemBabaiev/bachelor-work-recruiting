package edu.chnu.recruiting.front.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import edu.chnu.recruiting.front.components.layouts.MainLayout;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Home")
@Route(value = "",layout = MainLayout.class)
@RouteAlias(value = "home", layout = MainLayout.class)
@PermitAll
public class HomeView extends VerticalLayout{
	 public HomeView() {
	        addClassName("home-view"); 
	        setSizeFull();
	        add(new H1("WELCOME HOME")); 
	    }
}
