package pl.sii.shopsystem.order.service.impl;

import lombok.Builder;
import pl.sii.shopsystem.product.model.Product;

@Builder
public record ProductQuantity(
        Product product,
        int quantity) {
}
