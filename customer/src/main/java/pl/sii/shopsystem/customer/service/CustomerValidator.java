package pl.sii.shopsystem.customer.service;

import pl.sii.shopsystem.customer.dto.CustomerEmailInputDto;
import pl.sii.shopsystem.customer.dto.CustomerInputDto;
import pl.sii.shopsystem.customer.dto.UpdateCustomerInputDto;

public interface CustomerValidator {
    void validateCustomerInputDto(CustomerInputDto customerInputDto);
    void validateUpdateCustomerInputDto(UpdateCustomerInputDto updateCustomerInputDto);
    void validateCustomerEmailInputDto(CustomerEmailInputDto customerEmailInputDto);
    void validateCustomerExistence(String email);
    void validateEmailChange(String oldEmail, String newEmail);
}
