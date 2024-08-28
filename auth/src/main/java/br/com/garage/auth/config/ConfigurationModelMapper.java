package br.com.garage.auth.config;

import br.com.garage.auth.models.Usuario;
import br.com.garage.auth.interfaces.rest.dtos.UsuarioResponseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationModelMapper {

	@Bean
	ModelMapper configModelMapper() {

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		modelMapper.addMappings(new PropertyMap<Usuario, UsuarioResponseDto>() {
			@Override
			protected void configure() {
				map(source.getTenant(), destination.getTenant());
			}
		});
		
		return modelMapper;
	}

}
