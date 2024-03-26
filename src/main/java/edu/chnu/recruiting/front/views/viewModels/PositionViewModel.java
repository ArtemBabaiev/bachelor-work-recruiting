package edu.chnu.recruiting.front.views.viewModels;

import java.time.LocalDate;
import java.util.List;

import edu.chnu.recruiting.models.security.User;
import lombok.Data;

@Data
public class PositionViewModel {
	private Long id;

	private String name;
	
	private String description;
	
	private String department;
	
	private String location;
	
	private Double minSalary;
	
	private Double maxSalary;
	
	private String currencyCode;
	
	private Boolean active;
	
	private String employmentType;
	
	private LocalDate datePosted;

	private Long companyId;

	private String companyName;

	private List<User> companyRecruiters;

	private User companyOwner;
}
