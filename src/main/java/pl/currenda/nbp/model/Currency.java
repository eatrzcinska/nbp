package pl.currenda.nbp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Currency {

    private String code;
    private BigDecimal bid;
    private BigDecimal ask;
    private LocalDate effectiveDate;
}
