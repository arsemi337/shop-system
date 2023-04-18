package pl.artur.shopsystem.product.controller;

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
import product.model.Genre;

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
    ResponseEntity<Page<ProductOutputDto>> fetchProductsList(
            @RequestParam(required = false) String field,
            @RequestParam(required = false) String operation,
            @RequestParam(required = false) String value,
            Pageable pageable) {
        return ResponseEntity.ok(productService.fetchAllProducts(field, operation, value, pageable));
    }

    @GetMapping("/{productId}")
    ResponseEntity<ProductOutputDto> fetchProductById(@PathVariable String productId) {
        return ResponseEntity.ok(productService.fetchProductById(productId));
    }



    @GetMapping("/type/{type}")
    ResponseEntity<ProductTypeNumberOutputDto> getNumberOfProductsByGenre(@PathVariable Genre type) {
        return ResponseEntity.ok(productService.getNumberOfProductsByGenre(type));
    }

    @PutMapping
    ResponseEntity<MassProductOutputDto> updateProduct(@RequestBody AlterProductInputDto alterProductInputDto) {
        return ResponseEntity.ok(productService.updateAllSameProducts(alterProductInputDto));
    }

    @DeleteMapping
    void removeProductsByName(
            @RequestParam String productName,
            @RequestParam(required = false) String productsNumber) {
        productService.removeProductsByName(productName, productsNumber);
    }

    @PostMapping("/orders")
    ResponseEntity<List<OrderProductOutputDto>> orderProducts(
            @RequestBody List<OrderProductInputDto> orderProductInputDtoList) {
        return ResponseEntity.ok(productService.purchaseProducts(orderProductInputDtoList));
    }
}
