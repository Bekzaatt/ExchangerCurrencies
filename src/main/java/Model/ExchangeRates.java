package Model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.math.RoundingMode;
public class ExchangeRates {
    public int id;
    public int baseCurrencyId;
    public int targetCurrencyId;
    public BigDecimal rate;

    public ExchangeRates(int id, int baseCurrencyId, int targetCurrencyId){
        this.id = id;
        this.baseCurrencyId = baseCurrencyId;
        this.targetCurrencyId = targetCurrencyId;

        rate = new BigDecimal("1234.56");
        rate = rate.setScale(6, RoundingMode.HALF_UP);
    }
    public ExchangeRates(){}
}
