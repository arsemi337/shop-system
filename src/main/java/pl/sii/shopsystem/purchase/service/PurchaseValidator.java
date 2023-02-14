package pl.sii.shopsystem.purchase.service;

import pl.sii.shopsystem.purchase.dto.PurchaseInputDto;

public interface PurchaseValidator {

    void validatePurchaseInputDto(PurchaseInputDto purchaseInputDto);
}
