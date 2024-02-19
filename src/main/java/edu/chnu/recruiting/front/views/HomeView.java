package edu.chnu.recruiting.front.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import edu.chnu.recruiting.front.layouts.MainLayout;

@PageTitle("Home")
@Route(value = "",layout = MainLayout.class)
@RouteAlias(value = "home", layout = MainLayout.class)
@AnonymousAllowed
public class HomeView extends VerticalLayout{
	 public HomeView() {
	        addClassName("home-view"); 
	        setSizeFull();
	        add(new H1("WELCOME HOME")); 
	    }
}
