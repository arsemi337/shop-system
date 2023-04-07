package pl.artur.shopsystem.order.model;

import jakarta.persistence.*;
import lombok.*;
import pl.artur.shopsystem.customer.model.Customer;
//import pl.artur.shopsystem.order.orderProduct.model.OrderProduct;
import pl.artur.shopsystem.product.model.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    private LocalDateTime creationTime;
    private BigDecimal totalCost;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order")
    Set<Product> products;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
