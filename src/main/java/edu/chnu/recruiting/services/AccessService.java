package edu.chnu.recruiting.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.chnu.recruiting.models.Application;
import edu.chnu.recruiting.models.Company;
import edu.chnu.recruiting.models.formModels.CompanyFormModel;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.models.viewModels.ApplicationViewModel;
import edu.chnu.recruiting.security.SecurityContext;

@Service
public class AccessService {
	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private CompanyService companyService;

	public boolean canUserManageApplication(ApplicationViewModel model) {
		Company comp = this.companyService.getCompanyByUser(this.securityContext.getAuthenticatedUser());
		return model.getPositionCompanyId().equals(comp.getId());
	}

	public boolean canUserEditApplication(Application application) {
		return securityContext.getAuthenticatedUser().getId().equals(application.getUser().getId());
	}

	public boolean canUserEditCompany(CompanyFormModel model) {
		User user = this.securityContext.getAuthenticatedUser();
		return model.getOwner().getId().equals(user.getId());
	}
}
