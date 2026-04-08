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
package org.springframework.samples.petclinic.user;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for admin user management.
 */
@Controller
@RequestMapping("/users")
class UserController {

	private static final String VIEWS_USER_FORM = "users/createOrUpdateUserForm";

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@ModelAttribute("roles")
	public Role[] roles() {
		return Role.values();
	}

	@GetMapping
	public String listUsers(Model model) {
		List<User> users = userRepository.findAll();
		model.addAttribute("users", users);
		return "users/userList";
	}

	@GetMapping("/new")
	public String initCreationForm(Model model) {
		model.addAttribute("user", new User());
		return VIEWS_USER_FORM;
	}

	@PostMapping("/new")
	public String processCreationForm(@Valid User user, BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return VIEWS_USER_FORM;
		}
		if (userRepository.findByUsername(user.getUsername()).isPresent()) {
			result.rejectValue("username", "duplicate", "Username already exists");
			return VIEWS_USER_FORM;
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		redirectAttributes.addFlashAttribute("message", "User " + user.getUsername() + " created");
		return "redirect:/users";
	}

	@GetMapping("/{userId}/edit")
	public String initUpdateForm(@PathVariable("userId") int userId, Model model) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
		user.setPassword("");
		model.addAttribute("user", user);
		return VIEWS_USER_FORM;
	}

	@PostMapping("/{userId}/edit")
	public String processUpdateForm(@PathVariable("userId") int userId, @Valid User user, BindingResult result,
			RedirectAttributes redirectAttributes) {
		User existing = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

		if (result.hasFieldErrors("username") || result.hasFieldErrors("role")) {
			return VIEWS_USER_FORM;
		}

		userRepository.findByUsername(user.getUsername()).ifPresent(conflict -> {
			if (!conflict.getId().equals(userId)) {
				result.rejectValue("username", "duplicate", "Username already exists");
			}
		});
		if (result.hasErrors()) {
			return VIEWS_USER_FORM;
		}

		existing.setUsername(user.getUsername());
		existing.setRole(user.getRole());
		existing.setEnabled(user.isEnabled());
		if (StringUtils.hasText(user.getPassword())) {
			existing.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		userRepository.save(existing);
		redirectAttributes.addFlashAttribute("message", "User " + existing.getUsername() + " updated");
		return "redirect:/users";
	}

	@PostMapping("/{userId}/delete")
	public String deleteUser(@PathVariable("userId") int userId, @AuthenticationPrincipal UserDetails currentUser,
			RedirectAttributes redirectAttributes) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
		if (user.getUsername().equals(currentUser.getUsername())) {
			redirectAttributes.addFlashAttribute("error", "You cannot delete your own account");
			return "redirect:/users";
		}
		userRepository.delete(user);
		redirectAttributes.addFlashAttribute("message", "User " + user.getUsername() + " deleted");
		return "redirect:/users";
	}

}
