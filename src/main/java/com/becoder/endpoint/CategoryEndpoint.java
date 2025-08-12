package com.becoder.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.becoder.util.Constants.ROLE_ADMIN;

import static com.becoder.util.Constants.ROLE_ADMIN_USER;

import com.becoder.dto.CategoryDto;

@RequestMapping("/api/v1/category")
public interface CategoryEndpoint {

	@PostMapping("/save")
	@PreAuthorize(ROLE_ADMIN)
	public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto) throws Exception;

	@GetMapping("/")
	@PreAuthorize(ROLE_ADMIN)
	public ResponseEntity<?> getAllCategories();

	@GetMapping("/active")
	@PreAuthorize(ROLE_ADMIN_USER)
	public ResponseEntity<?> getActiveCategory();

	@GetMapping("/{id}")
	@PreAuthorize(ROLE_ADMIN)
	public ResponseEntity<?> getCategoryDetailsById(@PathVariable Integer id) throws Exception;

	@DeleteMapping("delete/{id}")
	@PreAuthorize(ROLE_ADMIN)
	public ResponseEntity<?> deleteCategory(@PathVariable Integer id);

}
