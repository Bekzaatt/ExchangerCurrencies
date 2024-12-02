package Service.Implementation;

import DAO.Interface.ExchangeRatesDAO;
import Service.Interface.ExchangeRatesService;
import jakarta.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExchangeRatesServiceImpl implements ExchangeRatesService {
    private ExchangeRatesDAO exchangeRatesDAO;


    public ExchangeRatesServiceImpl(ExchangeRatesDAO exchangeRatesDAO) {
        this.exchangeRatesDAO = exchangeRatesDAO;
    }

    @Override
    public List<Map<String, Object>> findAll() throws SQLException {

        return exchangeRatesDAO.findAll();
    }

    @Override
    public List<Map<String, Object>> findByCode(String code) throws SQLException {
        return exchangeRatesDAO.findByCode(code);
    }

    @Override
    public List<Map<String, Object>> save(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate, HttpServletResponse resp) throws SQLException {
        return exchangeRatesDAO.save(baseCurrencyCode, targetCurrencyCode, rate, resp);
    }

    @Override
    public List<Map<String, Object>> update(String code, BigDecimal rate, HttpServletResponse resp) throws SQLException {
        return exchangeRatesDAO.update(code, rate, resp);
    }

    @Override
    public List<Map<String, Object>> exchange(String baseCurrencyCode, String targetCurrencyCode, String amount) throws SQLException {


        if(!exchangeRatesDAO.findByCode(targetCurrencyCode + baseCurrencyCode).isEmpty() &&
            exchangeRatesDAO.findByCode(baseCurrencyCode+targetCurrencyCode).isEmpty()){
            List<Map<String, Object>> exchanger =
                    exchangeRatesDAO.exchange(targetCurrencyCode, baseCurrencyCode, amount);
            Double rate = Double.parseDouble(String.valueOf(exchanger.get(0).get("rate")));

            Double reverseRate = 1 / rate;
            Double totalConvertedAmount = reverseRate * Double.parseDouble(amount);
            Map<String, Object> targetCurrency = (Map<String, Object>)exchanger.get(0).get("baseCurrency");

            exchanger.get(0).put("baseCurrency", exchanger.get(0).get("targetCurrency"));
            exchanger.get(0).put("targetCurrency", targetCurrency);
            exchanger.get(0).put("rate", reverseRate);
            exchanger.get(0).put("amount", amount);
            exchanger.get(0).put("convertedAmount", totalConvertedAmount);

            return exchanger;
        }

        if(exchangeRatesDAO.getByBaseCode(baseCurrencyCode).get(0).get("targetCurrency").equals(
                exchangeRatesDAO.getByTargetCode(targetCurrencyCode).get(0).get("baseCurrency"))){

            List<Map<String, Object>> totalExchanger = new ArrayList<>();
            List<Map<String, Object>> exchangerBase =
                    exchangeRatesDAO.getByBaseCode(baseCurrencyCode);
            List<Map<String, Object>> exchangerTarget =
                    exchangeRatesDAO.getByTargetCode(targetCurrencyCode);

            Map<String, Object> exchangerMap = (Map<String, Object>) exchangerBase.get(0).get("targetCurrency");
            String target = String.valueOf(exchangerMap.get("code"));
            List<Map<String, Object>> exchanger = exchangeRatesDAO.exchange(baseCurrencyCode,
                    target, amount);

            String converted = String.valueOf(exchanger.get(0).get("convertedAmount"));

            Map<String, Object> exchangerMapBase = (Map<String, Object>) exchangerTarget.get(0).get("baseCurrency");
            String base = String.valueOf(exchangerMapBase.get("code"));

            List<Map<String, Object>> exchangerToTarget =
                    exchangeRatesDAO.exchange(base, targetCurrencyCode, converted);

            Double baseRate = Double.parseDouble(String.valueOf(exchanger.get(0).get("rate")));
            Double targetRate = Double.parseDouble(String.valueOf(exchangerToTarget.get(0).get("rate")));
            Double totalRate = baseRate * targetRate;

            Map<String, Object> totalMap = new LinkedHashMap<>();
            totalMap.put("baseCurrency", exchanger.get(0).get("baseCurrency"));
            totalMap.put("targetCurrency", exchangerTarget.get(0).get("targetCurrency"));
            totalMap.put("rate", totalRate);
            totalMap.put("amount", amount);
            totalMap.put("convertedAmount", exchangerToTarget.get(0).get("convertedAmount"));

            totalExchanger.add(totalMap);

            return totalExchanger;
        }
        
        return exchangeRatesDAO.exchange(baseCurrencyCode, targetCurrencyCode, amount);
    }
}
