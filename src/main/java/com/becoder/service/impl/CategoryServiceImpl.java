package com.becoder.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.becoder.dto.CategoryDto;
import com.becoder.dto.CategoryResponse;
import com.becoder.entity.Category;
import com.becoder.exception.ResourceNotFoundException;
import com.becoder.repository.CategoryRepository;
import com.becoder.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepo;

	@Autowired
	private ModelMapper mapper;

	@Override
	public Boolean saveCategory(CategoryDto categoryDto) {

		Category category = mapper.map(categoryDto, Category.class);

		if (ObjectUtils.isEmpty(category.getId())) {

			category.setIsDeleted(false);

			category.setCreatedBy(1);

			category.setCreatedOn(new Date());

		} else {

			updateCatagory(category);

		}

		Category saveCategory = categoryRepo.save(category);

		if (ObjectUtils.isEmpty(saveCategory)) {

			return false;
		}
		return true;
	}

	private void updateCatagory(Category category) {

		Optional<Category> findById = categoryRepo.findById(category.getId());

		if (findById.isPresent()) {

			Category existCategory = findById.get();

			category.setCreatedBy(existCategory.getCreatedBy());
			category.setCreatedOn(existCategory.getCreatedOn());
			category.setIsDeleted(existCategory.getIsDeleted());

			category.setUpdatedBy(1);
			category.setUpdatedOn(new Date());
		}

	}

	@Override
	public List<CategoryDto> getAllCategory() {

		List<Category> categories = categoryRepo.findByIsDeletedFalse();

		List<CategoryDto> categoryDtoList = categories.stream().map(cat -> mapper.map(cat, CategoryDto.class)).toList();

		return categoryDtoList;

	}

	@Override
	public List<CategoryResponse> getActiveCategory() {

		List<Category> categories = categoryRepo.findByIsActiveTrueAndIsDeletedFalse();

		List<CategoryResponse> categoryRespo = categories.stream().map(cat -> mapper.map(cat, CategoryResponse.class))
				.toList();

		return categoryRespo;
	}

	@Override
	public CategoryDto getCategoryById(Integer id) throws Exception {

		Category category = categoryRepo.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category not Found with id: " + id));

		if (!ObjectUtils.isEmpty(category)) {

			return mapper.map(category, CategoryDto.class);

		}

		return null;

	}

	@Override
	public Boolean deleteCategoryById(Integer id) {

		Optional<Category> deleteById = categoryRepo.findById(id);

		if (deleteById.isPresent()) {

			Category category = deleteById.get();

			category.setIsDeleted(true);

			categoryRepo.save(category);

			return true;

		}

		return false;

	}

}
