package edu.chnu.recruiting.models.security;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@Entity
public class VerificationToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String token;

	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "user_id")
	private User user;

	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime expiryDate;

	public void calculateExpiryDate(final int expiryTimeInMinutes) {
		LocalDateTime now = LocalDateTime.now();
//		final Calendar cal = Calendar.getInstance();
//		cal.setTimeInMillis(new Date().getTime());
//		cal.add(Calendar.MINUTE, expiryTimeInMinutes);
		this.expiryDate =  now.plusMinutes(expiryTimeInMinutes);
	}
	
	public void updateToken(final String token, int expiryTimeInMinutes) {
        this.token = token;
        calculateExpiryDate(expiryTimeInMinutes);
    }
}
