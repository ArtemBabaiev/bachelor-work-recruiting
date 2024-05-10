package edu.chnu.recruiting.unit;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.chnu.recruiting.exceptions.TokenInvalidException;
import edu.chnu.recruiting.exceptions.VerificationTokenExpiredException;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.models.security.VerificationToken;
import edu.chnu.recruiting.repositories.UserRepository;
import edu.chnu.recruiting.repositories.VerificationTokeRepository;
import edu.chnu.recruiting.services.MailService;
import edu.chnu.recruiting.services.VerificationTokenService;

@ExtendWith(MockitoExtension.class)
public class VerificationTokenServiceTest {

	@InjectMocks
	VerificationTokenService tokenService;

	@Mock
	private VerificationTokeRepository tokenRepository;

	@Mock
	private UserRepository userReporsitory;

	@Mock
	private MailService mailService;

	@Test
	void testCreateToken() {
		User user = new User();

		when(tokenRepository.save(any(VerificationToken.class))).then(invocation -> invocation.getArguments()[0]);
		var token = tokenService.createVerificationToken(user, "12345-6789");
		assertNotNull(token.getUser());
		assertNotNull(token.getExpiryDate());
		assertNotNull(token.getToken());
	}

	@Test
	void testConfirmRegistration() throws TokenInvalidException, VerificationTokenExpiredException {
		User user = new User();
		VerificationToken token = new VerificationToken();
		token.setExpiryDate(LocalDateTime.now().plusHours(3));
		token.setUser(user);

		when(tokenRepository.findByToken(anyString())).thenReturn(token);
		doNothing().when(tokenRepository).delete(any(VerificationToken.class));
		when(userReporsitory.save(any(User.class))).then(invocation -> invocation.getArguments()[0]);

		var returned = tokenService.confirmRegistration("12345-6789");

		assertTrue(returned.isEnabled());

		token.setExpiryDate(LocalDateTime.now().minusHours(6));

		assertThrows(VerificationTokenExpiredException.class, () -> tokenService.confirmRegistration("12345-6789"));
	}

	@Test
	void testGenerateAndSendNewVerificationToken() {
		VerificationToken token = new VerificationToken();
		token.setToken("123456");

		when(tokenRepository.findByToken(anyString())).thenReturn(token);
		when(tokenRepository.save(any(VerificationToken.class))).then(invocation -> invocation.getArguments()[0]);
		doNothing().when(this.mailService).sendVerificationEmail(any(VerificationToken.class));

		var newToken = tokenService.generateAndSendNewVerificationToken("123456");

		assertNotEquals("123456", newToken.getToken());
	}
}
