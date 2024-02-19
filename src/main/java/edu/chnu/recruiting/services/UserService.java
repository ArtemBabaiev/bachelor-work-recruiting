package edu.chnu.recruiting.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.chnu.recruiting.events.registration.OnRegistrationCompleteEvent;
import edu.chnu.recruiting.exceptions.AlreadyExistsException;
import edu.chnu.recruiting.front.views.registration.SignUpModel;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.repositories.UserRepository;
import edu.chnu.recruiting.utils.constants.StarterRoles;

@Service
public class UserService {

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	public User registerUser(SignUpModel model) {
		if (this.userRepository.existsByUsernameOrEmail(model.getUsername(), model.getEmail())) {
			throw new AlreadyExistsException();
		}

		User user = new User();
		user.setEmail(model.getEmail());
		user.setEnabled(false);
		user.setPassword(this.passwordEncoder.encode(model.getPassword()));
		user.setUsername(model.getUsername());
		user.setRoles(Set.of(this.roleService.getRoleByName(StarterRoles.APPLICANT)));

		user = this.userRepository.save(user);
		
		eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user));
		return user;
	}
	
	public User updateUser(User user) {
		return this.userRepository.save(user);
	}
}
