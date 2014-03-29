package ai.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import dati.Dati;
import dati.InserzioneValidation;

@Configuration
@ComponentScan(basePackages="ai.server")
@EnableWebMvc
@ImportResource("classpath:spring-security.xml")
public class MvcConfiguration extends WebMvcConfigurerAdapter{

	@Bean
	public ViewResolver getViewResolver(){
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}
	@Bean
	public CommonsMultipartResolver multipartResolver(){
		CommonsMultipartResolver mr = new CommonsMultipartResolver();
       
        mr.setMaxUploadSize(10000000);
        return mr;
	}
	
	@Bean
	public Dati dati(){
		return dati.Dati.getInstance();
	}
	
	@Bean
	public InserzioneValidation inserzioneValidation(){
		return new InserzioneValidation();
	}
	
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

	
}
