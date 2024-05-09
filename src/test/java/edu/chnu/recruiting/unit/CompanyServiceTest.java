package edu.chnu.recruiting.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import edu.chnu.recruiting.exceptions.AlreadyExistsException;
import edu.chnu.recruiting.models.Company;
import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.models.formModels.CompanyFormModel;
import edu.chnu.recruiting.models.formModels.RecruiterFormModel;
import edu.chnu.recruiting.models.security.Role;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.models.wizard.Wizard;
import edu.chnu.recruiting.repositories.CompanyRepository;
import edu.chnu.recruiting.security.SecurityContext;
import edu.chnu.recruiting.services.ApplicationService;
import edu.chnu.recruiting.services.CompanyService;
import edu.chnu.recruiting.services.RoleService;
import edu.chnu.recruiting.services.UserService;
import edu.chnu.recruiting.utils.enums.StarterRoles;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {
	@InjectMocks
	CompanyService companyService;

	@Mock
	private CompanyRepository companyRepository;

	@Mock
	private SecurityContext securityContext;

	@Mock
	private UserService userService;

	@Mock
	private RoleService roleService;

	@Mock
	private ModelMapper modelMapper;

	@Test
	void testCreateCompany() throws AlreadyExistsException {
		User authUser = new User();
		authUser.setUsername("test");
		authUser.setRole(new Role("ROLE_USER"));

		ModelMapper mm = new ModelMapper();
		CompanyFormModel model = new CompanyFormModel();
		model.setName("Test company");
		Company company = new Company();

		when(companyRepository.existsByName(anyString())).thenReturn(false);
		when(securityContext.getAuthenticatedUser()).thenReturn(authUser);
		when(roleService.getRoleByName(StarterRoles.COMPANY.getName())).thenReturn(new Role("ROLE_COMPANY"));
		when(userService.updateUser(any(User.class))).thenReturn(authUser);
		when(modelMapper.map(model, Company.class)).thenReturn(company);
		when(this.companyRepository.save(any(Company.class))).thenReturn(company);

		var returned = companyService.createCompany(model);

		assertNotNull(returned.getOwner());
		assertEquals(StarterRoles.COMPANY.getName(), returned.getOwner().getRole().getName());
	}

	@Test
	void testSaveRecruiter() throws AlreadyExistsException {
		RecruiterFormModel model = new RecruiterFormModel();
		Company company = new Company();
		company.setRecruiters(new ArrayList<User>());
		
		when(this.companyRepository.findById(anyLong())).thenReturn(Optional.of(company));
		when(this.userService.createRecruiter(any(RecruiterFormModel.class))).thenReturn(new User());
		when(this.userService.updateRecruiter(any(RecruiterFormModel.class))).thenReturn(new User());
		when(this.companyRepository.save(any(Company.class))).thenReturn(company);
		
		companyService.saveRecruiter(1L, model);
		model.setId(1L);
		companyService.saveRecruiter(1L, model);
		
	}
	
	@Test
	void testDeleteRecruiter() {
		RecruiterFormModel model = new RecruiterFormModel();
		model.setId(1L);
		Company company = new Company();
		User user1 = new User();
		User user2 = new User();
		user1.setId(1L);
		user2.setId(2L);
		company.setRecruiters(new ArrayList<User>(List.of(user1, user2)));
		
		when(this.companyRepository.findById(anyLong())).thenReturn(Optional.of(company));
		when(this.companyRepository.save(any(Company.class))).thenReturn(company);
		doNothing().when(userService).deleteUser(anyLong());
		
		this.companyService.deleteRecruiter(1L, model);
		
		assertEquals(1, company.getRecruiters().size());
		assertEquals(2L, company.getRecruiters().iterator().next().getId());
	}
}
