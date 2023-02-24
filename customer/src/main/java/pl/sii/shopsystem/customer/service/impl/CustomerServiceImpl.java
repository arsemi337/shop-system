package pl.sii.shopsystem.customer.service.impl;

import org.springframework.stereotype.Service;
import pl.sii.shopsystem.customer.dto.CustomerEmailInputDto;
import pl.sii.shopsystem.customer.dto.CustomerInputDto;
import pl.sii.shopsystem.customer.dto.CustomerOutputDto;
import pl.sii.shopsystem.customer.dto.UpdateCustomerInputDto;
import pl.sii.shopsystem.customer.model.Customer;
import pl.sii.shopsystem.customer.repository.CustomerRepository;
import pl.sii.shopsystem.customer.service.CustomerService;
import pl.sii.shopsystem.customer.service.CustomerValidator;
import supplier.TimeSupplier;

import java.util.NoSuchElementException;
import java.util.UUID;

import static exception.CustomerExceptionMessages.NO_CUSTOMER_BY_EMAIL_FOUND;
import static exception.CustomerExceptionMessages.NO_CUSTOMER_BY_ID_FOUND;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerValidator validator;
    private final TimeSupplier timeSupplier;
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerValidator validator,
                               TimeSupplier timeSupplier,
                               CustomerRepository customerRepository) {
        this.validator = validator;
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

        Customer customer = customerRepository.findByEmail(customerEmailInputDto.email())
                .orElseThrow(() -> new NoSuchElementException(NO_CUSTOMER_BY_EMAIL_FOUND.getMessage()));

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

        Customer customer = customerRepository.findByEmail(customerEmailInputDto.email())
                .orElseThrow(() -> new NoSuchElementException(NO_CUSTOMER_BY_EMAIL_FOUND.getMessage()));
        customerRepository.deleteById(customer.getId());
    }

    @Override
    public CustomerOutputDto updateCustomer(UpdateCustomerInputDto updateCustomerInputDto) {
        validator.validateUpdateCustomerInputDto(updateCustomerInputDto);

        UUID id = UUID.fromString(updateCustomerInputDto.id());
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(NO_CUSTOMER_BY_ID_FOUND.getMessage()));

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
}
