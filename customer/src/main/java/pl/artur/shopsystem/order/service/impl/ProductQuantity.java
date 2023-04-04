package pl.artur.shopsystem.order.service.impl;

import lombok.Builder;
import pl.artur.shopsystem.product.model.Product;

@Builder
public record ProductQuantity(
        Product product,
        int quantity) {
}
