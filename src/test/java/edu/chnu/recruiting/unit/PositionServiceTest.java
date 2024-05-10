package edu.chnu.recruiting.unit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import edu.chnu.recruiting.models.Company;
import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.models.security.Role;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.models.wizard.Wizard;
import edu.chnu.recruiting.repositories.PositionRepository;
import edu.chnu.recruiting.security.SecurityContext;
import edu.chnu.recruiting.services.CompanyService;
import edu.chnu.recruiting.services.PositionService;
import edu.chnu.recruiting.services.WizardService;
import edu.chnu.recruiting.ui.views.management.position.components.FormСreationComponent;

@ExtendWith(MockitoExtension.class)
public class PositionServiceTest {

	@InjectMocks
	PositionService positionService;

	@Mock
	private WizardService wizardService;

	@Mock
	private SecurityContext securityContext;

	@Mock
	private CompanyService companyService;

	@Mock
	private PositionRepository positionRepository;

	@Mock
	private ModelMapper modelMapper;

	@Test
	void testCreatePosition() {
		User authUser = new User();
		authUser.setUsername("test");
		authUser.setRole(new Role("ROLE_COMPANY"));
		Position position = new Position();

		when(this.wizardService.createWizard(any(FormСreationComponent.class))).thenReturn(new Wizard());
		when(securityContext.getAuthenticatedUser()).thenReturn(authUser);
		when(companyService.getCompanyByUser(any(User.class))).thenReturn(new Company());
		when(positionRepository.save(position)).thenReturn(position);

		var returned = positionService.createPosition(position, new FormСreationComponent());

		assertNotNull(returned.getWizardData());
		assertNotNull(returned.getCompany());
		assertNotNull(returned.getDatePosted());
	}

	@Test
	public void testUpdatePosition() {
		Position position = new Position();
		when(positionRepository.save(position)).thenReturn(position);
		var returned = positionService.updatePosition(position);
		assertNotNull(returned.getUpdatedAt());
	}

	@Test
	void testUpdateWizard() {
		Position position = new Position();

		when(this.positionRepository.findById(anyLong())).thenReturn(Optional.of(position));
		when(this.wizardService.createWizard(any(FormСreationComponent.class))).thenReturn(new Wizard());
		when(positionRepository.save(position)).thenReturn(position);

		var returned = positionService.updateWizard(1L, new FormСreationComponent());

		assertNotNull(returned.getWizardData());
	}
}
