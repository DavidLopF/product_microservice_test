package co.corp.linktic.productservice.service.product;

import co.corp.linktic.productservice.dto.product.ProductCreateDTO;
import co.corp.linktic.productservice.dto.product.ProductUpdateDTO;
import co.corp.linktic.productservice.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Product createProduct(ProductCreateDTO product);

    Optional<Product> getProductById(Long id);

    Page<Product> getAllProducts(Pageable pageable);

    Product updateProduct(Long id, ProductUpdateDTO product);

    void deleteProduct(Long id);
}
