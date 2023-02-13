package pl.sii.shopsystem.purchase.purchaseProduct.persistence;

import jakarta.persistence.*;
import lombok.*;
import pl.sii.shopsystem.product.persistence.Product;
import pl.sii.shopsystem.purchase.persistence.Purchase;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseProduct {

    @EmbeddedId
    private PurchaseProductKey purchaseProductKey;
    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    Product product;
    @ManyToOne
    @MapsId("purchaseId")
    @JoinColumn(name = "purchase_id")
    Purchase purchase;
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
