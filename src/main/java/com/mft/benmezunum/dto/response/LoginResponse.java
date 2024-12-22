// src/main/java/com/mft/springsecurity/dto/LoginResponse.java
package com.mft.benmezunum.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String bearer;

}