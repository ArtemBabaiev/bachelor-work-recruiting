package edu.chnu.recruiting.models.security;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String email;
	private String username;
	private String password;
	private boolean enabled;

	private String fullName;
	
	@Column(columnDefinition = "DATE")
	private LocalDate dateOfBirth;

	@ManyToOne(fetch = FetchType.EAGER)
	private Role role;
}
