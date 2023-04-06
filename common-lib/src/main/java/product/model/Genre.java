package product.model;

import java.util.Arrays;
import java.util.function.Predicate;

public enum Genre {
    FANTASY,
    SCIENCE_FICTION,
    ADVENTURE,
    ROMANCE,
    THRILLER,
    HORROR,
    MYSTERY,
    BIOGRAPHY,
    AUTOBIOGRAPHY,
    PROGRAMMING,
    HISTORICAL_FICTION,
    POPULAR_SCIENCE;

    public static boolean contains(String value) {
        return Arrays.stream(values()).map(Enum::name).anyMatch(Predicate.isEqual(value));
    }
}
