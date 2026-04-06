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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.samples.petclinic.MockWebSecurityConfig;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests that the navbar shows the correct links for each authentication state.
 */
@WebMvcTest(WelcomeController.class)
@Import(MockWebSecurityConfig.class)
@DisabledInNativeImage
@DisabledInAotMode
class NavbarVisibilityTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void unauthenticatedUserSeesOnlyHomeLink() throws Exception {
		mockMvc.perform(get("/").with(anonymous()))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("href=\"/\"")))
			.andExpect(content().string(not(containsString("href=\"/vets.html\""))))
			.andExpect(content().string(not(containsString("href=\"/owners/find\""))))
			.andExpect(content().string(not(containsString("href=\"/users\""))))
			.andExpect(content().string(containsString("href=\"/login\"")));
	}

	@Test
	void userRoleSeesVeterinariansOnly() throws Exception {
		mockMvc.perform(get("/").with(user("user").roles("USER")))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("href=\"/vets.html\"")))
			.andExpect(content().string(not(containsString("href=\"/owners/find\""))))
			.andExpect(content().string(not(containsString("href=\"/users\""))));
	}

	@Test
	void adminRoleSeesVeterinariansOwnersAndUserManagement() throws Exception {
		mockMvc.perform(get("/").with(user("admin").roles("ADMIN")))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("href=\"/vets.html\"")))
			.andExpect(content().string(containsString("href=\"/owners/find\"")))
			.andExpect(content().string(containsString("href=\"/users\"")));
	}

}
