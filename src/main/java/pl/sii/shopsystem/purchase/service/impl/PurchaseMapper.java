package pl.sii.shopsystem.purchase.service.impl;

import pl.sii.shopsystem.client.persistence.Client;
import pl.sii.shopsystem.purchase.dto.PurchaseOutputDto;
import pl.sii.shopsystem.purchase.purchaseProduct.dto.PurchaseProductOutputDto;

import java.math.BigDecimal;
import java.util.List;

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
}