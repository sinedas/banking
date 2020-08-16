package lt.denislav.banking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@EnableResourceServer
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.antMatchers(HttpMethod.POST,
						"/users/register").permitAll()
				.antMatchers(
						HttpMethod.GET,
						"/v2/api-docs",
						"/swagger-resources/**",
						"/swagger-ui.html**",
						"/webjars/**",
						"favicon.ico").permitAll()
				.anyRequest()
				.authenticated();
	}
}
