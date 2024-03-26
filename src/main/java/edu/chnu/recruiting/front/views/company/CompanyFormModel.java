package edu.chnu.recruiting.front.views.company;

import java.util.HashSet;
import java.util.Set;

import edu.chnu.recruiting.models.security.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyFormModel {
	private Long id;
	@NotBlank
	private String name;
	private User owner;
	private Set<User> recruiters = new HashSet<User>();
}
