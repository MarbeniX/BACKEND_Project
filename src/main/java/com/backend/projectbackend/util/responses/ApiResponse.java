package com.backend.projectbackend.util.responses;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estructura de respuesta estándar para todas las peticiones de la API.")
public class ApiResponse<T> {

    @Schema(description = "Indica si la operación se completó exitosamente.", example = "true")
    private boolean success;

    @Schema(description = "Mensaje descriptivo sobre el resultado de la operación.", example = "Login exitoso.")
    private String message;

    @Schema(description = "Contenedor para los datos de la respuesta. Puede ser un objeto, una lista o un simple string (como un token JWT).")
    private T data;

    public ApiResponse() {}

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // Getters y Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}