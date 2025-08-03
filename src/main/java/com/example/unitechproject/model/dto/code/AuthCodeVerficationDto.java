package com.example.unitechproject.model.dto.code;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthCodeVerficationDto {

    @NotBlank(message = "Email boş ola bilməz.")
    @Email(message = "Doğru email formatı tələb olunur.")
    private String email;

    @NotBlank
    private String code;
}
