package pl.sii.shopsystem.order.model;

import jakarta.persistence.*;
import lombok.*;
import pl.sii.shopsystem.customer.model.Customer;
import pl.sii.shopsystem.order.orderProduct.model.OrderProduct;

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
    private LocalDateTime creationTime;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    private LocalDateTime dateTime;
    private BigDecimal cost;

    @OneToMany(mappedBy = "order")
    Set<OrderProduct> orderProducts;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
