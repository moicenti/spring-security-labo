package com.server.app.dto.auth;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 20,
            message = "El nombre de usuario debe tener entre 3 y 20 caracteres")
    @Pattern(
            regexp = "^[A-Za-z0-9_]+$",
            message = "El nombre de usuario solo puede contener letras, números y guiones bajos"
    )
    private String username;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50,
            message = "El nombre debe tener entre 2 y 50 caracteres")
    @Pattern(
            regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$",
            message = "El nombre solo puede contener letras y espacios"
    )
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 50,
            message = "El apellido debe tener entre 2 y 50 caracteres")
    @Pattern(
            regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$",
            message = "El apellido solo puede contener letras y espacios"
    )
    private String surname;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "Debe proporcionar un correo electrónico válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 100,
            message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$",
            message = "La contraseña debe incluir al menos una mayúscula, una minúscula, un número y un carácter especial"
    )
    private String password;

    /**
     * ADMIN por defecto.
     * Idealmente el servicio debería asignarlo automáticamente.
     */
    private Long role = 1L;
}
