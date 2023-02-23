package pl.sii.shopsystem.customer.service.impl;

import org.springframework.stereotype.Service;
import pl.sii.shopsystem.common.TimeSupplier;
import pl.sii.shopsystem.customer.dto.*;
import pl.sii.shopsystem.customer.exception.CustomerException;
import pl.sii.shopsystem.customer.model.*;
import pl.sii.shopsystem.customer.repository.CustomerRepository;
import pl.sii.shopsystem.customer.service.CustomerService;
import pl.sii.shopsystem.customer.service.CustomerValidator;

import java.util.UUID;

import static pl.sii.shopsystem.customer.exception.CustomerExceptionMessages.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerValidator validator;
    private final CustomerParser customerParser;
    private final TimeSupplier timeSupplier;
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerValidator validator,
                               TimeSupplier timeSupplier,
                               CustomerRepository customerRepository) {
        this.validator = validator;
        this.customerParser = new CustomerParser();
        this.timeSupplier = timeSupplier;
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerOutputDto addCustomer(CustomerInputDto customerInputDto) {
        validator.validateCustomerInputDto(customerInputDto);
        validator.validateCustomerExistence(customerInputDto.email());

        Customer customer = customerRepository.save(
                Customer.builder()
                        .creationTime(timeSupplier.get())
                        .firstname(customerInputDto.firstname())
                        .lastname(customerInputDto.lastname())
                        .email(customerInputDto.email())
                        .build());

        return CustomerOutputDto.builder()
                .id(customer.getId())
                .firstname(customer.getFirstname())
                .lastname(customer.getLastname())
                .email(customer.getEmail())
                .build();
    }

    @Override
    public CustomerOutputDto getCustomer(CustomerEmailInputDto customerEmailInputDto) {
        validator.validateCustomerEmailInputDto(customerEmailInputDto);

        Customer customer = getCustomerByEmail(customerEmailInputDto.email());

        return CustomerOutputDto.builder()
                .id(customer.getId())
                .firstname(customer.getFirstname())
                .lastname(customer.getLastname())
                .email(customer.getEmail())
                .build();
    }

    @Override
    public void removeCustomer(CustomerEmailInputDto customerEmailInputDto) {
        validator.validateCustomerEmailInputDto(customerEmailInputDto);

        Customer customer = getCustomerByEmail(customerEmailInputDto.email());
        customerRepository.deleteById(customer.getId());
    }

    @Override
    public CustomerOutputDto updateCustomer(UpdateCustomerInputDto updateCustomerInputDto) {
        validator.validateUpdateCustomerInputDto(updateCustomerInputDto);

        UUID id = customerParser.parseUUID(updateCustomerInputDto.id());
        Customer customer = customerRepository.findById(id)
                        .orElseThrow(() -> new CustomerException(NO_CUSTOMER_BY_ID_FOUND.getMessage()));

        validator.validateEmailChange(customer.getEmail(), updateCustomerInputDto.newEmail());

        customer.setFirstname(updateCustomerInputDto.newFirstname());
        customer.setLastname(updateCustomerInputDto.newLastname());
        customer.setEmail(updateCustomerInputDto.newEmail());

        customerRepository.save(customer);

        return CustomerOutputDto.builder()
                .id(customer.getId())
                .firstname(customer.getFirstname())
                .lastname(customer.getLastname())
                .email(customer.getEmail())
                .build();
    }

    private Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomerException(NO_CUSTOMER_BY_EMAIL_FOUND.getMessage()));
    }
}
