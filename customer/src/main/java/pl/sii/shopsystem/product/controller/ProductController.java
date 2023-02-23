package pl.sii.shopsystem.product.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sii.shopsystem.product.dto.ProductOutputDto;
import pl.sii.shopsystem.product.service.ProductService;

@RestController
@Tag(name = "Product")
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    ResponseEntity<Page<ProductOutputDto>> fetchAllProducts(@Parameter Pageable pageable) {
        return ResponseEntity.ok(productService.fetchAllProducts(pageable));
    }

    @GetMapping("/{id}")
    ResponseEntity<ProductOutputDto> fetchProductById(@PathVariable String id) {
        return ResponseEntity.ok(productService.fetchProductById(id));
    }
}
