package pl.artur.shopsystem.product.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.artur.shopsystem.product.dto.ProductOutputDto;
import pl.artur.shopsystem.product.dto.ProductTypeNumberOutputDto;
import pl.artur.shopsystem.product.service.ProductService;
import product.model.Genre;

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

    @GetMapping
    ResponseEntity<ProductTypeNumberOutputDto> getNumberOfProductsByGenre(@RequestParam("type") Genre type) {
        return ResponseEntity.ok(productService.getNumberOfProductsByGenre(type));
    }
}
