package edu.chnu.recruiting.ui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import edu.chnu.recruiting.configuration.ConfigServerTest;
import edu.chnu.recruiting.ui.MainLayout;

@PageTitle("Home")
@Route(value = "",layout = MainLayout.class)
@RouteAlias(value = "home", layout = MainLayout.class)
@AnonymousAllowed
public class HomeView extends VerticalLayout{
	 public HomeView(ConfigServerTest cst) {
	        addClassName("home-view"); 
	        setSizeFull();
	        add(new H1("WELCOME HOME")); 
	        add(new Button("Click me", e -> {
	        	Notification.show(cst.getTest());
	        }));
	    }
}
