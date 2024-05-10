package edu.chnu.recruiting.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import edu.chnu.recruiting.exceptions.AlreadyExistsException;
import edu.chnu.recruiting.models.formModels.RecruiterFormModel;
import edu.chnu.recruiting.models.security.Role;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.models.security.VerificationToken;
import edu.chnu.recruiting.repositories.UserRepository;
import edu.chnu.recruiting.services.MailService;
import edu.chnu.recruiting.services.RoleService;
import edu.chnu.recruiting.services.UserService;
import edu.chnu.recruiting.services.VerificationTokenService;
import edu.chnu.recruiting.ui.views.auth.registration.SignUpModel;
import edu.chnu.recruiting.utils.enums.StarterRoles;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	@InjectMocks
	UserService userService;
	@Mock
	private RoleService roleService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private ModelMapper modelMapper;

	@Mock
	private VerificationTokenService verTokenService;

	@Mock
	private MailService mailService;

	@Test
	void testRegisterUser() throws AlreadyExistsException {
		SignUpModel model = new SignUpModel();
		model.setEmail("email");
		model.setUsername("user");
		model.setPassword("pass");
		when(this.userRepository.existsByUsernameOrEmail(anyString(), anyString())).thenReturn(false);
		when(this.passwordEncoder.encode(anyString())).thenReturn("encoded");
		when(this.roleService.getRoleByName(StarterRoles.USER.getName())).thenReturn(new Role("ROLE_USER"));

		when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
		when(verTokenService.createVerificationToken(any(User.class), anyString())).thenReturn(new VerificationToken());
		doNothing().when(mailService).sendVerificationEmail(any(VerificationToken.class));

		var returned = userService.registerUser(model);

		assertFalse(returned.getUsername().isBlank());
		assertEquals("encoded", returned.getPassword());
		assertFalse(returned.isEnabled());

	}

	@Test
	void testCreateRecruiter() throws AlreadyExistsException {
		RecruiterFormModel model = new RecruiterFormModel();
		model.setPassword("pass");
		model.setUsername("user");
		when(this.userRepository.existsByUsername(anyString())).thenReturn(false);
		when(modelMapper.map(model, User.class)).thenReturn(new User());
		when(this.passwordEncoder.encode(anyString())).thenReturn("encoded");
		when(this.roleService.getRoleByName(anyString())).thenReturn(new Role("ROLE_RECRUITER"));
		when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

		var user = userService.createRecruiter(model);

		assertEquals(StarterRoles.RECRUITER.getName(), user.getRole().getName());
		assertEquals("encoded", user.getPassword());
	}

	@Test
	void testUpdateRecruiter() throws AlreadyExistsException {
		RecruiterFormModel model = new RecruiterFormModel();
		model.setUsername("user");
		model.setEnabled(true);

		when(this.userRepository.findById(model.getId())).then(invocation -> {
			var user = new User();
			user.setPassword("old_password");
			user.setEnabled(false);
			return Optional.of(user);
		});
		when(this.passwordEncoder.encode(anyString())).thenReturn("new_encoded");
		when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

		var user = userService.updateRecruiter(model);

		assertEquals("old_password", user.getPassword());
		assertEquals(model.getEnabled(), user.isEnabled());

		model.setPassword("pass");
		model.setEnabled(true);
		user = userService.updateRecruiter(model);

		assertEquals("new_encoded", user.getPassword());
		assertEquals(model.getEnabled(), user.isEnabled());

	}
}
