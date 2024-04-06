package edu.chnu.recruiting.front.data;

import java.util.stream.Stream;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;

import edu.chnu.recruiting.models.Application;
import edu.chnu.recruiting.models.viewModels.ApplicationGridVM;
import edu.chnu.recruiting.services.ApplicationService;
import edu.chnu.recruiting.utils.VaadinToSpring;

public class ApplicationDataProvider extends AbstractBackEndDataProvider<ApplicationGridVM, ApplicationFilter>{

	private ApplicationService applicationService;
	
	public ApplicationDataProvider(ApplicationService applicationService) {
		super();
		this.applicationService = applicationService;
	}
	
	@Override
	protected Stream<ApplicationGridVM> fetchFromBackEnd(Query<ApplicationGridVM, ApplicationFilter> query) {
		ApplicationFilter filter = query.getFilter().get();
		Specification<Application> spec = filter.getSpecification();
		PageRequest page = PageRequest.of(query.getPage(), query.getPageSize(), VaadinToSpring.convert(query.getSortOrders()));
		return this.applicationService.getAllBy(spec, page).stream();
	}

	@Override
	protected int sizeInBackEnd(Query<ApplicationGridVM, ApplicationFilter> query) {
		Specification<Application> spec = query.getFilter().get().getSpecification();
		return (int) applicationService.countBy(spec);
	}
	
}
