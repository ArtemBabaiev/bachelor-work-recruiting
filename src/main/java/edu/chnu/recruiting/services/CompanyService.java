package edu.chnu.recruiting.services;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.chnu.recruiting.exceptions.AlreadyExistsException;
import edu.chnu.recruiting.models.Company;
import edu.chnu.recruiting.models.formModels.CompanyFormModel;
import edu.chnu.recruiting.models.formModels.RecruiterFormModel;
import edu.chnu.recruiting.models.security.Role;
import edu.chnu.recruiting.models.security.User;
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

	public Company updateCompany(CompanyFormModel model) {
		Company entity = this.companyRepository.findById(model.getId()).orElse(null);
		modelMapper.map(model, entity);
		return this.companyRepository.save(entity);
	}

	public Company createCompany(CompanyFormModel model) throws AlreadyExistsException {
		if (companyRepository.existsByName(model.getName())) {
			throw new AlreadyExistsException("Company with such name already exists");
		}
		User companyOwner = securityContext.getAuthenticatedUser();
		Role comRole = roleService.getRoleByName(StarterRoles.COMPANY.getName());
		companyOwner.setRole(comRole);
		companyOwner = userService.updateUser(companyOwner);

		Company company = modelMapper.map(model, Company.class);
		company.setOwner(companyOwner);

		return this.companyRepository.save(company);
	}

	@Transactional
	public <T> T getCompanyByAuthUser(Class<T> modelType) {
		User owner = this.securityContext.getAuthenticatedUser();
		Company company = this.getCompanyByUser(owner);
		if (company == null) {
			return null;
		}
		return modelMapper.map(company, modelType);
	}

	public Company getCompanyByUser(User user) {
		if (user.getRole().getName().equals(StarterRoles.COMPANY.getName())) {
			return this.companyRepository.findByOwner(user);
		}
		return this.companyRepository.findByRecruiters(user);
	}
	
	@Transactional
	public List<User> getCompanyRecruiters(Long companyId){
		return new ArrayList<User>(this.companyRepository.findById(companyId).get().getRecruiters());
	}
	
	@Transactional
	public void saveRecruiter(Long companyId, RecruiterFormModel model) throws AlreadyExistsException {
		Company company = this.companyRepository.findById(companyId).get();
		User rec;
		if (model.getId() == null) {
			rec = this.userService.createRecruiter(model);
			company.getRecruiters().add(rec);
			this.companyRepository.save(company);
		} else {
			rec = this.userService.updateRecruiter(model);
		}
	}
}
