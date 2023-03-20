package com.caloriestracking.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.caloriestracking.exceptions.ExistingResourceException;
import com.caloriestracking.exceptions.ResourceNotFoundException;
import com.caloriestracking.model.Category;
import com.caloriestracking.repo.CategoryRepository;
import com.caloriestracking.service.CategoryService;
import com.caloriestracking.service.CloudinaryService;
import com.caloriestracking.validator.ObjectsValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private static Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

	private final CategoryRepository categoryRepository;

	private final CloudinaryService cloudinaryService;

	private final ObjectsValidator<Category> validator;

	@Override
	public Category findById(Long id) {
		return categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category with id <" + id + "> not found"));
	}

	@Override
	public Category findByCategoryName(String categoryName) {
		return categoryRepository.findByName(categoryName.trim())
				.orElseThrow(() -> new ResourceNotFoundException("Category with name <" + categoryName.trim() + "> not found"));
	}

	@Override
	public List<Category> findAll() {
		return categoryRepository.findAll(Sort.by("name").ascending());
	}
	
	@Override
	public List<Category> findAll(Integer pageNo, Integer pageSize, String sortBy) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Slice<Category> slicePage = categoryRepository.findAll(paging);
		return slicePage.getContent();
	}

	@Override
	public Category save(Category category, MultipartFile file) {

		log.info("Validate object: " + category);
		validator.validate(category);

		if (category.getId() != null) {
			if (categoryRepository.findById(category.getId()).isPresent()) {
				throw new ExistingResourceException("Category with id <" + category.getId() + "> is existing");
			}
		}
		
		if (categoryRepository.findByName(category.getName().trim()).isPresent())
			throw new ExistingResourceException("Category with name <" + category.getName().trim() + "> is existing");
		
		if (file != null && !file.isEmpty()) {
			String imageURL = cloudinaryService.uploadImage(file);
			category.setImage(imageURL);
		}
		
		category.setName(category.getName().trim());

		return categoryRepository.save(category);
	}

	@Override
	public Category update(Category category, MultipartFile file) {

		log.info("Validate object: " + category);
		validator.validate(category);

		Category cate = categoryRepository.findById(category.getId()).orElseThrow(
				() -> new ResourceNotFoundException("Category with id <" + category.getId() + "> not found"));

		if (file != null && !file.isEmpty()) {
			String imageURL = cloudinaryService.uploadImage(file);
			cate.setImage(imageURL);
		} else {
			cate.setImage(cate.getImage());
		}

		cate.setName(category.getName().trim());

		return categoryRepository.save(cate);
	}

	@Override
	public void delete(String categoryName) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public ResponseEntity<String> delete(Long id) {
		Category cate = findById(id);
		if (!cate.getFoods().isEmpty()) {
			throw new RuntimeException("Category with id <" + id + "> has foods list, can't be deleted");
		}
		
		categoryRepository.deleteById(id);
		
		return ResponseEntity.ok("Delete Category with <" + id + "> successfully!");
	}

}
