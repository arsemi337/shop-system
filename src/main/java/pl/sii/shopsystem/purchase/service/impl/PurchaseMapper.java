package pl.sii.shopsystem.purchase.service.impl;

import pl.sii.shopsystem.client.persistence.Client;
import pl.sii.shopsystem.client.service.impl.ProductQuantity;
import pl.sii.shopsystem.purchase.dto.PurchaseInputDto;
import pl.sii.shopsystem.purchase.dto.PurchaseOutputDto;
import pl.sii.shopsystem.purchase.persistence.Purchase;
import pl.sii.shopsystem.purchase.purchaseProduct.dto.PurchaseProductInputDto;
import pl.sii.shopsystem.purchase.purchaseProduct.dto.PurchaseProductOutputDto;
import pl.sii.shopsystem.purchase.purchaseProduct.persistence.PurchaseProduct;
import pl.sii.shopsystem.purchase.purchaseProduct.persistence.PurchaseProductKey;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class PurchaseMapper {

    PurchaseOutputDto mapToPurchaseOutputDto(Client client, List<PurchaseProductOutputDto> purchasedProducts, BigDecimal totalCost) {
        return PurchaseOutputDto.builder()
                .clientFirstname(client.getFirstname())
                .clientLastname(client.getLastname())
                .clientEmail(client.getEmail())
                .purchasedProducts(purchasedProducts)
                .totalCost(totalCost)
                .build();
    }

    List<PurchaseProduct> mapToPurchaseProducts(List<ProductQuantity> productQuantities, Purchase purchase) {
        return productQuantities
                .stream()
                .map(productQuantity -> PurchaseProduct.builder()
                        .purchaseProductKey(
                                PurchaseProductKey.builder()
                                        .productId(productQuantity.product().getId())
                                        .purchaseId(purchase.getId())
                                        .build())
                        .product(productQuantity.product())
                        .purchase(purchase)
                        .quantity(productQuantity.quantity())
                        .build())
                .toList();
    }

    List<PurchaseProductOutputDto> mapToPurchaseProductOutputDtoList(List<ProductQuantity> productQuantities) {
        return productQuantities
                .stream()
                .map(productQuantity -> PurchaseProductOutputDto.builder()
                        .title(productQuantity.product().getTitle())
                        .type(productQuantity.product().getType())
                        .manufacturer(productQuantity.product().getManufacturer())
                        .price(productQuantity.product().getPrice())
                        .quantity(productQuantity.quantity())
                        .build())
                .toList();
    }

    List<ProductQuantity> mapToProductQuantities(
            PurchaseInputDto purchaseInputDto,
            Function<PurchaseProductInputDto, Optional<ProductQuantity>> mapToOptionalOfProductQuantity) {
        return purchaseInputDto.purchaseProducts()
                .stream()
                .map(mapToOptionalOfProductQuantity)
                .flatMap(Optional::stream)
                .toList();
    }
}