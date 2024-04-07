package edu.chnu.recruiting.models.viewModels;

import java.util.List;

import edu.chnu.recruiting.models.security.User;
import lombok.Data;

@Data
public class CompanyViewModel {
	private Long id;

	private String name;

	private List<User> recruiters;

	private User owner;
}
