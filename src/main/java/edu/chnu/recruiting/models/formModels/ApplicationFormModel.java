package edu.chnu.recruiting.models.formModels;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationFormModel {
	@NotBlank
	private String lastName;
	@NotBlank
	private String firstName;
	@NotNull
	private LocalDate dateOfBirth;
	
	private LocalDateTime startedAt = LocalDateTime.now();
}
