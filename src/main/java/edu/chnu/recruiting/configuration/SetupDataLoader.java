package edu.chnu.recruiting.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import edu.chnu.recruiting.models.security.Role;
import edu.chnu.recruiting.repositories.RoleRepository;
import edu.chnu.recruiting.utils.enums.StarterRoles;
import jakarta.transaction.Transactional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	boolean alreadySetup = false;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (alreadySetup)
			return;
		for (StarterRoles role : StarterRoles.values()) {
			createRoleIfNotFound(role.getName());
		}

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
