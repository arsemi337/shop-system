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
@SQLDelete(sql = "UPDATE Customers SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;
    @Column(name = "creation_time")
    private LocalDateTime creationTime;
    private String firstname;
    private String lastname;
    private String email;
    @Column(name = "is_deleted")
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
