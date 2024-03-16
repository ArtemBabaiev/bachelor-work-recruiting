package edu.chnu.recruiting.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import edu.chnu.recruiting.exceptions.AlreadyExistsException;
import edu.chnu.recruiting.front.views.company.CompanyModel;
import edu.chnu.recruiting.models.Company;
import edu.chnu.recruiting.models.security.Role;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.repositories.CompanyRepository;
import edu.chnu.recruiting.utils.constants.StarterRoles;

@Service
public class CompanyService {

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	public Company createCompany(CompanyModel model) {
		if (companyRepository.existsByName(model.getName())) {
			throw new AlreadyExistsException("Company Already Exists");
		}
		Role recRole = roleService.getRoleByName(StarterRoles.RECRUITER.getName());
		Role comRole = roleService.getRoleByName(StarterRoles.COMPANY.getName());

		List<User> recruiters = new ArrayList<User>();
		for (User user : model.getRecruiters()) {
			user.setRole(recRole);
			recruiters.add(userService.updateUser(user));
		}
		User companyOwner = userService.getAuthenticatedUser();
		companyOwner.setRole(comRole);
		companyOwner = userService.updateUser(companyOwner);

		Company company = new Company();
		company.setName(model.getName());
		company.setRecruiters(recruiters);
		company.setOwner(companyOwner);

		company = this.companyRepository.save(company);
		return company;
	}
	
	public Company updateCompany(CompanyModel model) {
		throw new NotImplementedException();
	}

	public List<User> provideUsersForForm(String username, PageRequest pageRequest) {
		return this.userService.searchPaginated(username, pageRequest);
	}
	
	public CompanyModel getModelByCurrentUser() {
		User owner = this.userService.getAuthenticatedUser();
		Company company= this.companyRepository.findByOwner(owner);
		return map(company);
	}
	
	public Company getCompanyByUser(User user) {
		if (user.getRole().getName().equals(StarterRoles.COMPANY.getName())) {
			return this.companyRepository.findByOwner(user);
		}
		return this.companyRepository.findByRecruiters(user);
	}
	
	private CompanyModel map(Company company) {
		CompanyModel model = new CompanyModel();
		model.setId(company.getId());
		model.setName(company.getName());
		model.setOwner(company.getOwner());
		model.setRecruiters(new HashSet<User>(company.getRecruiters()));
		return model;
	}
	
	private Company map(CompanyModel model) {
		Company company = new Company();
		company.setId(model.getId());
		company.setName(model.getName());
		company.setOwner(model.getOwner());
		company.setRecruiters(model.getRecruiters().stream().toList());
		return company;
	}
}
