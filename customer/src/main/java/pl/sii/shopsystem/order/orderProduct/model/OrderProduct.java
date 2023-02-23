package pl.sii.shopsystem.order.orderProduct.model;

import jakarta.persistence.*;
import lombok.*;
import pl.sii.shopsystem.order.model.Order;
import pl.sii.shopsystem.product.model.Product;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_products")
public class OrderProduct {

    @EmbeddedId
    private OrderProductKey orderProductKey;
    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    Product product;
    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    Order order;
    private Integer quantity;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
