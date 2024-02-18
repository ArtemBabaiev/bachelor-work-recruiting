package edu.chnu.recruiting.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.chnu.recruiting.OnRegistrationCompleteEvent;
import edu.chnu.recruiting.exceptions.AlreadyExistsException;
import edu.chnu.recruiting.front.views.registration.RegisterModel;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.models.security.VerificationToken;
import edu.chnu.recruiting.repositories.UserRepository;
import edu.chnu.recruiting.repositories.VerificationTokeRepository;
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
	
	public User registerUser(RegisterModel model) {
		if (this.userRepository.existsByUsernameOrEmail(model.getUsername(), model.getEmail())) {
			throw new AlreadyExistsException();
		}

		User user = new User();
		user.setEmail(model.getEmail());
		user.setEnabled(true);
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
