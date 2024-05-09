package edu.chnu.recruiting.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import edu.chnu.recruiting.exceptions.WizardFinishedException;
import edu.chnu.recruiting.models.ApplicationFull;
import edu.chnu.recruiting.models.ApplicationSummary;
import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.models.formModels.ApplyFormModel;
import edu.chnu.recruiting.models.security.Role;
import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.models.wizard.Wizard;
import edu.chnu.recruiting.models.wizard.WizardStep;
import edu.chnu.recruiting.repositories.ApplicationFullRepository;
import edu.chnu.recruiting.repositories.ApplicationRepository;
import edu.chnu.recruiting.security.SecurityContext;
import edu.chnu.recruiting.services.ApplicationService;
import edu.chnu.recruiting.services.MailService;
import edu.chnu.recruiting.services.PositionService;
import edu.chnu.recruiting.utils.enums.ApplicationStatus;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {

	@InjectMocks
	ApplicationService applicationService;

	@Mock
	ApplicationRepository applicationRepository;

	@Mock
	ApplicationFullRepository appFullRepository;

	@Mock
	PositionService positionService;

	@Mock
	SecurityContext securityContext;

	@Mock
	ModelMapper modelMapper;

	@Mock
	MailService mailService;

	static User authUser;
	static Position position;

	@BeforeAll
	static void init() {
		authUser = new User();
		authUser.setUsername("test");
		authUser.setRole(new Role("USER"));

		position = new Position();
		position.setId(1L);
		position.setName("Test Position");
		position.setActive(true);
		position.setWizardData(new Wizard());
	}

	@Test
	void testApply() {
		ModelMapper mm = new ModelMapper();
		ApplyFormModel model = new ApplyFormModel();
		model.setDateOfBirth(LocalDate.now());
		model.setEmail("test@email.com");
		model.setFullName("Test full name");
		ApplicationFull applicationFull = new ApplicationFull();

		when(positionService.getPosition(anyLong())).thenReturn(position);
		when(securityContext.getAuthenticatedUser()).thenReturn(authUser);
		when(modelMapper.map(model, ApplicationFull.class)).then(invocation -> {
			mm.map(model, applicationFull);
			return applicationFull;
		});
		when(appFullRepository.save(any(ApplicationFull.class))).then(invocation -> {
			applicationFull.setId(1L);
			return applicationFull;
		});

		var returned = applicationService.apply(model, 1L);

		assertNotNull(returned.getUser());
		assertNotNull(returned.getPosition());
		assertEquals(returned.getFullName(), model.getFullName());

	}

	@Test
	void testSaveFinalApplication() {
		ApplicationFull applicationFull = new ApplicationFull();
		when(this.appFullRepository.save(applicationFull)).thenReturn(applicationFull);

		var returned = applicationService.saveFinalApplication(applicationFull);
		assertEquals(ApplicationStatus.PENDING_REVIEW.toString(), returned.getStatus());
		assertNotNull(returned.getSubmittedAt());
	}

	@Test
	void testGetApplicationStep() {
		ApplicationFull applicationFull = new ApplicationFull();
		Wizard wiz = new Wizard();
		WizardStep step = new WizardStep();
		step.setId(0);
		wiz.addStep(step);
		applicationFull.setWizardData(wiz);

		when(this.appFullRepository.findById(anyLong())).thenReturn(Optional.of(applicationFull));

		var returned = this.applicationService.getApplicationStep(1L, 0);
		assertNotNull(returned);
		assertEquals(0, returned.getId());
	}

	@Test
	void testSaveStepAndGetNext() throws WizardFinishedException {
		ApplicationFull applicationFull = new ApplicationFull();
		Wizard wiz = new Wizard();
		WizardStep step0 = new WizardStep();
		step0.setId(0);
		WizardStep step1 = new WizardStep();
		step1.setId(1);
		wiz.addStep(step0);
		wiz.addStep(step1);
		applicationFull.setWizardData(wiz);

		when(this.appFullRepository.findById(anyLong())).thenReturn(Optional.of(applicationFull));
		when(this.appFullRepository.save(applicationFull)).thenReturn(applicationFull);

		var returned = applicationService.saveStepAndGetNext(1L, step0);

		assertEquals(1, returned.getId());
	}

	@Test
	void testAcceptApplication() {
		ApplicationSummary app = new ApplicationSummary();
		when(this.applicationRepository.findById(anyLong())).thenReturn(Optional.of(app));
		when( this.applicationRepository.save(app)).thenReturn(app);
		doNothing().when(mailService).sendAcceptedEmail(app);
		
		var returned = applicationService.acceptApplication(1L, "notes");
		assertEquals(ApplicationStatus.ACCEPTED.toString(), returned.getStatus());
		assertEquals("notes", returned.getNotes());
	}
	
	@Test
	void testRejectApplication() {
		ApplicationSummary app = new ApplicationSummary();
		when(this.applicationRepository.findById(anyLong())).thenReturn(Optional.of(app));
		when( this.applicationRepository.save(app)).thenReturn(app);
		doNothing().when(mailService).sendRejectedEmail(app);
		
		var returned = applicationService.rejectApplication(1L, "reject");
		assertEquals(ApplicationStatus.REJECTED.toString(), returned.getStatus());
		assertEquals("reject", returned.getRejectReason());
	}
}
