package edu.chnu.recruiting.models;

import java.util.List;

import edu.chnu.recruiting.models.security.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class Company {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@OneToMany(fetch = FetchType.EAGER)
	private List<User> recruiters;
	
	@OneToOne(fetch = FetchType.EAGER)
	private User owner;
}
