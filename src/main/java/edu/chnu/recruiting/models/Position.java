package edu.chnu.recruiting.models;

import edu.chnu.recruiting.models.wizard.Wizard;
import edu.chnu.recruiting.utils.JpaConverterJson;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
public class Position {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String name;
	
	@NotBlank
	private String description;

	@ManyToOne
	private Company company;

	@Column(name = "wizard_data", columnDefinition="LONGTEXT")
	@Convert(converter = JpaConverterJson.class)
	private Wizard wizardData;
}
