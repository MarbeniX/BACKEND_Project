package com.backend.projectbackend.service;

import com.backend.projectbackend.dto.auth.UpdatePasswordDTO;
import com.backend.projectbackend.dto.user.changeUsernameDTO;
import com.backend.projectbackend.model.User;
import com.backend.projectbackend.repository.AuthRepository;
import com.backend.projectbackend.util.responses.ApiResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;

    public UserService(AuthRepository authRepository, PasswordEncoder passwordEncoder, CloudinaryService cloudinaryService) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.cloudinaryService = cloudinaryService;
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
            if(!request.getNewPassword().equals(request.getConfirmPassword())) {
                return new ApiResponse<>(false, "Passwords do not match", null);
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
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
}
