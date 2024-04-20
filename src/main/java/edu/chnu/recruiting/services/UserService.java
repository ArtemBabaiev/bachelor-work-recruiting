package edu.chnu.recruiting.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.chnu.recruiting.events.registration.OnRegistrationCompleteEvent;
import edu.chnu.recruiting.exceptions.AlreadyExistsException;
import edu.chnu.recruiting.front.views.auth.registration.SignUpModel;
import edu.chnu.recruiting.models.formModels.RecruiterFormModel;
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

	@Autowired
	private ModelMapper modelMapper;

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

	public User createRecruiter(RecruiterFormModel model) throws AlreadyExistsException {
		if (this.userRepository.existsByUsername(model.getUsername())) {
			throw new AlreadyExistsException("User with such username already in use");
		}
		User user = modelMapper.map(model, User.class);
		user.setPassword(passwordEncoder.encode(model.getPassword()));
		user.setRole(this.roleService.getRoleByName(StarterRoles.RECRUITER.getName()));
		return this.userRepository.save(user);
	}

	public User updateRecruiter(RecruiterFormModel model) {
		User entity = this.userRepository.findById(model.getId()).get();
		if (model.getPassword() != null && !model.getPassword().isBlank()) {
			entity.setPassword(passwordEncoder.encode(model.getPassword()));
		}
		entity.setEnabled(model.getEnabled());
		return this.userRepository.save(entity);
	}
}
