package edu.chnu.recruiting.front.views.registration;

import org.springframework.security.core.userdetails.User;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;

import edu.chnu.recruiting.front.layouts.MainLayout;
import jakarta.annotation.security.PermitAll;

@PageTitle("TEST")
@Route(value = "test",layout = MainLayout.class)
@PermitAll
public class TestView extends Div{
	public TestView(AuthenticationContext ctx) {
		var test = ctx.getAuthenticatedUser(User.class);
		System.out.println();
	}
}
