package edu.chnu.recruiting.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.security.AuthenticationContext;

import edu.chnu.recruiting.front.views.HomeView;

@Service
public class SecurityService {
	
	@Autowired
	private AuthenticationContext authContext;
	
	public void logout() {
		UI.getCurrent().navigate(HomeView.class);
		authContext.logout();
	}
	
	public User getAuthenticatedUser() {
		return this.authContext.getAuthenticatedUser(User.class).orElse(null);
	}
	
	public boolean isAuthenticated() {
		return this.getAuthenticatedUser() != null;
	}
}
