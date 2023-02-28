package pl.sii.shopsystem.customer;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import pl.sii.shopsystem.customer.model.Customer;
import pl.sii.shopsystem.customer.repository.CustomerRepository;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CustomerIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void beforeEach() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("Adding customer happy path")
    void addingCustomerHappyPath() {
        assertThat(customerRepository.findAll()).isEmpty();

        given().accept(JSON).body("""
                        {
                            "firstname": "John",
                            "lastname": "Doe",
                            "email": "jdoe@sii.pl"
                        }
                        """)
                .contentType(JSON)
                .when().post("/api/v1/customer")
                .then().statusCode(HttpStatus.SC_OK)
                .body("firstname", equalTo("John"))
                .body("lastname", equalTo("Doe"))
                .body("email", equalTo("jdoe@sii.pl"));

        assertThat(customerRepository.findAll()).hasSize(1);
        Optional<Customer> customer = customerRepository.findByEmail("jdoe@sii.pl");
        assertThat(customer).isNotEmpty();
        assertEquals(customer.get().getFirstname(), "John");
        assertEquals(customer.get().getLastname(), "Doe");
        assertEquals(customer.get().getEmail(), "jdoe@sii.pl");
    }

    @Test
    @DisplayName("Fetching customer happy path")
    void fetchingCustomerHappyPath() {
        assertThat(customerRepository.findAll()).isEmpty();
        Customer customer = addCustomerToDatabase();
        assertThat(customerRepository.findAll()).hasSize(1);

        given()
                .pathParams("id", customer.getId())
                .when().get("/api/v1/customer/{id}")
                .then().statusCode(HttpStatus.SC_OK)
                .body("firstname", equalTo("John"))
                .body("lastname", equalTo("Doe"))
                .body("email", equalTo("jdoe@sii.pl"));

        assertThat(customerRepository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("Removing customer happy path")
    void removingCustomerHappyPath() {
        assertThat(customerRepository.findAll()).isEmpty();
        addCustomerToDatabase();
        assertThat(customerRepository.findAll()).hasSize(1);

        given().accept(JSON).body("""
                        {
                            "email": "jdoe@sii.pl"
                        }
                        """)
                .contentType(JSON)
                .when().delete("/api/v1/customer")
                .then().statusCode(HttpStatus.SC_OK);

        assertThat(customerRepository.findAll()).hasSize(0);
    }

    @Test
    @DisplayName("Updating customer happy path")
    void updatingCustomerHappyPath() {
        assertThat(customerRepository.findAll()).isEmpty();
        Customer customer = addCustomerToDatabase();
        assertThat(customerRepository.findAll()).hasSize(1);

        given().accept(JSON).body(String.format("""
                        {
                            "id": "%s",
                            "newFirstname": "Mark",
                            "newLastname": "Johnson",
                            "newEmail": "mjohnson@sii.pl"
                        }
                        """, customer.getId()))
                .contentType(JSON)
                .when().put("/api/v1/customer")
                .then().statusCode(HttpStatus.SC_OK)
                .body("firstname", equalTo("Mark"))
                .body("lastname", equalTo("Johnson"))
                .body("email", equalTo("mjohnson@sii.pl"));

        assertThat(customerRepository.findAll()).hasSize(1);
        Optional<Customer> optCustomer = customerRepository.findByEmail("mjohnson@sii.pl");
        assertThat(optCustomer).isNotEmpty();
        assertEquals(optCustomer.get().getFirstname(), "Mark");
        assertEquals(optCustomer.get().getLastname(), "Johnson");
        assertEquals(optCustomer.get().getEmail(), "mjohnson@sii.pl");
    }

    private Customer addCustomerToDatabase() {
        Customer customer = Customer.builder()
                .firstname("John")
                .lastname("Doe")
                .email("jdoe@sii.pl")
                .build();
        return customerRepository.save(customer);
    }
}
