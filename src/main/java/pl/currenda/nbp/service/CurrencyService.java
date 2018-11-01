package pl.currenda.nbp.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.currenda.nbp.model.Currency;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class CurrencyService {

    private RestTemplate restTemplate = new RestTemplate();

    public String createUrl(String code, String start, String finish) {
        StringBuilder url = new StringBuilder();
        url.append("http://api.nbp.pl/api/exchangerates/rates/c/");
        url.append(code);
        url.append("/");
        url.append(start);
        url.append("/");
        url.append(finish);
        url.append("/?format=json");
        return url.toString();
    }

    public List<Currency> getList(String code, String start, String finish) {
        String url = createUrl(code, start, finish);
        JSONParser parser = new JSONParser();
        String s = restTemplate.getForObject(url, String.class);
        Object obj = null;
        try {
            obj = parser.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = (JSONObject) obj;
        JSONArray jsonArray = (JSONArray) jsonObject.get("rates");
        List<Currency> currencyList = new ArrayList<>();
        Iterator itr1 = jsonArray.iterator();
        while (itr1.hasNext()) {
            Currency currency = new Currency();
            Iterator<Map.Entry> itr2 = ((Map) itr1.next()).entrySet().iterator();
            Map.Entry pairNo = itr2.next();
            Map.Entry pairAsk = itr2.next();
            Map.Entry pairBid = itr2.next();
            Map.Entry pairEffectiveDate = itr2.next();
            currency.setBid(new BigDecimal((double) pairBid.getValue()));
            currency.setAsk(new BigDecimal((double) pairAsk.getValue()));
            currencyList.add(currency);
        }
        return currencyList;
    }

    public BigDecimal getAverageBid(String code, String start, String finish) {
        List<Currency> currencyList = getList(code, start, finish);
        BigDecimal sum = BigDecimal.valueOf(0);
        for (Currency c : currencyList) {
            sum = sum.add(c.getBid());
        }
        BigDecimal averageBid = sum.divide(new BigDecimal(currencyList.size()), 4, RoundingMode.HALF_UP);
        return averageBid;
    }

    public BigDecimal getStandardDeviation(String code, String start, String finish) {
        List<Currency> lista = getList(code, start, finish);
        BigDecimal sum = BigDecimal.valueOf(0);
        for (Currency c : lista) {
            sum = sum.add(c.getAsk());
        }
        BigDecimal averageAsk = sum.divide(new BigDecimal(lista.size()), 4, RoundingMode.HALF_UP);
        BigDecimal sd2 = BigDecimal.valueOf(0);
        for (Currency c : lista) {
            sd2 = sd2.add((c.getAsk().subtract(averageAsk)).pow(2));
        }
        BigDecimal sd = sd2.divide(new BigDecimal(lista.size()), 8, RoundingMode.HALF_UP);
        double p = Math.pow(sd.doubleValue(), 1 / 2.0);
        sd = new BigDecimal(p).setScale(4, RoundingMode.HALF_UP);
        return sd;
    }
}
