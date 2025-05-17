package com.backend.projectbackend.service;

import com.backend.projectbackend.dto.user.CloudinaryImageDTO;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private static Cloudinary cloudinary = null;

    public CloudinaryService(Cloudinary cloudinary) {
        CloudinaryService.cloudinary = cloudinary;
    }

    public static CloudinaryImageDTO uploadImageExercise(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("folder", "ejercicios"));

        String url = uploadResult.get("secure_url").toString();
        String publicId = uploadResult.get("public_id").toString();

        return new CloudinaryImageDTO(url, publicId);
    }

    public static CloudinaryImageDTO uploadImageProfilePicture(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("folder", "profilePictures"));

        String url = uploadResult.get("secure_url").toString();
        String publicId = uploadResult.get("public_id").toString();

        return new CloudinaryImageDTO(url, publicId);
    }

    public void deleteImage(String publicId) throws IOException {
        Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        if (!"ok".equals(result.get("result"))) {
            throw new RuntimeException("No se pudo eliminar la imagen: " + publicId);
        }
    }
}
