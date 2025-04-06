package co.corp.linktic.productservice.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para la actualización de un producto existente")
public class ProductUpdateDTO {
    @Schema(description = "Nuevo nombre del producto", example = "Laptop HP Pro")
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @Schema(description = "Nueva descripción del producto", example = "Laptop HP Pro con procesador Intel i7")
    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    @Schema(description = "Nuevo precio del producto", example = "1099.99")
    @NotNull(message = "El precio es obligatorio")
    private Double price;

    @Schema(description = "Nueva cantidad disponible en stock", example = "15")
    @NotNull(message = "El stock es obligatorio")
    private Integer stock;

    @Schema(description = "Nueva URL de la imagen del producto (opcional)", example = "https://bucket.s3.amazonaws.com/new-product-image.jpg")
    private String imageUrl;
}