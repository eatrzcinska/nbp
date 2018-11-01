package pl.currenda.nbp.controller.Data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Input {

    private String code;
    private String start;
    private String finish;
}
