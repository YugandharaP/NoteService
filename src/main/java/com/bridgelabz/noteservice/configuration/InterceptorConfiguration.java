package com.bridgelabz.noteservice.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;




/**
 * @author yuga
 * @since 06/08/2018
 * <p><b>Configure interceptor class to add the interceptors in the todo application.</b></p>
 */
/*@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

	@Autowired
	LoggerInterceptor logInterceptor;
	@Autowired
	NoteInterceptor noteInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(logInterceptor);
		registry.addInterceptor(noteInterceptor).addPathPatterns("/notes/**");
	}
}
*/