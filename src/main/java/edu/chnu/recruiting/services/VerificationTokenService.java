package edu.chnu.recruiting.services;

import java.time.LocalDateTime;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.chnu.recruiting.exceptions.VerificationTokenExpiredException;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.models.security.VerificationToken;
import edu.chnu.recruiting.repositories.VerificationTokeRepository;

@Service
public class VerificationTokenService {
	@Autowired
	private VerificationTokeRepository tokenRepository;
	
	@Autowired
	private UserService userService;
	
	@Value("${verifaction-token.expiration: 1440}")
	private int tokenExpiration;
	
	public void createVerificationToken(User user, String token) {
		VerificationToken vToken = new VerificationToken();
		vToken.setToken(token);
		vToken.setUser(user);
		vToken.calculateExpiryDate(tokenExpiration);
		tokenRepository.save(vToken);
		
	}
	
	public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }
	
	public User confirmRegistration(String token) {
		VerificationToken verificationToken = this.getVerificationToken(token);
		User user = verificationToken.getUser();
		final Calendar cal = Calendar.getInstance();
		if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			tokenRepository.delete(verificationToken);
			 throw new VerificationTokenExpiredException();
		} 
	    user.setEnabled(true); 
	    return userService.updateUser(user);
	}
}
