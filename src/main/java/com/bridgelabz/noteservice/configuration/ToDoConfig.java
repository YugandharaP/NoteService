package com.bridgelabz.noteservice.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yuga
 * @since 16/07/2018
 *        <p>
 *        <b>To configure the beans which is required in the todo
 *        application</b>
 *        </p>
 */
@Configuration
public class ToDoConfig {
	/**
	 * <p>
	 * <b>ModelMapper - Performs object mapping, maintains</b>
	 * </p>
	 * 
	 * @return model mapper object
	 */
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
