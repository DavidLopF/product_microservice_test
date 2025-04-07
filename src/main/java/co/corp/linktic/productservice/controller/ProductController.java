package co.corp.linktic.productservice.controller;

import co.corp.linktic.productservice.dto.GeneralResponseDTO;
import co.corp.linktic.productservice.dto.product.ProductCreateDTO;
import co.corp.linktic.productservice.dto.product.ProductUpdateDTO;
import co.corp.linktic.productservice.entity.Product;
import co.corp.linktic.productservice.service.product.ProductService;
import co.corp.linktic.productservice.service.s3.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Productos", description = "API para la gestión de productos")
public class ProductController {

    private final ProductService productService;
    private final S3Service s3Service;

    public ProductController(ProductService productService, S3Service s3Service) {
        this.productService = productService;
        this.s3Service = s3Service;
    }

    @Operation(summary = "Obtener todos los productos", description = "Retorna una lista paginada de productos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos encontrados exitosamente",
                    content = @Content(schema = @Schema(implementation = GeneralResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - API key inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponseDTO<Page<Product>>> getAllProducts(
            @Parameter(description = "Parámetros de paginación", example = "{\"page\": 0, \"size\": 10}")
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<Product> products = (Page<Product>) productService.getAllProducts(pageable);
        GeneralResponseDTO<Page<Product>> response = new GeneralResponseDTO<>();
        response.setData(products);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener un producto por ID", description = "Retorna un producto específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado exitosamente",
                    content = @Content(schema = @Schema(implementation = GeneralResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado - API key inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponseDTO<Product>> getProductById(
            @Parameter(description = "ID del producto", example = "1")
            @PathVariable Long id) {
        Product product = productService.getProductById(id).orElse(null);
        GeneralResponseDTO<Product> response = new GeneralResponseDTO<>();
        response.setData(product);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Crear un nuevo producto", description = "Crea un nuevo producto con opción de imagen")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto creado exitosamente",
                    content = @Content(schema = @Schema(implementation = GeneralResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado - API key inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GeneralResponseDTO<Product>> createProduct(
            @Parameter(description = "Datos del producto a crear")
            @Valid @RequestPart("product") ProductCreateDTO product,
            @Parameter(description = "Imagen del producto (opcional)")
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

    @Operation(summary = "Actualizar un producto", description = "Actualiza un producto existente por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = GeneralResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado - API key inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GeneralResponseDTO<Product>> updateProduct(
            @Parameter(description = "ID del producto a actualizar", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos del producto a actualizar")
            @Valid @RequestPart("product") ProductUpdateDTO productDTO,
            @Parameter(description = "Nueva imagen del producto (opcional)")
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

    @Operation(summary = "Eliminar un producto", description = "Elimina un producto por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado - API key inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponseDTO<Void>> deleteProduct(
            @Parameter(description = "ID del producto a eliminar", example = "1")
            @PathVariable Long id) {
        productService.deleteProduct(id);
        GeneralResponseDTO<Void> response = new GeneralResponseDTO<>();
        response.setData(null);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Obtener cantidad de productos por ID", description = "Retorna la cantidad de un producto específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cantidad de producto encontrada exitosamente",
                    content = @Content(schema = @Schema(implementation = GeneralResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado - API key inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping(value = "/{id}/quantity", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponseDTO<Integer>> getProductQuantityById(
            @Parameter(description = "ID del producto", example = "1")
            @PathVariable Long id) {
        Integer quantity = productService.getProductQuantityById(id);
        GeneralResponseDTO<Integer> response = new GeneralResponseDTO<>();
        response.setData(quantity);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{id}/stock", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralResponseDTO<Product>> updateProductStock(
            @Parameter(description = "ID del producto a actualizar", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Nueva cantidad de stock", example = "20")
            @RequestParam Integer stock) {
        Product updatedProduct = productService.updateProductStock(id, stock);
        GeneralResponseDTO<Product> response = new GeneralResponseDTO<>();
        response.setData(updatedProduct);
        return ResponseEntity.ok(response);
    }

}