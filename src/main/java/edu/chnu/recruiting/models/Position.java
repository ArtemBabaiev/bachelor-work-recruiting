package edu.chnu.recruiting.models;

import java.util.Date;

import edu.chnu.recruiting.models.wizard.Wizard;
import edu.chnu.recruiting.utils.WizardConverterJson;
import edu.chnu.recruiting.utils.enums.EmploymentType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@Entity
public class Position {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String name;
	
	@NotBlank
	@Column(columnDefinition = "TEXT")
	private String description;
	
	@NotBlank
	private String department;
	
	@NotBlank
	private String location;
	
	private Double minSalary;
	
	private Double maxSalary;
	
	private String currencyCode = "USD";
	
	private Boolean active = true;
	
	@NotBlank
	private String employmentType = EmploymentType.FULL_TIME.toString();
	
	@Temporal(TemporalType.DATE)
	private Date datePosted;

	@ManyToOne(fetch = FetchType.EAGER)
	private Company company;

	@Column(name = "wizard_data", columnDefinition="LONGTEXT")
	@Convert(converter = WizardConverterJson.class)
	private Wizard wizardData;
}
