package edu.chnu.recruiting.models.viewModels;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
	
	private LocalDateTime updatedAt;

	private Long companyId;

	private String companyName;
	
	private String companyEmail;
	
	private String companyContactPhone;

	private List<User> companyRecruiters;

	private User companyOwner;
}
