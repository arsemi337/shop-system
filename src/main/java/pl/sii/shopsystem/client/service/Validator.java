package pl.sii.shopsystem.client.service;

import pl.sii.shopsystem.purchase.dto.PurchaseInputDto;
import pl.sii.shopsystem.client.dto.ClientInputDto;
import pl.sii.shopsystem.client.dto.ClientEmailInputDto;

public interface Validator {

    boolean isAnyBlank(ClientInputDto clientInputDto);

    boolean isAnyBlank(ClientEmailInputDto clientEmailInputDto);

    boolean isAnyBlank(PurchaseInputDto purchaseInputDto);
}
