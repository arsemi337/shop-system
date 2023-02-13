package pl.sii.shopsystem.client.service;

import pl.sii.shopsystem.client.dto.*;
import pl.sii.shopsystem.purchase.dto.PurchaseInputDto;
import pl.sii.shopsystem.purchase.dto.PurchaseOutputDto;

public interface ClientService {
    ClientOutputDto addClient(ClientInputDto clientInputDto);

    ClientOutputDto getClient(ClientEmailInputDto clientEmailInputDto);

    void removeClient(ClientEmailInputDto clientEmailInputDto);

    ClientOutputDto updateClient(ClientInputDto clientInputDto);

    PurchaseOutputDto makePurchase(PurchaseInputDto purchaseInputDto);
}
