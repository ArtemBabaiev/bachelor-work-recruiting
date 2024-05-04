package edu.chnu.recruiting.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.chnu.recruiting.models.Position;
import edu.chnu.recruiting.models.viewModels.PositionViewModel;

@Configuration
public class ModelMapperConfig {
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper mm = new ModelMapper();
		configurePositionMap(mm);
		return mm;
	}

	private void configurePositionMap(ModelMapper mm) {
		mm.createTypeMap(Position.class, PositionViewModel.class)
				.addMappings(mapping -> mapping.skip(PositionViewModel::setCompanyRecruiters));
	}
}
