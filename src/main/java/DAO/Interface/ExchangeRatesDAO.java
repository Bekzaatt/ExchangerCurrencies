package DAO.Interface;

import jakarta.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ExchangeRatesDAO {
    List<Map<String, Object>> findAll() throws SQLException;

    List<Map<String, Object>> findByCode(String code) throws SQLException;

    List<Map<String, Object>> save(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate, HttpServletResponse resp) throws SQLException;
    List<Map<String, Object>> update(String code, BigDecimal rate, HttpServletResponse resp) throws SQLException;

    List<Map<String, Object>> exchange(String baseCurrencyCode, String targetCurrencyCode, String amount) throws SQLException;
    List<Map<String, Object>> getByBaseCode(String code) throws SQLException;
    List<Map<String, Object>> getByTargetCode(String code) throws SQLException;

}
