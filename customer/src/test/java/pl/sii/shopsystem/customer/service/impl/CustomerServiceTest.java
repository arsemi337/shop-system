package pl.sii.shopsystem.customer.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sii.shopsystem.customer.dto.CustomerInputDto;
import pl.sii.shopsystem.customer.dto.CustomerOutputDto;
import pl.sii.shopsystem.customer.model.Customer;
import pl.sii.shopsystem.customer.repository.CustomerRepository;
import pl.sii.shopsystem.customer.service.CustomerValidator;
import supplier.TimeSupplier;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @InjectMocks
    CustomerServiceImpl underTest;
    @Mock
    CustomerValidator validator;
    @Mock
    TimeSupplier timeSupplier;
    @Mock
    CustomerRepository customerRepository;

    @Test
    @DisplayName("when customer data is passed, a newly created customer is returned")
    void addCustomer_should_createCustomer_when_validDataIsPassed() {
        // given
        CustomerInputDto input = CustomerInputDto.builder()
                .firstname("John")
                .lastname("Doe")
                .email("jdoe@sii.pl")
                .build();
        when(timeSupplier.get()).thenReturn(LocalDateTime.of(2010, 1, 1, 1, 10, 30));
        when(customerRepository.save(any(Customer.class))).thenAnswer(answer -> answer.getArgument(0));

        // when
        CustomerOutputDto customerOutput = underTest.addCustomer(input);

        // then
        assertEquals(input.firstname(), customerOutput.firstname());
        assertEquals(input.lastname(), customerOutput.lastname());
        assertEquals(input.email(), customerOutput.email());
        verify(customerRepository).save(any(Customer.class));
    }
}
