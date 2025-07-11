package com.becoder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.dto.CategoryDto;
import com.becoder.dto.CategoryResponse;
import com.becoder.entity.Category;
import com.becoder.exception.ResourceNotFoundException;
import com.becoder.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/category")
@Slf4j
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@PostMapping("/save-category")
	public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto) throws Exception {

		Boolean saveCategory = categoryService.saveCategory(categoryDto);

		if (saveCategory) {

			return new ResponseEntity<>("saved success", HttpStatus.CREATED);
		} else {

			return new ResponseEntity<>("not saved", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/list")
	public ResponseEntity<?> getAllCategories() {

		List<CategoryDto> allCategory = categoryService.getAllCategory();

		if (CollectionUtils.isEmpty(allCategory)) {

			return ResponseEntity.noContent().build();
		} else {

			return new ResponseEntity<>(allCategory, HttpStatus.OK);

		}

	}

	@GetMapping("/active-category")
	public ResponseEntity<?> getActiveCategory() {

		List<CategoryResponse> activeCategory = categoryService.getActiveCategory();

		if (CollectionUtils.isEmpty(activeCategory)) {

			return ResponseEntity.noContent().build();

		} else {

			return new ResponseEntity<>(activeCategory, HttpStatus.OK);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getCategoryDetailsById(@PathVariable Integer id) throws Exception {

		CategoryDto categoryDto = categoryService.getCategoryById(id);

		if (ObjectUtils.isEmpty(categoryDto)) {

			return new ResponseEntity<>("Category Not Found with Id= " + id, HttpStatus.NOT_FOUND);

		}

		return new ResponseEntity<>(categoryDto, HttpStatus.OK);

	}

	@DeleteMapping("delete/{id}")
	public ResponseEntity<?> deleteCategory(@PathVariable Integer id) {

		Boolean deleted = categoryService.deleteCategoryById(id);

		if (deleted) {

			return new ResponseEntity<>("Category Deleted success", HttpStatus.OK);

		}

		return new ResponseEntity<>("Category Not Found", HttpStatus.INTERNAL_SERVER_ERROR);

	}

}
