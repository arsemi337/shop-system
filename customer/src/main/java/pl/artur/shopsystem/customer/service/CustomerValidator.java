package pl.artur.shopsystem.customer.service;

import pl.artur.shopsystem.customer.dto.CustomerEmailInputDto;
import pl.artur.shopsystem.customer.dto.CustomerInputDto;
import pl.artur.shopsystem.customer.dto.UpdateCustomerInputDto;

public interface CustomerValidator {
    void validateCustomerInputDto(CustomerInputDto customerInputDto);
    void validateUpdateCustomerInputDto(UpdateCustomerInputDto updateCustomerInputDto);
    void validateCustomerEmailInputDto(CustomerEmailInputDto customerEmailInputDto);
    void validateCustomerExistence(String email);
    void validateEmailChange(String oldEmail, String newEmail);
}
