package com.caloriestracking.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

	private static Logger log = LoggerFactory.getLogger(CloudinaryService.class);

	private final Cloudinary cloudinary;

	public String uploadImage(MultipartFile file) {
		String resultURL = "";
		try {
			var uploadResult = cloudinary
					.uploader()
					.upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto",
																"public_id", file.getOriginalFilename().split("\\.")[0]));
			resultURL = uploadResult.get("secure_url").toString();
			log.info("Upload file: " + resultURL + " successfully!");
		} catch (IOException e) {
			log.error("Upload file : " + file.getOriginalFilename() + " failed!");
			log.error(e.getMessage());
		}
		return resultURL;
	}
}
