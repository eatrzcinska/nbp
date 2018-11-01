package pl.currenda.nbp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import pl.currenda.nbp.controller.Data.Input;
import pl.currenda.nbp.controller.Data.Output;
import pl.currenda.nbp.service.CurrencyService;
import java.math.BigDecimal;


@Controller
public class CurrencyController {

    private CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/")
    public String showForm(Model model) {
        Input inputData = new Input();
        model.addAttribute("Input", inputData);
        return "index";
    }

    @PostMapping("/")
    public String sendForm(Model model) {
        Input inputData = new Input();
        model.addAttribute("Input", inputData);
        return "redirect:/result";
    }

    @PostMapping("/result")
    public String showResult(@ModelAttribute("Input") Input inputData, Output outputData, Model model) {
        BigDecimal averageBid = currencyService.getAverageBid(inputData.getCode(), inputData.getStart(), inputData.getFinish());
        BigDecimal standardDeviation = currencyService.getStandardDeviation(inputData.getCode(), inputData.getStart(), inputData.getFinish());
        outputData.setAverage(averageBid);
        outputData.setDeviation(standardDeviation);
        model.addAttribute("averageBid", outputData.getAverage());
        model.addAttribute("standardDeviation", outputData.getDeviation());
        return "result";
    }

    @ExceptionHandler({HttpClientErrorException.class})
    public String handleException() {
        return "error";
    }

}
