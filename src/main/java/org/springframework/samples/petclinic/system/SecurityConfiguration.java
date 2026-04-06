/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.system;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration: form login, access rules by role.
 */
@Configuration
@EnableWebSecurity
@ConditionalOnProperty(name = "security.enabled", matchIfMissing = true)
public class SecurityConfiguration {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(
				auth -> auth.requestMatchers("/", "/login", "/resources/**", "/webjars/**", "/actuator/**")
					.permitAll()
					.requestMatchers("/vets", "/vets.html")
					.hasAnyRole("USER", "ADMIN")
					.anyRequest()
					.hasRole("ADMIN"))
			.formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/", true).permitAll())
			.httpBasic(Customizer.withDefaults())
			.logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll());
		return http.build();
	}

}
