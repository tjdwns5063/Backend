package Lingtning.new_match42;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@EnableJpaAuditing
@SpringBootApplication
public class NewMatch42Application {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(NewMatch42Application.class);
		application.addListeners(new ApplicationPidFileWriter());
		application.run(args);
	}

	/* Cors 설정 */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("/*")
						.allowedHeaders("*")
//						.allowedOrigins("http://" + System.getenv("NCP_IP") + ":" + System.getenv("REACT_PORT"), "http://localhost:" + System.getenv("REACT_PORT"))
						.allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
						.allowCredentials(true);
			}
		};
	}
}
