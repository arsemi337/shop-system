package pl.sii.shopsystem.purchase.persistence;

import jakarta.persistence.*;
import lombok.*;
import pl.sii.shopsystem.client.persistence.Client;
import pl.sii.shopsystem.purchase.purchaseProduct.persistence.PurchaseProduct;

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
public class Purchase {

    @Id
    @GeneratedValue
    @Column
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
    @Column
    private LocalDateTime dateTime;
    @Column
    private BigDecimal cost;

    @OneToMany(mappedBy = "purchase")
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
