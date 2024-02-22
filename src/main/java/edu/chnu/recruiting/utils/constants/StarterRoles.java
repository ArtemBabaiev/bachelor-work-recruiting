package edu.chnu.recruiting.utils.constants;

import lombok.Getter;

@Getter
public enum StarterRoles {
	ADMIN("ROLE_ADMIN"),
	COMPANY("ROLE_COMPANY"),
	RECRUITER("ROLE_RECRUITER"),
	USER("ROLE_USER")
	;
	
	
	private String name;
	
	private  StarterRoles(String name) {
		this.name = name;
	}
}
