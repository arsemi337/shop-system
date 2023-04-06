package pl.artur.shopsystem.customer.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.artur.shopsystem.customer.dto.CustomerEmailInputDto;
import pl.artur.shopsystem.customer.dto.CustomerInputDto;
import pl.artur.shopsystem.customer.dto.CustomerOutputDto;
import pl.artur.shopsystem.customer.dto.UpdateCustomerInputDto;
import pl.artur.shopsystem.customer.service.CustomerService;

@RestController
@Tag(name = "Customer")
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    ResponseEntity<CustomerOutputDto> addCustomer(@RequestBody CustomerInputDto customerInputDto) {
        return ResponseEntity.ok(customerService.addCustomer(customerInputDto));
    }

    @GetMapping
    ResponseEntity<CustomerOutputDto> getCustomerByEmail(@RequestBody CustomerEmailInputDto customerEmailInputDto) {
        return ResponseEntity.ok(customerService.getCustomerByEmail(customerEmailInputDto));
    }

    @GetMapping("/{id}")
    ResponseEntity<CustomerOutputDto> getCustomerById(@PathVariable String id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @DeleteMapping
    void removeCustomer(@RequestBody CustomerEmailInputDto customerEmailInputDto) {
        customerService.removeCustomer(customerEmailInputDto);
    }

    @PutMapping
    ResponseEntity<CustomerOutputDto> updateCustomer(@RequestBody UpdateCustomerInputDto updateCustomerInputDto) {
        return ResponseEntity.ok(customerService.updateCustomer(updateCustomerInputDto));
    }
}
