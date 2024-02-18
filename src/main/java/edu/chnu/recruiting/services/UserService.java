package edu.chnu.recruiting.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.chnu.recruiting.exceptions.AlreadyExistsException;
import edu.chnu.recruiting.front.views.auth.RegisterModel;
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

	public User registerUser(RegisterModel model) {

		if (this.userRepository.existsByUsernameOrEmail(model.getUsername(), model.getEmail())) {
			throw new AlreadyExistsException();
		}

		User u = new User();
		u.setEmail(model.getEmail());
		u.setEnabled(true);
		u.setPassword(this.passwordEncoder.encode(model.getPassword()));
		u.setUsername(model.getUsername());
		u.setRoles(Set.of(this.roleService.getRoleByName(StarterRoles.APPLICANT)));

		return this.userRepository.save(u);
	}
}
