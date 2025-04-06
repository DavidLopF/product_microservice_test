package co.corp.linktic.productservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO genérico para las respuestas de la API")
public class GeneralResponseDTO<T> {
    @Schema(description = "Datos de la respuesta", example = "{\"id\": 1, \"name\": \"Producto\", \"price\": 99.99}")
    private T data;
}
