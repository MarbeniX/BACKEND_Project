package com.backend.projectbackend.controllers;
//ENDPOINTS-

import com.backend.projectbackend.dto.auth.UpdatePasswordDTO;
import com.backend.projectbackend.dto.auth.UserDTO;
import com.backend.projectbackend.dto.user.CloudinaryImageDTO;
import com.backend.projectbackend.dto.user.changeUsernameDTO;
import com.backend.projectbackend.model.User;
import com.backend.projectbackend.service.CloudinaryService;
import com.backend.projectbackend.service.UserService;
import com.backend.projectbackend.util.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) { this.userService = userService; }

    @PutMapping("/change-username")
    public ResponseEntity<ApiResponse<String>> changeUsername(@Valid @RequestBody changeUsernameDTO request, Authentication authentication) throws MessagingException, UnsupportedEncodingException {
        User user = (User) authentication.getPrincipal(); // Usuario autenticado a partir del JWT
        ApiResponse<String> response = userService.changeUsername(request, user);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@Valid @RequestBody UpdatePasswordDTO request, Authentication authentication) throws MessagingException, UnsupportedEncodingException {
        User user = (User) authentication.getPrincipal(); // Usuario autenticado a partir del JWT
        ApiResponse<String> response = userService.changePassword(request, user);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping(path = "/uploadProfilePicture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> uploadProfilePicture(@RequestParam MultipartFile image, Authentication authentication) throws MessagingException, UnsupportedEncodingException {
        User user = (User) authentication.getPrincipal(); // Usuario autenticado a partir del JWT
        String imageUrl = "";
        String publicID = "";
        try {
            CloudinaryImageDTO dataImage = CloudinaryService.uploadImageProfilePicture(image);
            imageUrl = dataImage.getUrl();
            publicID = dataImage.getPublicID();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ApiResponse<String> response = userService.uploadProfilePicture(imageUrl, publicID, user);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/getUserPp")
    public ResponseEntity<ApiResponse<CloudinaryImageDTO>> getUserPp(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        ApiResponse<CloudinaryImageDTO> response = userService.getUserPp(user);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "Obtener información del usuario autenticado (Endpoint Protegido)",
            description = "Devuelve los datos del usuario que realiza la petición. Requiere un token JWT válido en la cabecera 'Authorization'.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Información del usuario obtenida.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado. Se requiere token JWT.", content = @Content)
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/user")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(new UserDTO(user));
    }
}
