package com.caloriestracking.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.caloriestracking.dto.CategoryDTO;
import com.caloriestracking.model.Category;

@Service
public class CategoryDTOMapper implements Function<Category, CategoryDTO>{
	
	@Override
	public CategoryDTO apply(Category t) {
		
		return new CategoryDTO(
				t.getId(),
				t.getName(),
				t.getImage()
		);
	}

}
