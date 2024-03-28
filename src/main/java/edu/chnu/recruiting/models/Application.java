package edu.chnu.recruiting.models;

import java.time.LocalDate;
import java.util.UUID;

import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.models.wizard.Wizard;
import edu.chnu.recruiting.utils.WizardConverterJson;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@Entity
public class Application {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	private String lastName;
	private String firstName;
	
	@Column(columnDefinition = "DATE")
	private LocalDate dateOfBirth;
	
	private String status;
	private String rejectReason;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Position position;
	
	@Convert(converter = WizardConverterJson.class)
	@Column(name = "wizard_data", columnDefinition="LONGTEXT")
	private Wizard wizardData;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;
}
