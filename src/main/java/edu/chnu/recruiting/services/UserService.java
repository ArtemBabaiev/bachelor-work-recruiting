package edu.chnu.recruiting.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.chnu.recruiting.events.registration.OnRegistrationCompleteEvent;
import edu.chnu.recruiting.exceptions.AlreadyExistsException;
import edu.chnu.recruiting.front.views.auth.registration.SignUpModel;
import edu.chnu.recruiting.models.security.Role;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.repositories.UserRepository;
import edu.chnu.recruiting.utils.enums.StarterRoles;

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

	public User registerUser(SignUpModel model) throws AlreadyExistsException {
		if (this.userRepository.existsByUsernameOrEmail(model.getUsername(), model.getEmail())) {
			throw new AlreadyExistsException("User with such email/username already in use");
		}

		User user = new User();
		user.setEmail(model.getEmail());
		user.setEnabled(false);
		user.setPassword(this.passwordEncoder.encode(model.getPassword()));
		user.setUsername(model.getUsername());
		user.setRole(this.roleService.getRoleByName(StarterRoles.USER.getName()));

		user = this.userRepository.save(user);

		eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user));
		return user;
	}

	public User updateUser(User user) {
		return this.userRepository.save(user);
	}

	public List<User> getAllUsers() {
		return this.userRepository.findAll();
	}

	public List<User> searchPaginated(String username, PageRequest pageRequest) {
		Role userRole = this.roleService.getRoleByName(StarterRoles.USER.getName());
		var res = this.userRepository.findAllUsernameLikeAndRoleIs(username, userRole.getId(), pageRequest);
		return res;
	}
}
