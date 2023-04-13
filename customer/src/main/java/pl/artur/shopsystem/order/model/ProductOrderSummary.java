package pl.artur.shopsystem.order.model;

import lombok.*;
import product.model.Genre;

@Getter
@Setter
@Builder
public class ProductOrderSummary {
    private String name;
    private Genre type;
    private String manufacturer;
    private long number;

    public ProductOrderSummary(String name, Genre type, String manufacturer, long number) {
        this.name = name;
        this.type = type;
        this.manufacturer = manufacturer;
        this.number = number;
    }
}
