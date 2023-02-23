package pl.sii.shopsystem.product.model;

import jakarta.persistence.*;
import lombok.*;
import pl.sii.shopsystem.order.orderProduct.model.OrderProduct;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue
    private UUID id;
    private LocalDateTime creationTime;
    private String type;
    private String title;
    private String manufacturer;
    private BigDecimal price;

    @OneToMany(mappedBy = "product")
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
