package edu.chnu.recruiting.models.formModels;

import edu.chnu.recruiting.models.security.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecruiterFormModel {

	private Long id;
	@NotBlank
	private String username;
	private String password;
	private Boolean enabled;

	public static RecruiterFormModel of(User user) {
		RecruiterFormModel model = new RecruiterFormModel();
		model.id = user.getId();
		model.username = user.getUsername();
		model.enabled = user.isEnabled();
		return model;
	}
}
