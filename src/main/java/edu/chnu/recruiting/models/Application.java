package edu.chnu.recruiting.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import edu.chnu.recruiting.models.security.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@MappedSuperclass
public class Application {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String fullName;

	private String email;
	
	@Column(columnDefinition = "DATE")
	private LocalDate dateOfBirth;
	
	private String status;
	private String rejectReason;
	private String notes;
	
	
	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime startedAt;
	
	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime submittedAt;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Position position;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;
}
