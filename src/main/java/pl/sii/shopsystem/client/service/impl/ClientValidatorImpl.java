package pl.sii.shopsystem.client.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.sii.shopsystem.client.exception.ClientException;
import pl.sii.shopsystem.client.repository.ClientRepository;
import pl.sii.shopsystem.client.service.ClientValidator;
import pl.sii.shopsystem.client.dto.ClientInputDto;
import pl.sii.shopsystem.client.dto.ClientEmailInputDto;

import static pl.sii.shopsystem.client.exception.ClientExceptionMessages.*;

@Component
class ClientValidatorImpl implements ClientValidator {

    private final ClientRepository clientRepository;

    public ClientValidatorImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void validateClientInputDto(ClientInputDto clientInputDto) {
        if (isAnyBlank(clientInputDto)) {
            throw new ClientException(INPUT_DATA_CONTAINS_BLANK_FIELDS.getMessage());
        }
    }

    @Override
    public void validateClientEmailInputDto(ClientEmailInputDto clientEmailInputDto) {
        if (isAnyBlank(clientEmailInputDto)) {
            throw new ClientException(INPUT_DATA_CONTAINS_BLANK_FIELDS.getMessage());
        }
    }

    @Override
    public void validateClientExistence(String email) {
        if (clientRepository.existsByEmail(email)) {
            throw new ClientException(CLIENT_ALREADY_EXISTS.getMessage());
        }
    }

    @Override
    public void validateEmailChange(String oldEmail, String newEmail) {
        if (!oldEmail.equals(newEmail) && clientRepository.existsByEmail(oldEmail)) {
            throw new ClientException(EMAIL_ALREADY_USED.getMessage());
        }
    }

    private boolean isAnyBlank(ClientInputDto clientInputDto) {
        return StringUtils.isAnyBlank(clientInputDto.firstname(), clientInputDto.lastname(), clientInputDto.email());
    }

    private boolean isAnyBlank(ClientEmailInputDto clientEmailInputDto) {
        return StringUtils.isAnyBlank(clientEmailInputDto.email());
    }
}
