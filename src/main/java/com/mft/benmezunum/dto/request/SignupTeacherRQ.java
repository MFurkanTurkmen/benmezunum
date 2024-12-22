package com.mft.benmezunum.dto.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@AllArgsConstructor
@Getter
@ToString
@Setter
public class SignupTeacherRQ {

    @NotBlank(message = "Kullanici Adi boş geçilemez")
    @Size(min = 3, max = 50, message = "kullanici Adi 3-50 karakter arasinda olmalidir")
    private String username;

    @NotBlank(message = "Şifre boş geçilemez")
    @Size(min = 6, max = 100, message = "sifre 6-12 karakter arasinda olmalidir")
    private String password;

}
