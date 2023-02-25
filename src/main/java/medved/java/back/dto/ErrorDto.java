package medved.java.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorDto {
    private String message;
    private Integer id;
}
