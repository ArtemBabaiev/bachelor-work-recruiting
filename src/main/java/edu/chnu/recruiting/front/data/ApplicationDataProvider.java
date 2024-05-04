package edu.chnu.recruiting.front.data;

import java.util.stream.Stream;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;

import edu.chnu.recruiting.models.ApplicationSummary;
import edu.chnu.recruiting.models.viewModels.HasId;
import edu.chnu.recruiting.services.ApplicationService;
import edu.chnu.recruiting.utils.VaadinToSpring;

public class ApplicationDataProvider<T extends HasId<?>>
		extends AbstractBackEndDataProvider<T, IFilter<ApplicationSummary>> {

	private ApplicationService applicationService;
	private Class<T> type;

	public ApplicationDataProvider(ApplicationService applicationService, Class<T> type) {
		super();
		this.applicationService = applicationService;
		this.type = type;
	}

	@Override
	protected Stream<T> fetchFromBackEnd(Query<T, IFilter<ApplicationSummary>> query) {
		IFilter<ApplicationSummary> filter = query.getFilter().get();
		Specification<ApplicationSummary> spec = filter.getSpecification();
		PageRequest page = PageRequest.of(query.getPage(), query.getPageSize(),
				VaadinToSpring.convert(query.getSortOrders()));
		return this.applicationService.getAllBy(spec, page, type).stream();
	}

	@Override
	protected int sizeInBackEnd(Query<T, IFilter<ApplicationSummary>> query) {
		Specification<ApplicationSummary> spec = query.getFilter().get().getSpecification();
		return (int) applicationService.countBy(spec);
	}

	@Override
	public Object getId(T item) {
		return item.getId();
	}
}
