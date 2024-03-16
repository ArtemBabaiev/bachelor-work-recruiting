package edu.chnu.recruiting.models;

import java.util.Date;
import java.util.UUID;

import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.models.wizard.Wizard;
import edu.chnu.recruiting.utils.JpaConverterJson;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
public class Application {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	private String lastName;
	private String firstName;
	
	@Temporal(TemporalType.DATE)
	private Date dateOfBirth;
	
	private String status;
	private String rejectReason;
	
	
	@ManyToOne
	private Position position;
	
	@Convert(converter = JpaConverterJson.class)
	private Wizard wizardData;
	
	@ManyToOne
	private User user;
}
