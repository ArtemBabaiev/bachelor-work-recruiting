package edu.chnu.recruiting.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import edu.chnu.recruiting.exceptions.AlreadyExistsException;
import edu.chnu.recruiting.models.Company;
import edu.chnu.recruiting.models.formModels.CompanyFormModel;
import edu.chnu.recruiting.models.security.Role;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.models.viewModels.CompanyViewModel;
import edu.chnu.recruiting.repositories.CompanyRepository;
import edu.chnu.recruiting.security.SecurityContext;
import edu.chnu.recruiting.utils.enums.StarterRoles;
import jakarta.transaction.Transactional;

@Service
public class CompanyService {

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private SecurityContext securityContext;
	
	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private ModelMapper modelMapper;

	public Company createCompany(CompanyFormModel model) throws AlreadyExistsException {
		if (companyRepository.existsByName(model.getName())) {
			throw new AlreadyExistsException("Company with such name already exists");
		}
		User companyOwner = securityContext.getAuthenticatedUser();
		
		Role recRole = roleService.getRoleByName(StarterRoles.RECRUITER.getName());
		Role comRole = roleService.getRoleByName(StarterRoles.COMPANY.getName());

		List<User> recruiters = new ArrayList<User>();
		for (User user : model.getRecruiters()) {
			user.setRole(recRole);
			recruiters.add(userService.updateUser(user));
		}
		
		companyOwner.setRole(comRole);
		companyOwner = userService.updateUser(companyOwner);

		Company company = new Company();
		company.setName(model.getName());
		company.setRecruiters(recruiters);
		company.setOwner(companyOwner);

		company = this.companyRepository.save(company);
		return company;
	}
	
	public Company updateCompany(CompanyFormModel model) {
		throw new NotImplementedException();
	}

	public List<User> provideUsersForForm(String username, PageRequest pageRequest) {
		return this.userService.searchPaginated(username, pageRequest);
	}
	
	@Transactional
	public CompanyViewModel getCompanyVMByAuthUser() {
		User owner = this.securityContext.getAuthenticatedUser();
		Company company= this.companyRepository.findByOwner(owner);
		return modelMapper.map(company, CompanyViewModel.class);
	}
	
	@Transactional
	public CompanyFormModel getCompanyFMByAuthUser() {
		User owner = this.securityContext.getAuthenticatedUser();
		Company company= this.companyRepository.findByOwner(owner);
		return modelMapper.map(company, CompanyFormModel.class);
	}
	
	public Company getCompanyByUser(User user) {
		if (user.getRole().getName().equals(StarterRoles.COMPANY.getName())) {
			return this.companyRepository.findByOwner(user);
		}
		return this.companyRepository.findByRecruiters(user);
	}
}
