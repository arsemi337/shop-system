package pl.sii.shopsystem.purchase.purchaseProduct.persistence;

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
public class PurchaseProductKey implements Serializable {

    @Column(name = "product_id")
    private UUID productId;
    @Column(name = "purchase_id")
    private UUID purchaseId;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
