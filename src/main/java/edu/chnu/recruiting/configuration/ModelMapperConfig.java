package edu.chnu.recruiting.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.chnu.recruiting.front.views.viewModels.CompanyViewModel;
import edu.chnu.recruiting.front.views.viewModels.PositionViewModel;
import edu.chnu.recruiting.models.Company;
import edu.chnu.recruiting.models.Position;

@Configuration
public class ModelMapperConfig {
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper mm = new ModelMapper();
		configurePositionMap(mm);
		configureCompanyMap(mm);
		return mm;
	}
	
	private void configurePositionMap(ModelMapper mm){
		mm.createTypeMap(Position.class, PositionViewModel.class)
		.addMappings(mapping -> mapping.skip(PositionViewModel::setCompanyRecruiters));
	}
	
	private void configureCompanyMap(ModelMapper mm) {
		mm.createTypeMap(Company.class, CompanyViewModel.class);
	}
}
