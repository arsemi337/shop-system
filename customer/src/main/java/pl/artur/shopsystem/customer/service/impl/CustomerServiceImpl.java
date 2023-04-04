package pl.artur.shopsystem.customer.service.impl;

import org.springframework.stereotype.Service;
import pl.artur.shopsystem.customer.dto.CustomerEmailInputDto;
import pl.artur.shopsystem.customer.dto.CustomerInputDto;
import pl.artur.shopsystem.customer.dto.CustomerOutputDto;
import pl.artur.shopsystem.customer.dto.UpdateCustomerInputDto;
import pl.artur.shopsystem.customer.model.Customer;
import pl.artur.shopsystem.customer.repository.CustomerRepository;
import pl.artur.shopsystem.customer.service.CustomerService;
import pl.artur.shopsystem.customer.service.CustomerValidator;
import supplier.TimeSupplier;

import java.util.NoSuchElementException;
import java.util.UUID;

import static exception.CustomerExceptionMessages.NO_CUSTOMER_BY_EMAIL_FOUND;
import static exception.CustomerExceptionMessages.NO_CUSTOMER_BY_ID_FOUND;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerValidator validator;
    private final CustomerMapper mapper;
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerValidator validator,
                               TimeSupplier timeSupplier,
                               CustomerRepository customerRepository) {
        this.validator = validator;
        this.mapper = new CustomerMapper(timeSupplier);
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerOutputDto addCustomer(CustomerInputDto customerInputDto) {
        validator.validateCustomerInputDto(customerInputDto);
        validator.validateCustomerExistence(customerInputDto.email());

        Customer customer = customerRepository.save(
                mapper.mapToCustomer(customerInputDto));

        return mapper.mapToCustomerOutputDto(customer);
    }

    @Override
    public CustomerOutputDto getCustomer(String id) {
        Customer customer = customerRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NoSuchElementException(NO_CUSTOMER_BY_EMAIL_FOUND.getMessage()));

        return mapper.mapToCustomerOutputDto(customer);
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

        return mapper.mapToCustomerOutputDto(customer);
    }
}
