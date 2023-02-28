package pl.sii.shopsystem.order.service.impl;

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
import pl.sii.shopsystem.order.model.Order;
import pl.sii.shopsystem.order.orderProduct.model.OrderProduct;
import pl.sii.shopsystem.order.repository.OrderRepository;
import pl.sii.shopsystem.product.model.Product;
import pl.sii.shopsystem.product.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderIntegrationTest {

    @LocalServerPort
    int port;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void beforeEach() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("Making order happy path")
    void makingOrderHappyPath() {
        assertThat(orderRepository.findAll()).isEmpty();
        Customer customer = addCustomerToDatabase();
        Product product = addProductToDatabase();
        int quantity = 10;

        given().accept(JSON).body(String.format("""
                                {
                                    "customerId": "%s",
                                    "orderProducts": [
                                        {
                                            "productId": "%s",
                                            "quantity": 10
                                        }
                                    ]
                                }
                                """, customer.getId().toString(),
                        product.getId().toString()))
                .contentType(JSON)
                .when().post("/api/v1/order")
                .then().statusCode(HttpStatus.SC_OK)
                .body("customerFirstname", equalTo(customer.getFirstname()))
                .body("customerLastname", equalTo(customer.getLastname()))
                .body("customerEmail", equalTo(customer.getEmail()))
                .body("orderedProducts.size()", equalTo(1))
                .body("orderedProducts[0].title", equalTo(product.getTitle()))
                .body("orderedProducts[0].type", equalTo(product.getType()))
                .body("orderedProducts[0].manufacturer", equalTo(product.getManufacturer()))
                .body("orderedProducts[0].price", equalTo(product.getPrice().floatValue()))
                .body("orderedProducts[0].quantity", equalTo(quantity))
                .body("totalCost", equalTo(
                        product.getPrice().multiply(new BigDecimal(quantity)).floatValue()));

        assertThat(orderRepository.findAll()).hasSize(1);
        List<Order> orders = orderRepository.findAll();
        List<OrderProduct> orderProducts = new ArrayList<>(orders.get(0).getOrderProducts());
        assertThat(orders).isNotEmpty();
        assertEquals(orders.get(0).getCustomer().getFirstname(), customer.getFirstname());
        assertEquals(orders.get(0).getCustomer().getLastname(), customer.getLastname());
        assertEquals(orders.get(0).getCustomer().getEmail(), customer.getEmail());
        assertEquals(orderProducts.size(), 1);
        assertEquals(orderProducts.get(0).getProduct(), product);
        assertEquals(orderProducts.get(0).getQuantity(), quantity);
        assertEquals(orders.get(0).getCost(), product.getPrice().multiply(new BigDecimal(quantity)));
    }

    private Customer addCustomerToDatabase() {
        Customer customer = Customer.builder()
                .firstname("John")
                .lastname("Doe")
                .email("jdoe@sii.pl")
                .build();
        return customerRepository.save(customer);
    }

    private Product addProductToDatabase() {
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .title("Pixel 6")
                .type("Smartphone")
                .manufacturer("Google")
                .price(new BigDecimal("479.99"))
                .build();
        return productRepository.save(product);
    }
}