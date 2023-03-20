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
import com.caloriestracking.model.Food;
import com.caloriestracking.model.UserFavoriteFood;
import com.caloriestracking.model.UserFoodTracking;
import com.caloriestracking.repo.FoodRepository;
import com.caloriestracking.repo.UserFavoriteFoodRepository;
import com.caloriestracking.repo.UserFoodTrackingRepository;
import com.caloriestracking.service.CategoryService;
import com.caloriestracking.service.CloudinaryService;
import com.caloriestracking.service.FoodService;
import com.caloriestracking.validator.ObjectsValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {

	private static Logger log = LoggerFactory.getLogger(FoodServiceImpl.class);

	private final FoodRepository foodRepository;

	private final CategoryService categoryService;

	private final CloudinaryService cloudinaryService;

	private final ObjectsValidator<Food> validator;
	
	private final UserFavoriteFoodRepository favoriteFoodRepository;
	
	private final UserFoodTrackingRepository trackingRepository;

	@Override
	public Food findById(Long id) {
		return foodRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Food with id <" + id + "> not found"));
	}

	@Override
	public Food findByName(String foodName) {
		return foodRepository.findByName(foodName.trim())
				.orElseThrow(() -> new ResourceNotFoundException("Food with name <" + foodName.trim() + "> not found"));
	}

	@Override
	public List<Food> findAll() {
		return foodRepository.findAll();
	}
	
	@Override
	public List<Food> findAllByCategory(Long cateId) {
		return foodRepository.findAllByCategory_Id(cateId);
	}
	
	@Override
	public List<Food> findAllByCategory(Long cateId, Integer pageNo, Integer pageSize, String sortBy) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Slice<Food> slicePage = foodRepository.findAllByCategory_Id(cateId, paging);
		return slicePage.getContent();
	}

	@Override
	public List<Food> findAll(Integer pageNo, Integer pageSize, String sortBy) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Slice<Food> slicePage = foodRepository.findAll(paging);
		return slicePage.getContent();
	}

	@Override
	public Food save(Food food, MultipartFile file) {

		log.info("Validate object: " + food);
		validator.validate(food);

		if (food.getId() != null) {
			if (foodRepository.findById(food.getId()).isPresent()) {
				throw new ExistingResourceException("Food with id <" + food.getId() + "> is existing");
			}
		}

		if (foodRepository.findByName(food.getName().trim()).isPresent())
			throw new ExistingResourceException("Food with name <" + food.getName().trim() + "> is existing");

		Category category = food.getCategory();
		if (category != null) {
			if (category.getId() != null)
				category = categoryService.findById(category.getId());
			else
				throw new ResourceNotFoundException(
						" 'id' of this category is empty, must specify id for this category");
		} else
			throw new ResourceNotFoundException(" 'category' field is not included in this food JSON");

		if (file != null && !file.isEmpty()) {
			String imageURL = cloudinaryService.uploadImage(file);
			food.setImage(imageURL);
		}

		food.setName(food.getName().trim());
		food.setCategory(category);

		return foodRepository.save(food);
	}

	@Override
	public Food update(Food food, MultipartFile file) {
		log.info("Validate object: " + food);
		validator.validate(food);
		
		Food foundFood = foodRepository.findById(food.getId()).orElseThrow(
				() -> new ResourceNotFoundException("Food with id <" + food.getId() + "> not found"));
		
		Category category = food.getCategory();
		// Nếu có cập nhật category cho food này thì sẽ check info kẹp trong json
		// Không thì vẫn giữ nguyễn category cũ
		if(category != null) {
			if(category.getId() != null) {
				foundFood.setCategory(categoryService.findById(category.getId()));
			}
			else throw new ResourceNotFoundException(" 'id' of this category is empty, must specify id for this category");
		}
		
		if (file != null && !file.isEmpty()) {
			String imageURL = cloudinaryService.uploadImage(file);
			foundFood.setImage(imageURL);
		} else {
			foundFood.setImage(foundFood.getImage());
		}
		
		foundFood.setName(food.getName().trim());
		foundFood.setDescription(food.getDescription());
		foundFood.setProtein(food.getProtein());
		foundFood.setFat(food.getFat());
		foundFood.setCarb(food.getCarb());
		foundFood.setEnergyPerServing(food.getEnergyPerServing());
		
		return foodRepository.save(foundFood);
	}

	@Override
	public ResponseEntity<String> delete(Long id) {
		
		findById(id);
		
		List<UserFavoriteFood> favFoods = favoriteFoodRepository.findFavorListByFood_Id(id);
		
		List<UserFoodTracking> trackingFoods = trackingRepository.findFoodTrackingByFood_Id(id);
		
		if (!favFoods.isEmpty()) {
			log.info("Favorite list size: " + favFoods.size());
			throw new RuntimeException("Food with id <" + id + "> has favorite list, can't be deleted");
		}
		if (!trackingFoods.isEmpty()) {
			log.info("Tracking list size: " + trackingFoods.size());
			throw new RuntimeException("Food with id <" + id + "> has tracking list, can't be deleted");
		}
		
		foodRepository.deleteById(id);
		
		return ResponseEntity.ok("Delete Food with <" + id + "> successfully!");
	}
}
