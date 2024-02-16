package edu.chnu.recruiting.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import edu.chnu.recruiting.models.security.Role;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.repositories.RoleRepository;
import edu.chnu.recruiting.repositories.UserRepository;
import edu.chnu.recruiting.utils.constants.StarterRoles;
import jakarta.transaction.Transactional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	boolean alreadySetup = false;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (alreadySetup)
			return;
		createRoleIfNotFound(StarterRoles.COMPANY);
		createRoleIfNotFound(StarterRoles.APPLICANT);
		Role adminRole = createRoleIfNotFound(StarterRoles.ADMIN);
		User admin = new User();
		admin.setEmail("test@email.com");
		admin.setPassword("$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW");
		admin.setEnabled(true);
		admin.getRoles().add(adminRole);
		this.userRepository.save(admin);
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
}
