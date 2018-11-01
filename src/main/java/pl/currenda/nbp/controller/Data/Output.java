package pl.currenda.nbp.controller.Data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Output {

    private BigDecimal average;
    private BigDecimal deviation;
}
