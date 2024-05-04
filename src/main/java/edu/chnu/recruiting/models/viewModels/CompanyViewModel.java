package edu.chnu.recruiting.models.viewModels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyViewModel implements HasId<Long>{
	private Long id;
	
	private String name;
	
	private String description;
	
	private String industry;
	
	private String contactPhone;
	
	private String email;
	
	private String address;
}
