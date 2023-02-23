package pl.sii.shopsystem.order.orderProduct.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductKey implements Serializable {

    @Column(name = "product_id")
    private UUID productId;
    @Column(name = "order_id")
    private UUID orderId;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
