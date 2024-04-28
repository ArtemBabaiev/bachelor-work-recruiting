package edu.chnu.recruiting.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.chnu.recruiting.exceptions.TokenInvalidException;
import edu.chnu.recruiting.exceptions.VerificationTokenExpiredException;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.models.security.VerificationToken;
import edu.chnu.recruiting.repositories.UserRepository;
import edu.chnu.recruiting.repositories.VerificationTokeRepository;

@Service
public class VerificationTokenService {
	@Autowired
	private VerificationTokeRepository tokenRepository;

	@Autowired
	private UserRepository userReporsitory;

	@Autowired
	private MailService mailService;

	@Value("${base-url}")
	private String contextPath;

	@Value("${verifaction-token.expiration: 1440}")
	private int tokenExpiration;

	public VerificationToken createVerificationToken(User user, String token) {
		VerificationToken vToken = new VerificationToken();
		vToken.setToken(token);
		vToken.setUser(user);
		vToken.calculateExpiryDate(tokenExpiration);
		return tokenRepository.save(vToken);

	}

	public VerificationToken getVerificationToken(String VerificationToken) {
		return tokenRepository.findByToken(VerificationToken);
	}

	public User confirmRegistration(String token) throws TokenInvalidException, VerificationTokenExpiredException {
		VerificationToken verificationToken = this.getVerificationToken(token);

		if (verificationToken == null) {
			throw new TokenInvalidException();
		}
		LocalDateTime now = LocalDateTime.now();
		if ((verificationToken.getExpiryDate().isBefore(now))) {
			throw new VerificationTokenExpiredException();
		}
		User user = verificationToken.getUser();
		user.setEnabled(true);
		tokenRepository.delete(verificationToken);
		return userReporsitory.save(user);
	}

	public VerificationToken generateAndSendNewVerificationToken(String existingToken) {
		VerificationToken vToken = this.getVerificationToken(existingToken);
		vToken.updateToken(UUID.randomUUID().toString(), this.tokenExpiration);
		vToken = tokenRepository.save(vToken);

		this.mailService.sendVerificationEmail(vToken.getUser().getEmail(), vToken.getToken());

		return vToken;
	}
}
