package pl.artur.shopsystem.customer.service.impl;

import pl.artur.shopsystem.customer.dto.CustomerInputDto;
import pl.artur.shopsystem.customer.dto.CustomerOutputDto;
import pl.artur.shopsystem.customer.model.Customer;
import supplier.TimeSupplier;

public class CustomerMapper {

    private final TimeSupplier timeSupplier;

    public CustomerMapper(TimeSupplier timeSupplier) {
        this.timeSupplier = timeSupplier;
    }

    public Customer mapToCustomer(CustomerInputDto customerInputDto) {
        return Customer.builder()
                .creationTime(timeSupplier.get())
                .firstname(customerInputDto.firstname())
                .lastname(customerInputDto.lastname())
                .email(customerInputDto.email())
                .build();
    }

    public CustomerOutputDto mapToCustomerOutputDto(Customer customer) {
        return CustomerOutputDto.builder()
                .id(customer.getId())
                .firstname(customer.getFirstname())
                .lastname(customer.getLastname())
                .email(customer.getEmail())
                .build();
    }
}
