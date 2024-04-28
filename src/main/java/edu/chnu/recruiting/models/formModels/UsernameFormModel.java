package edu.chnu.recruiting.models.formModels;

import edu.chnu.recruiting.utils.constants.RegexPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsernameFormModel {
	@NotBlank
	@Size(min = 6, max = 16)
	@Pattern(regexp = RegexPatterns.USERNAME, message = "Only alphanumeric and underscore values are allowed")
	private String username;
}
