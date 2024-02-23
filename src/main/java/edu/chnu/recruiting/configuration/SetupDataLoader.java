package edu.chnu.recruiting.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import edu.chnu.recruiting.models.security.Role;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.models.security.VerificationToken;
import edu.chnu.recruiting.repositories.RoleRepository;
import edu.chnu.recruiting.repositories.UserRepository;
import edu.chnu.recruiting.repositories.VerificationTokeRepository;
import edu.chnu.recruiting.utils.constants.StarterRoles;
import jakarta.transaction.Transactional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	boolean alreadySetup = false;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private VerificationTokeRepository tokenRepository;

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (alreadySetup)
			return;
		Role adminRole = createRoleIfNotFound(StarterRoles.ADMIN.getName());
		for (StarterRoles role : StarterRoles.values()) {
			createRoleIfNotFound(role.getName());
		}
		 
		User admin = new User();
		admin.setUsername("admin");
		admin.setEmail("test@email.com");
		admin.setPassword("$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW");
		admin.setEnabled(true);
		admin.setRole(adminRole);
		this.userRepository.save(admin);
		VerificationToken token = new VerificationToken();
		token.setUser(admin);
		token.setToken("59291730-3105-4710-9e03-393826c6a68c");
		token.calculateExpiryDate(1440);
		this.tokenRepository.save(token);
		populateUsers();
		alreadySetup = true;
	}

	@Transactional
	Role createRoleIfNotFound(String name) {

		Role role = roleRepository.findByName(name);
		if (role == null) {
			role = new Role(name);
			roleRepository.save(role);
		}
		return role;
	}

	private void populateUsers() {
		var role = this.roleRepository.findByName(StarterRoles.USER.getName());
		for (int i = 0; i < 15; i++) {
			User user = new User();
			user.setUsername("user" + i);
			user.setEmail("test" + i + "@email.com");
			user.setPassword("$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW");
			user.setEnabled(true);
			user.setRole(role);
			this.userRepository.save(user);
		}
		
		for (int i = 0; i < 15; i++) {
			User user = new User();
			user.setUsername("test" + i);
			user.setEmail("test" + i + "@email.com");
			user.setPassword("$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW");
			user.setEnabled(true);
			user.setRole(role);
			this.userRepository.save(user);
		}
	}
}
