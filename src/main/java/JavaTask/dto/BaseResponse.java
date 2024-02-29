package JavaTask.dto;

import lombok.Data;

@Data
public class BaseResponse {
    private String message;

    // Добавьте геттеры и сеттеры при необходимости

    public BaseResponse(String message) {
        this.message = message;
    }
}