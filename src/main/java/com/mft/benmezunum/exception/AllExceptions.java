package com.mft.benmezunum.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@AllArgsConstructor
public enum AllExceptions {

    UNKNOWN_ERROR(400, "bilinmeyen hata",HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_ACCESS(401, "Yetkisiz erişim",HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(403, "Erişim reddedildi",HttpStatus.FORBIDDEN),
    PAGE_NOT_FOUND(404, "Kaynak bulunamadı",HttpStatus.NOT_FOUND),
    USER_NOT_FOUND(405, "Kullanici Bulunamadi",HttpStatus.BAD_REQUEST);

    public int code;
    public String message;
    public HttpStatus httpStatus;
}
