package pl.artur.shopsystem.customer.service;

import pl.artur.shopsystem.customer.dto.CustomerEmailInputDto;
import pl.artur.shopsystem.customer.dto.CustomerInputDto;
import pl.artur.shopsystem.customer.dto.CustomerOutputDto;
import pl.artur.shopsystem.customer.dto.UpdateCustomerInputDto;

public interface CustomerService {
    CustomerOutputDto addCustomer(CustomerInputDto customerInputDto);

    CustomerOutputDto getCustomer(String id);

    void removeCustomer(CustomerEmailInputDto customerEmailInputDto);

    CustomerOutputDto updateCustomer(UpdateCustomerInputDto updateCustomerInputDto);
}
