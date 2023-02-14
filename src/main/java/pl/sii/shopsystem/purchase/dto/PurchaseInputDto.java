package pl.sii.shopsystem.purchase.dto;

import lombok.Builder;
import pl.sii.shopsystem.purchase.purchaseProduct.dto.PurchaseProductInputDto;

import java.util.List;

@Builder
public record PurchaseInputDto(String userId, List<PurchaseProductInputDto> purchaseProducts) {
}
