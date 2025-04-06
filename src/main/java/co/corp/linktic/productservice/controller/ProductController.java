package co.corp.linktic.productservice.controller;

import co.corp.linktic.productservice.dto.GeneralResponseDTO;
import co.corp.linktic.productservice.dto.product.ProductCreateDTO;
import co.corp.linktic.productservice.dto.product.ProductUpdateDTO;
import co.corp.linktic.productservice.entity.Product;
import co.corp.linktic.productservice.service.product.ProductService;
import co.corp.linktic.productservice.service.s3.S3Service;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*",
        allowedHeaders = {"Content-Type", "api-key", "Authorization"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ProductController {

    private final ProductService productService;
    private final S3Service s3Service;

    public ProductController(ProductService productService, S3Service s3Service) {
        this.productService = productService;
        this.s3Service = s3Service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponseDTO<Page<Product>>> getAllProducts(
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<Product> products = (Page<Product>) productService.getAllProducts(pageable);
        GeneralResponseDTO<Page<Product>> response = new GeneralResponseDTO<>();

        response.setData(products);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponseDTO<Product>> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id).orElse(null);
        GeneralResponseDTO<Product> response = new GeneralResponseDTO<>();
        response.setData(product);
        return ResponseEntity.ok(response);
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GeneralResponseDTO<Product>> createProduct(
            @Valid @RequestPart("product") ProductCreateDTO product,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            String imageUrl = s3Service.uploadFile(image);
            product.setImageUrl(imageUrl);
        }
        Product savedProduct = productService.createProduct(product);
        GeneralResponseDTO<Product> response = new GeneralResponseDTO<>();
        response.setData(savedProduct);
        return ResponseEntity.ok(response);
    }
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GeneralResponseDTO<Product>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestPart("product") ProductUpdateDTO productDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        if (image != null && !image.isEmpty()) {
            String imageUrl = s3Service.uploadFile(image);
            productDTO.setImageUrl(imageUrl);
        }

        Product updatedProduct = productService.updateProduct(id, productDTO);
        GeneralResponseDTO<Product> response = new GeneralResponseDTO<>();
        response.setData(updatedProduct);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponseDTO<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        GeneralResponseDTO<Void> response = new GeneralResponseDTO<>();
        response.setData(null);
        return ResponseEntity.ok(response);
    }

}