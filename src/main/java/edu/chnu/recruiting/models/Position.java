package edu.chnu.recruiting.models;

import edu.chnu.recruiting.models.wizard.Wizard;
import edu.chnu.recruiting.utils.JpaConverterJson;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Position {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	
	private String description;

	@ManyToOne
	private Company company;

	@Convert(converter = JpaConverterJson.class)
	private Wizard wizardData;
}
