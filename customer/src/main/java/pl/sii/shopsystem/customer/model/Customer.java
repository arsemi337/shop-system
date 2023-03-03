package pl.sii.shopsystem.customer.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE customers SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue
    private UUID id;
    private LocalDateTime creationTime;
    private String firstname;
    private String lastname;
    private String email;
    private boolean isDeleted = Boolean.FALSE;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
