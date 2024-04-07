package edu.chnu.recruiting.models.viewModels;

import java.time.LocalDate;
import java.time.LocalDateTime;

import edu.chnu.recruiting.models.wizard.Wizard;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationViewModel {
	private String lastName;
	private String firstName;

	private LocalDate dateOfBirth;

	private String status;
	private String rejectReason;

	private LocalDateTime startedAt;

	private LocalDateTime submittedAt;

	private Wizard wizardData;

	private Long positionId;
	private String positionName;
	private Long positionCompanyId;
}
