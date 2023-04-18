package pl.artur.shopsystem.product.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import order.OrderProductInputDto;
import order.OrderProductOutputDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.artur.shopsystem.product.dto.*;
import pl.artur.shopsystem.product.service.ProductService;

import java.util.List;

@RestController
@Tag(name = "Product")
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    ResponseEntity<List<MassProductOutputDto>> addProducts(@RequestBody List<AddProductInputDto> addProductInputDtoList) {
        return ResponseEntity.ok(productService.addProducts(addProductInputDtoList));
    }

    @GetMapping
    ResponseEntity<Page<ProductOutputDto>> fetchProductsList(@Parameter Pageable pageable) {
        return ResponseEntity.ok(productService.fetchAllProducts(pageable));
    }

    @GetMapping("/{productId}")
    ResponseEntity<ProductOutputDto> fetchProductById(@PathVariable String productId) {
        return ResponseEntity.ok(productService.fetchProductById(productId));
    }

    @PutMapping
    ResponseEntity<MassProductOutputDto> updateProduct(@RequestBody AlterProductInputDto alterProductInputDto) {
        return ResponseEntity.ok(productService.updateAllSameProducts(alterProductInputDto));
    }

    @DeleteMapping
    void removeProductsByName(
            @RequestParam String productName,
            @RequestParam String productsNumber) {
        productService.removeProductsByName(productName, productsNumber);
    }

    @PostMapping("/orders")
    ResponseEntity<List<OrderProductOutputDto>> purchaseProducts(
            @RequestBody List<OrderProductInputDto> orderProductInputDtoList) {
        return ResponseEntity.ok(productService.purchaseProducts(orderProductInputDtoList));
    }
}
