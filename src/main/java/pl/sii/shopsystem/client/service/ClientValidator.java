package pl.sii.shopsystem.client.service;

import pl.sii.shopsystem.client.dto.ClientEmailInputDto;
import pl.sii.shopsystem.client.dto.ClientInputDto;

public interface ClientValidator {
    void validateClientInputDto(ClientInputDto clientInputDto);
    void validateClientEmailInputDto(ClientEmailInputDto clientEmailInputDto);
    void validateClientExistence(String email);
    void validateEmailChange(String oldEmail, String newEmail);
}
