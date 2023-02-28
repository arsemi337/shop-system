package pl.sii.shopsystem.product.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import pl.sii.shopsystem.order.orderProduct.model.OrderProduct;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Product product = (Product) o;
        return getId() != null && Objects.equals(getId(), product.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
