package pl.sii.shopsystem.product.persistence;

import jakarta.persistence.*;
import lombok.*;
import pl.sii.shopsystem.purchase.purchaseProduct.persistence.PurchaseProduct;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue
    @Column
    private UUID id;
    @Column
    private String type;
    @Column
    private String title;
    @Column
    private String manufacturer;
    @Column
    private String price;

    @OneToMany(mappedBy = "product")
    Set<PurchaseProduct> purchaseProducts;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
