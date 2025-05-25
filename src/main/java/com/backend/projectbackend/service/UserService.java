package com.backend.projectbackend.service;

import com.backend.projectbackend.dto.auth.UpdatePasswordDTO;
import com.backend.projectbackend.dto.user.CloudinaryImageDTO;
import com.backend.projectbackend.dto.user.changeUsernameDTO;
import com.backend.projectbackend.model.User;
import com.backend.projectbackend.repository.AuthRepository;
import com.backend.projectbackend.repository.RoutineRepository;
import com.backend.projectbackend.util.responses.ApiResponse;
import com.cloudinary.Api;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;
    private final RoutineRepository routineRepository;

    public UserService(AuthRepository authRepository, PasswordEncoder passwordEncoder, CloudinaryService cloudinaryService, RoutineRepository routineRepository) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.cloudinaryService = cloudinaryService;
        this.routineRepository = routineRepository;
    }

    public ApiResponse<String> changeUsername(changeUsernameDTO request, User user) {
        try {
            user.setUsername(request.getUsername());
            authRepository.save(user);
            return new ApiResponse<>(true, "Saved.", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }

    public ApiResponse<String> changePassword(UpdatePasswordDTO request, User user) {
        try {
            if(!request.getPassword().equals(request.getPasswordConfirm())) {
                return new ApiResponse<>(false, "Passwords do not match", null);
            }
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            authRepository.save(user);
            return new ApiResponse<>(true, "Saved.", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }

    public ApiResponse<String> uploadProfilePicture(String imageUrl, String publicID, User user) {
        try {
            if(user.getProfilePictureURL() != null) {
                cloudinaryService.deleteImage(user.getPublicPictureID());
            }
            user.setPublicPictureID(publicID);
            user.setProfilePictureURL(imageUrl);
            authRepository.save(user);
            return new ApiResponse<>(true, "Saved.", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }

    public ApiResponse<CloudinaryImageDTO> getUserPp(User user) {
        try {
            if(user.getProfilePictureURL() == null) {
                return new ApiResponse<>(false, "User has no profile picture", null);
            }
            CloudinaryImageDTO imageDTO = new CloudinaryImageDTO();
            imageDTO.setUrl(user.getProfilePictureURL());
            imageDTO.setPublicID(user.getPublicPictureID());
            return new ApiResponse<CloudinaryImageDTO>(true, "Saved.", imageDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(false, "Internal server error: " + e.getMessage(), null);
        }
    }
}
