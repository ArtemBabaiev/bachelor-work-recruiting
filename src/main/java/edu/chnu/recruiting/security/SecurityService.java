package edu.chnu.recruiting.security;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.security.AuthenticationContext;

import edu.chnu.recruiting.exceptions.NoAuthorizationException;
import edu.chnu.recruiting.front.views.HomeView;
import edu.chnu.recruiting.models.security.Role;
import edu.chnu.recruiting.repositories.UserRepository;

@Service
public class SecurityService {
	
	@Autowired
	private AuthenticationContext authContext;
	
	@Autowired
	private UserRepository userRepository;
	
	public void logout() {
		UI.getCurrent().navigate(HomeView.class);
		authContext.logout();
	}
	
	public User getAuthenticatedUser() {
		return this.authContext.getAuthenticatedUser(User.class).orElse(null);
	}
	
	public boolean isAuthenticated() {
		return this.authContext.getAuthenticatedUser(User.class).isPresent();
	}
	

}
