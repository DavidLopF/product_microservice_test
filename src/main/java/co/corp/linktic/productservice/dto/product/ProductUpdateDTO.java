package co.corp.linktic.productservice.dto.product;

import lombok.Data;

@Data
public class ProductUpdateDTO {
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String imageUrl;
}