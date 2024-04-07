package edu.chnu.recruiting.front.data;

import java.util.stream.Stream;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;

import edu.chnu.recruiting.models.Application;
import edu.chnu.recruiting.models.viewModels.ApplicationMgmtGridVM;
import edu.chnu.recruiting.services.ApplicationService;
import edu.chnu.recruiting.utils.VaadinToSpring;

public class ApplicationDataProvider<T> extends AbstractBackEndDataProvider<T, IFilter<Application>>{

	private ApplicationService applicationService;
	private Class<T> type;
	
	public ApplicationDataProvider(ApplicationService applicationService, Class<T> type) {
		super();
		this.applicationService = applicationService;
		this.type = type;
	}
	
	@Override
	protected Stream<T> fetchFromBackEnd(Query<T, IFilter<Application>> query) {
		IFilter<Application> filter = query.getFilter().get();
		Specification<Application> spec = filter.getSpecification();
		PageRequest page = PageRequest.of(query.getPage(), query.getPageSize(), VaadinToSpring.convert(query.getSortOrders()));
		return this.applicationService.getAllBy(spec, page, type).stream();
	}

	@Override
	protected int sizeInBackEnd(Query<T, IFilter<Application>> query) {
		Specification<Application> spec = query.getFilter().get().getSpecification();
		return (int) applicationService.countBy(spec);
	}
	
}
