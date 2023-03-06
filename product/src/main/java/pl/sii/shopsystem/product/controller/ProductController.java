package pl.sii.shopsystem.product.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sii.shopsystem.product.dto.ProductInputDto;
import pl.sii.shopsystem.product.dto.ProductOutputDto;
import pl.sii.shopsystem.product.service.ProductService;

import java.util.List;

@RestController
@Tag(name = "Product")
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    ResponseEntity<List<ProductOutputDto>> addProducts(@RequestBody List<ProductInputDto> productInputDtoList) {
        return ResponseEntity.ok(productService.addProducts(productInputDtoList));
    }

    @GetMapping
    ResponseEntity<Page<ProductOutputDto>> fetchProductsList(@Parameter Pageable pageable) {
        return ResponseEntity.ok(productService.fetchAllProducts(pageable));
    }

    @GetMapping("/{productId}")
    ResponseEntity<ProductOutputDto> fetchProductById(@PathVariable String productId) {
        return ResponseEntity.ok(productService.fetchProductById(productId));
    }

    @PutMapping("/{productId}")
    ResponseEntity<ProductOutputDto> updateProduct(@PathVariable String productId,
                                                   @RequestBody ProductInputDto productInputDto) {
        return ResponseEntity.ok(productService.updateProduct(productId, productInputDto));
    }

    @DeleteMapping("/{productId}")
    void removeProduct(@PathVariable String productId) {
        productService.removeProduct(productId);
    }
}
