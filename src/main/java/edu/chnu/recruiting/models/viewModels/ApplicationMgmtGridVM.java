package edu.chnu.recruiting.models.viewModels;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationMgmtGridVM {
	
	private UUID id;
	private String lastName;
	
	private String firstName;

	private LocalDate dateOfBirth;
	
	private LocalDateTime startedAt;
	private LocalDateTime submittedAt;

	private String status;
}
