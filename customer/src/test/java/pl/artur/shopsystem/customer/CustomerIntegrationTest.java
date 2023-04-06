package pl.artur.shopsystem.customer;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import pl.artur.shopsystem.customer.model.Customer;
import pl.artur.shopsystem.customer.repository.CustomerRepository;

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
                            "email": "jdoe@email.pl"
                        }
                        """)
                .contentType(JSON)
                .when().post("/api/v1/customers")
                .then().statusCode(HttpStatus.SC_OK)
                .body("firstname", equalTo("John"))
                .body("lastname", equalTo("Doe"))
                .body("email", equalTo("jdoe@email.pl"));

        assertThat(customerRepository.findAll()).hasSize(1);
        Optional<Customer> customer = customerRepository.findByEmail("jdoe@email.pl");
        assertThat(customer).isNotEmpty();
        assertEquals(customer.get().getFirstname(), "John");
        assertEquals(customer.get().getLastname(), "Doe");
        assertEquals(customer.get().getEmail(), "jdoe@email.pl");
    }

    @Test
    @DisplayName("Fetching customer by email happy path")
    void fetchingCustomerByEmailHappyPath() {
        assertThat(customerRepository.findAll()).isEmpty();
        addCustomerToDatabase();
        assertThat(customerRepository.findAll()).hasSize(1);

        given()
                .body("""
                        {
                            "email": "jdoe@email.pl"
                        }
                        """)
                .contentType(JSON)
                .when().get("/api/v1/customers")
                .then().statusCode(HttpStatus.SC_OK)
                .body("firstname", equalTo("John"))
                .body("lastname", equalTo("Doe"))
                .body("email", equalTo("jdoe@email.pl"));

        assertThat(customerRepository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("Fetching customer by ID happy path")
    void fetchingCustomerByIdHappyPath() {
        assertThat(customerRepository.findAll()).isEmpty();
        Customer customer = addCustomerToDatabase();
        assertThat(customerRepository.findAll()).hasSize(1);

        given()
                .pathParams("id", customer.getId())
                .when().get("/api/v1/customers/{id}")
                .then().statusCode(HttpStatus.SC_OK)
                .body("firstname", equalTo("John"))
                .body("lastname", equalTo("Doe"))
                .body("email", equalTo("jdoe@email.pl"));

        assertThat(customerRepository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("Removing customer happy path")
    void removingCustomerHappyPath() {
        assertThat(customerRepository.findAll()).isEmpty();
        addCustomerToDatabase();
        assertThat(customerRepository.findAll()).hasSize(1);

        given()
                .accept(JSON)
                .body("""
                        {
                            "email": "jdoe@email.pl"
                        }
                        """)
                .contentType(JSON)
                .when().delete("/api/v1/customers")
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
                            "newEmail": "mjohnson@email.pl"
                        }
                        """, customer.getId()))
                .contentType(JSON)
                .when().put("/api/v1/customers")
                .then().statusCode(HttpStatus.SC_OK)
                .body("firstname", equalTo("Mark"))
                .body("lastname", equalTo("Johnson"))
                .body("email", equalTo("mjohnson@email.pl"));

        assertThat(customerRepository.findAll()).hasSize(1);
        Optional<Customer> optCustomer = customerRepository.findByEmail("mjohnson@email.pl");
        assertThat(optCustomer).isNotEmpty();
        assertEquals(optCustomer.get().getFirstname(), "Mark");
        assertEquals(optCustomer.get().getLastname(), "Johnson");
        assertEquals(optCustomer.get().getEmail(), "mjohnson@email.pl");
    }

    private Customer addCustomerToDatabase() {
        Customer customer = Customer.builder()
                .firstname("John")
                .lastname("Doe")
                .email("jdoe@email.pl")
                .build();
        return customerRepository.save(customer);
    }
}
