package edu.chnu.recruiting.front.views.auth.registration;

import java.time.LocalDate;

import edu.chnu.recruiting.utils.constants.RegexPatterns;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpModel {
	
	@NotBlank
	@Size(min = 6, max = 16)
	@Pattern(regexp = RegexPatterns.USERNAME, message = "Only alphanumeric and underscore values are allowed")
	private String username;
	
	@Email
	@NotBlank
	private String email;
	
	@NotBlank
	@Size(min = 8, max = 32)
	@Pattern(regexp = RegexPatterns.PASSWORD,
	message = "Required: uppercase letter, lowercase letter, number and special character @$!%_*?&")
	private String password;
	
	@NotBlank
	private String confirmPassword;
	
	@NotBlank
	private String fullName;
	
	@NotNull
	private LocalDate dateOfBirth;
}
