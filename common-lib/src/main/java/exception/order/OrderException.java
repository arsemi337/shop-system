package exception.order;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderException extends RuntimeException {

    private final List<ProductErrorDto> errorDtoList;

    public OrderException(String errorMessage, List<ProductErrorDto> errorDtoList) {
        super(errorMessage);
        this.errorDtoList = errorDtoList;
    }
}
