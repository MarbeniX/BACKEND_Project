package com.backend.projectbackend.controllers;

import com.backend.projectbackend.dto.auth.*;
import com.backend.projectbackend.model.User;
import com.backend.projectbackend.service.AuthService;
import com.backend.projectbackend.util.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "1. Autenticación y Gestión de Cuentas", description = "Endpoints para registro, login y recuperación de cuenta.")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Crear una nueva cuenta de usuario",
               description = "Registra un nuevo usuario en el sistema y le envía un correo electrónico con un token para confirmar la cuenta.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Cuenta creada exitosamente. Se ha enviado un correo de confirmación.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Error en la solicitud (ej. email ya existe, contraseña débil).",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/create-account")
    public ResponseEntity<ApiResponse<String>> createAccount(@Valid @RequestBody AuthCreateAccountDTO request) throws MessagingException, UnsupportedEncodingException {
        ApiResponse<String> response = authService.createAccount(request);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "Confirmar una cuenta de usuario",
               description = "Valida el token recibido por correo para activar la cuenta del usuario.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Cuenta confirmada exitosamente.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Token inválido o expirado.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/{token}")
    public ResponseEntity<ApiResponse<String>> confirmAccount(
            @Parameter(description = "Token de confirmación recibido en el correo electrónico.") @PathVariable String token) throws MessagingException, UnsupportedEncodingException {
        ApiResponse<String> response = authService.confirmAccount(token);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "Iniciar sesión de usuario",
               description = "Autentica al usuario con su email y contraseña. Si es exitoso, devuelve un token JWT para ser usado en las peticiones protegidas.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Login exitoso. El token JWT se encuentra en el campo 'data' de la respuesta.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Credenciales incorrectas o cuenta no confirmada.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody AuthLoginDTO request) throws MessagingException, UnsupportedEncodingException {
        ApiResponse<String> response = authService.login(request);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "Solicitar nuevo código de confirmación",
               description = "Re-envía el correo de confirmación si el usuario no lo recibió o el token expiró.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Solicitud procesada. Se ha enviado un nuevo correo.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "El email no está registrado o la cuenta ya fue confirmada.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/req-code")
    public ResponseEntity<ApiResponse<String>> requestCode(@Valid @RequestBody RequestCodeDTO request) throws MessagingException, UnsupportedEncodingException {
        ApiResponse<String> response = authService.requestCode(request);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }
    
    @Operation(summary = "Solicitar código para restablecer contraseña",
               description = "Inicia el flujo para restablecer la contraseña. Envía un código al email del usuario.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Solicitud procesada. Se ha enviado un correo con el token de reseteo.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "El email no se encuentra registrado.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/req-passreset-code")
    public ResponseEntity<ApiResponse<String>> reqPassResetCode(@Valid @RequestBody ReqPassResetCodeDTO request) throws MessagingException, UnsupportedEncodingException {
        ApiResponse<String> response = authService.reqPassResetCode(request);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "Actualizar contraseña con un token",
               description = "Permite al usuario establecer una nueva contraseña utilizando el token que recibió por correo.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Contraseña actualizada correctamente.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Token inválido o expirado.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/update-password/{token}")
    public ResponseEntity<ApiResponse<String>> updatePassword(
            @Parameter(description = "Token de reseteo de contraseña.") @PathVariable String token,
            @Valid @RequestBody UpdatePasswordDTO request) throws MessagingException, UnsupportedEncodingException {
        ApiResponse<String> response = authService.updatePassword(token, request);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "Validar un token",
               description = "Verifica si un token (de confirmación o de reseteo de contraseña) es válido y no ha expirado.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "El token es válido.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "El token es inválido o ha expirado.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/validate/{token}")
    public ResponseEntity<ApiResponse<String>> validateToken(
            @Parameter(description = "Token a validar.") @PathVariable String token) throws MessagingException, UnsupportedEncodingException {
        ApiResponse<String> response = authService.validateToken(token);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }
}