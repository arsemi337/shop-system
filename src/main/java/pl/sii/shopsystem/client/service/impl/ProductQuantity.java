package pl.sii.shopsystem.client.service.impl;

import lombok.Builder;
import pl.sii.shopsystem.product.persistence.Product;

@Builder
public record ProductQuantity(
        Product product,
        int quantity) {
}
