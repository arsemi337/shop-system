package pl.sii.shopsystem.customer.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.sii.shopsystem.customer.dto.UpdateCustomerInputDto;
import pl.sii.shopsystem.customer.exception.CustomerException;
import pl.sii.shopsystem.customer.repository.CustomerRepository;
import pl.sii.shopsystem.customer.service.CustomerValidator;
import pl.sii.shopsystem.customer.dto.CustomerInputDto;
import pl.sii.shopsystem.customer.dto.CustomerEmailInputDto;

import static pl.sii.shopsystem.customer.exception.CustomerExceptionMessages.*;

@Component
class CustomerValidatorImpl implements CustomerValidator {

    private final CustomerRepository customerRepository;

    public CustomerValidatorImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void validateCustomerInputDto(CustomerInputDto customerInputDto) {
        if (isAnyBlank(customerInputDto)) {
            throw new CustomerException(INPUT_DATA_CONTAINS_BLANK_FIELDS.getMessage());
        }
    }

    @Override
    public void validateUpdateCustomerInputDto(UpdateCustomerInputDto updateCustomerInputDto) {
        if (isAnyBlank(updateCustomerInputDto)) {
            throw new CustomerException(INPUT_DATA_CONTAINS_BLANK_FIELDS.getMessage());
        }
    }

    @Override
    public void validateCustomerEmailInputDto(CustomerEmailInputDto customerEmailInputDto) {
        if (isAnyBlank(customerEmailInputDto)) {
            throw new CustomerException(INPUT_DATA_CONTAINS_BLANK_FIELDS.getMessage());
        }
    }

    @Override
    public void validateCustomerExistence(String email) {
        if (customerRepository.existsByEmail(email)) {
            throw new CustomerException(CUSTOMER_ALREADY_EXISTS.getMessage());
        }
    }

    @Override
    public void validateEmailChange(String oldEmail, String newEmail) {
        if (oldEmail.equals(newEmail)) {
            return;
        }
        if (customerRepository.existsByEmail(newEmail)) {
            throw new CustomerException(EMAIL_ALREADY_USED.getMessage());
        }
    }

    private boolean isAnyBlank(CustomerInputDto customerInputDto) {
        return StringUtils.isAnyBlank(customerInputDto.firstname(), customerInputDto.lastname(), customerInputDto.email());
    }

    private boolean isAnyBlank(UpdateCustomerInputDto updateCustomerInputDto) {
        return StringUtils.isAnyBlank(
                updateCustomerInputDto.id(),
                updateCustomerInputDto.newFirstname(),
                updateCustomerInputDto.newLastname(),
                updateCustomerInputDto.newEmail());
    }

    private boolean isAnyBlank(CustomerEmailInputDto customerEmailInputDto) {
        return StringUtils.isAnyBlank(customerEmailInputDto.email());
    }
}
