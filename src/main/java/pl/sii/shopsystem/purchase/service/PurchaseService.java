package pl.sii.shopsystem.purchase.service;

import pl.sii.shopsystem.purchase.dto.PurchaseInputDto;
import pl.sii.shopsystem.purchase.dto.PurchaseOutputDto;

public interface PurchaseService {

    PurchaseOutputDto makePurchase(PurchaseInputDto purchaseInputDto);
}
