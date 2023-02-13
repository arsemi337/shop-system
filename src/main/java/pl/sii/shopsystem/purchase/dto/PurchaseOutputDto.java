package pl.sii.shopsystem.purchase.dto;

import lombok.Builder;

@Builder
public record PurchaseOutputDto(String userFirstname,
                                String userLastname,
                                String userEmail,
                                String productType,
                                String productTitle,
                                String productManufacturer,
                                String productPrice) {
}
