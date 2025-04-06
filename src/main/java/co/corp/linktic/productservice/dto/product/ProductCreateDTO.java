package co.corp.linktic.productservice.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para la creación de un nuevo producto")
public class ProductCreateDTO {
    @Schema(description = "Nombre del producto", example = "Laptop HP")
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @Schema(description = "Descripción detallada del producto", example = "Laptop HP con procesador Intel i7")
    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    @Schema(description = "Precio del producto", example = "999.99")
    @NotNull(message = "El precio es obligatorio")
    private Double price;

    @Schema(description = "Cantidad disponible en stock", example = "10")
    @NotNull(message = "El stock es obligatorio")
    private Integer stock;

    @Schema(description = "URL de la imagen del producto (opcional)", example = "https://bucket.s3.amazonaws.com/product-image.jpg")
    private String imageUrl;
}