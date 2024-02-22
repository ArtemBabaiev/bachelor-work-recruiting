package edu.chnu.recruiting.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.chnu.recruiting.security.SecurityService;
import lombok.Getter;

@Service
@Getter
public class UnitOfWork {
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SecurityService securityService;
}
