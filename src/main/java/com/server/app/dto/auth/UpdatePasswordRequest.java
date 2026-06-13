package com.server.app.dto.auth;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePasswordRequest {

    @NotBlank(message = "La contraseña actual es obligatoria")
    private String oldpassword;

    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 8, max = 100,
            message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$",
            message = "La contraseña debe incluir al menos una mayúscula, una minúscula, un número y un carácter especial"
    )
    private String newpassword;

    @NotBlank(message = "Debe confirmar la nueva contraseña")
    private String confirmpassword;
}
