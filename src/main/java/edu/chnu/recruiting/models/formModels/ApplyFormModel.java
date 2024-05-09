package edu.chnu.recruiting.models.formModels;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplyFormModel {
	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String fullName;

	@NotNull
	@Past
	private LocalDate dateOfBirth;

	private LocalDateTime startedAt = LocalDateTime.now();
}
