package edu.chnu.recruiting.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.flow.spring.security.AuthenticationContext;

import edu.chnu.recruiting.exceptions.NoAuthorizationException;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.repositories.UserRepository;

@Service
public class SecurityContext {

	@Autowired
	private AuthenticationContext authContext;

	@Autowired
	private UserRepository userRepository;

	public void logout() {
		authContext.logout();
	}

	public Optional<org.springframework.security.core.userdetails.User> getContextUser() {
		return this.authContext.getAuthenticatedUser(org.springframework.security.core.userdetails.User.class);
	}

	public boolean isAuthenticated() {
		return this.authContext.isAuthenticated();
	}

	public User getAuthenticatedUser() {
		var optUser = this.getContextUser();
		if (optUser.isEmpty()) {
			return null;
		}
		return this.userRepository.findByUsername(optUser.get().getUsername()).orElseThrow(() -> new NoAuthorizationException());
	}

}
