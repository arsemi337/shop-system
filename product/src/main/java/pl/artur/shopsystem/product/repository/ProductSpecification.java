package pl.artur.shopsystem.product.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;
import pl.artur.shopsystem.product.model.Product;
import pl.artur.shopsystem.product.service.impl.SearchCriteria;

import java.time.LocalDateTime;

@Builder
public record ProductSpecification(SearchCriteria criteria) implements Specification<Product> {

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        if (criteria.getOperation().equalsIgnoreCase(">")) {
            if (criteria.getValue() instanceof LocalDateTime localDateTime) {
                return builder.greaterThanOrEqualTo(
                        root.get(criteria.getKey()), localDateTime);
            }
            return builder.greaterThanOrEqualTo(
                    root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase("<")) {
            if (criteria.getValue() instanceof LocalDateTime localDateTime) {
                return builder.lessThanOrEqualTo(
                        root.get(criteria.getKey()), localDateTime);
            }
            return builder.lessThanOrEqualTo(
                    root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase("=")) {
            return builder.equal(
                    root.get(criteria.getKey()), criteria.getValue());
        }
        return null;
    }
}
