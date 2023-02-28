package pl.sii.shopsystem.customer.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sii.shopsystem.customer.dto.*;
import pl.sii.shopsystem.customer.service.CustomerService;

@RestController
@Tag(name = "Customer")
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    ResponseEntity<CustomerOutputDto> addCustomer(@RequestBody CustomerInputDto customerInputDto) {
        return ResponseEntity.ok(customerService.addCustomer(customerInputDto));
    }

    @GetMapping("/{id}")
    ResponseEntity<CustomerOutputDto> getCustomer(@PathVariable String id) {
        return ResponseEntity.ok(customerService.getCustomer(id));
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
