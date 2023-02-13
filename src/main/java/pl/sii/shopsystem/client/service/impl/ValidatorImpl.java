package pl.sii.shopsystem.client.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.sii.shopsystem.purchase.dto.PurchaseInputDto;
import pl.sii.shopsystem.client.dto.ClientInputDto;
import pl.sii.shopsystem.client.dto.ClientEmailInputDto;
import pl.sii.shopsystem.client.service.Validator;

@Component
public class ValidatorImpl implements Validator {
    @Override
    public boolean isAnyBlank(ClientInputDto clientInputDto) {
        return StringUtils.isAnyBlank(clientInputDto.firstname(), clientInputDto.lastname(), clientInputDto.email());
    }

    @Override
    public boolean isAnyBlank(ClientEmailInputDto clientEmailInputDto) {
        return StringUtils.isAnyBlank(clientEmailInputDto.email());
    }

    @Override
    public boolean isAnyBlank(PurchaseInputDto purchaseInputDto) {
        return StringUtils.isAnyBlank(purchaseInputDto.userId(), purchaseInputDto.productId());
    }
}
