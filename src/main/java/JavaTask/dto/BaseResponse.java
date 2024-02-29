package JavaTask.dto;

import lombok.Data;

@Data
public class BaseResponse {
    private String message;
    private String entity;

    // Добавьте геттеры и сеттеры при необходимости

    public BaseResponse(String message, String entity) {
        this.message = message;
        this.entity = entity;
    }
}