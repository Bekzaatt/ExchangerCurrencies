package Util;

import Exception.CustomException.CurrencyExistsException;
import Exception.CustomException.ParametersNotFoundException;
import Service.Interface.ExchangeRatesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ExchangerUtil {
    public static String getParameter(HttpServletRequest req, HttpServletResponse resp){
        String pathInfo = req.getPathInfo();
        String code = pathInfo.substring(1);

        if(!code.toUpperCase().matches("[A-Z]{6}")){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            throw new ParametersNotFoundException("Коды валют пары отсутствуют в адресе");
        }
        return code;
    }

    public static Map<String, String> validateExchanger(HttpServletRequest req, HttpServletResponse resp, ExchangeRatesService exchangeRatesService) throws SQLException {
        Map<String, String> exchangeRates = new HashMap<>();

        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rate = req.getParameter("rate");

        if(baseCurrencyCode == null || targetCurrencyCode == null || rate == null ||
            baseCurrencyCode.isEmpty() || targetCurrencyCode.isEmpty() || rate.isEmpty()){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            throw new ParametersNotFoundException("Отсутствует нужное поле формы");
        }
        exchangeRates.put("baseCurrencyCode", baseCurrencyCode);
        exchangeRates.put("targetCurrencyCode", targetCurrencyCode);
        exchangeRates.put("rate", rate);

        String code = baseCurrencyCode + targetCurrencyCode;


        validatingCurrencyExists(code, exchangeRatesService, resp);
        return exchangeRates;
    }

    public static void validatingCurrencyExists(String code, ExchangeRatesService exchangeRatesService, HttpServletResponse resp) throws SQLException {
        if(!exchangeRatesService.findByCode(code.toUpperCase()).isEmpty()){
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            throw new CurrencyExistsException("Валютная пара с таким кодом уже существует");
        }
    }
}
