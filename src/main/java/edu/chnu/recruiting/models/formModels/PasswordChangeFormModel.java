package edu.chnu.recruiting.models.formModels;

import edu.chnu.recruiting.utils.constants.RegexPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeFormModel {
	@NotBlank
	private String oldPassword;
	@NotBlank
	@Size(min = 8, max = 32)
	@Pattern(regexp = RegexPatterns.PASSWORD,
	message = "Required: uppercase letter, lowercase letter, number and special character @$!%_*?&")
	private String newPassword;
	private String confirmPassword;
}
