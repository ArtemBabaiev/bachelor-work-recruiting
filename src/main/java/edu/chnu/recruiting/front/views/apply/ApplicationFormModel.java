package edu.chnu.recruiting.front.views.apply;

import java.time.LocalDate;

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
}
