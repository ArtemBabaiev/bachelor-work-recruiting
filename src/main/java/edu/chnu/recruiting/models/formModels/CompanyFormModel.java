package edu.chnu.recruiting.models.formModels;

import edu.chnu.recruiting.models.security.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyFormModel {
	private Long id;
	@NotBlank
	private String name;
	
	@NotBlank
	private String description;
	
	@NotBlank
	private String industry;
	
	@NotBlank
	@Pattern(regexp = "[+]?[(]?[0-9]{3}[)]?[-s.]?[0-9]{3}[-s.]?[0-9]{4,6}$", message = "must be valid phone number")
	private String contactPhone;
	
	@NotBlank
	@Email
	private String email;
	
	@NotBlank
	private String address;
	
	private User owner;
}
