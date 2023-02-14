package pl.sii.shopsystem.purchase.dto;

import lombok.Builder;
import pl.sii.shopsystem.purchase.purchaseProduct.dto.PurchaseProductOutputDto;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record PurchaseOutputDto(
        String clientFirstname,
        String clientLastname,
        String clientEmail,
        List<PurchaseProductOutputDto> purchasedProducts,
        BigDecimal totalCost) {
}
