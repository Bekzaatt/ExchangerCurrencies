package DAO.Impl;

import DAO.Interface.ExchangeRatesDAO;
import Exception.CustomException.CurrencyNotFoundException;
import jakarta.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class ExchangeRatesDaoImpl implements ExchangeRatesDAO {
    private static final String URL = "jdbc:sqlite:C:\\Users\\Asus\\IdeaProjects\\ExchangeCurrencies\\ExchangerCurrencies.sqlite";
    private static Connection connection;
    private CurrenciesDAOImpl currenciesDAO = new CurrenciesDAOImpl();

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Map<String, Object>> findAll() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "select ExchangeRates.id as id, " +
                        "c1.id as c1_id, " +
                        "c1.fullName as c1_name, " +
                        "c1.code as c1_code, " +
                        "c1.sign as c1_sign, " +
                        "c2.id as c2_id, " +
                        "c2.fullName as c2_name, " +
                        "c2.code as c2_code, " +
                        "c2.sign as c2_sign, " +
                        "rate from ExchangeRates " +
                        "join Currencies c1 On ExchangeRates.baseCurrencyId = c1.id " +
                        "join Currencies c2 on ExchangeRates.targetCurrencyId = c2.id"
        );
        ResultSet resultSet = preparedStatement.executeQuery();
        return generateDatesFromResultSet(resultSet);
    }

    @Override
    public List<Map<String, Object>> findByCode(String code) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "Select ExchangeRates.id as id, " +
                        "c1.id as c1_id, " +
                        "c1.fullName as c1_name, " +
                        "c1.code as c1_code, " +
                        "c1.sign as c1_sign, " +
                        "c2.id as c2_id, " +
                        "c2.fullName as c2_name, " +
                        "c2.code as c2_code, " +
                        "c2.sign as c2_sign, " +
                        "rate from ExchangeRates " +
                        "JOIN Currencies c1 on ExchangeRates.baseCurrencyId = c1.id " +
                        "JOIN Currencies c2 on ExchangeRates.targetCurrencyId = c2.id " +
                        "Where c1.code || c2.code = ?"
        );
        preparedStatement.setString(1, code);
        ResultSet resultSet = preparedStatement.executeQuery();
        return generateDatesFromResultSet(resultSet);
    }

    @Override
    public List<Map<String, Object>> save(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate, HttpServletResponse resp) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "insert into ExchangeRates (baseCurrencyId, targetCurrencyId, rate) values (?, ?, ?)"
        );
        preparingStatement(preparedStatement, baseCurrencyCode, targetCurrencyCode, rate, resp);
        preparedStatement.executeUpdate();

        return findByCode(baseCurrencyCode+targetCurrencyCode);
    }

    @Override
    public List<Map<String, Object>> update(String code, BigDecimal rate, HttpServletResponse resp) throws SQLException {
        String baseCurrencyCode = code.substring(0, 3);
        String targetCurrencyCode = code.substring(3, 6);

        PreparedStatement preparedStatement = connection.prepareStatement(
                "update ExchangeRates set baseCurrencyId = ?, targetCurrencyId = ?, rate = ?" +
                        " WHERE id = ?"
        );
        preparingStatement(preparedStatement, baseCurrencyCode, targetCurrencyCode, rate, resp);
        preparedStatement.setInt(4, Integer.parseInt(String.valueOf(findByCode(targetCurrencyCode+baseCurrencyCode).get(0).get("id"))));
        preparedStatement.executeUpdate();

        return findByCode(code);
    }

    @Override
    public List<Map<String, Object>> exchange(String baseCurrencyCode, String targetCurrencyCode, String amount) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                        "select c1.id as c1_id, " +
                        "c1.fullName as c1_name, " +
                        "c1.code as c1_code, " +
                        "c1.sign as c1_sign, " +
                        "c2.id as c2_id, " +
                        "c2.fullName as c2_name, " +
                        "c2.code as c2_code, " +
                        "c2.sign as c2_sign, " +
                        "rate " +
                        "from ExchangeRates " +
                        "join Currencies c1 on c1.id = ExchangeRates.baseCurrencyId " +
                        "join Currencies c2 on c2.id = ExchangeRates.targetCurrencyId " +
                                "where c1.code || c2.code = ?"
        );
        preparedStatement.setString(1, baseCurrencyCode + targetCurrencyCode);
        ResultSet resultSet = preparedStatement.executeQuery();


        List<Map<String, Object>> exchanger = generateDatesFromResultSetForExchange(resultSet);
        Map<String, Object> map = exchanger.get(0);
        Double rate = Double.parseDouble(String.valueOf((map.get("rate"))));
        Double convertedAmount = Double.parseDouble(amount) * rate;

        exchanger.get(0).put("amount", amount);
        exchanger.get(0).put("convertedAmount", convertedAmount);

        return exchanger;
    }


    @Override
    public List<Map<String, Object>> getByBaseCode(String code) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "Select ExchangeRates.id as id, " +
                        "c1.id as c1_id, " +
                        "c1.fullName as c1_name, " +
                        "c1.code as c1_code, " +
                        "c1.sign as c1_sign, " +
                        "c2.id as c2_id, " +
                        "c2.fullName as c2_name, " +
                        "c2.code as c2_code, " +
                        "c2.sign as c2_sign, " +
                        "rate from ExchangeRates " +
                        "JOIN Currencies c1 on ExchangeRates.baseCurrencyId = c1.id " +
                        "JOIN Currencies c2 on ExchangeRates.targetCurrencyId = c2.id " +
                        "Where c1.code = ?"
        );
        preparedStatement.setString(1, code);
        ResultSet resultSet = preparedStatement.executeQuery();
        return generateDatesFromResultSet(resultSet);
    }
    @Override
    public List<Map<String, Object>> getByTargetCode(String code) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "Select ExchangeRates.id as id, " +
                        "c1.id as c1_id, " +
                        "c1.fullName as c1_name, " +
                        "c1.code as c1_code, " +
                        "c1.sign as c1_sign, " +
                        "c2.id as c2_id, " +
                        "c2.fullName as c2_name, " +
                        "c2.code as c2_code, " +
                        "c2.sign as c2_sign, " +
                        "rate from ExchangeRates " +
                        "JOIN Currencies c1 on ExchangeRates.baseCurrencyId = c1.id " +
                        "JOIN Currencies c2 on ExchangeRates.targetCurrencyId = c2.id " +
                        "Where c2.code = ?"
        );
        preparedStatement.setString(1, code);
        ResultSet resultSet = preparedStatement.executeQuery();
        return generateDatesFromResultSet(resultSet);
    }

    private List<Map<String, Object>> generateDatesFromResultSet(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> exchangeRates = new ArrayList<>();
        while (resultSet.next()){
            Map<String, Object> exchange = new LinkedHashMap<>();
            exchange.put("id", String.valueOf(resultSet.getInt("id")));

            Map<String, String> baseMap = new LinkedHashMap<>();
            baseMap.put("id", String.valueOf(resultSet.getInt("c1_id")));
            baseMap.put("name", resultSet.getString("c1_name"));
            baseMap.put("code", resultSet.getString("c1_code"));
            baseMap.put("sign", resultSet.getString("c1_sign"));
            exchange.put("baseCurrency", baseMap);

            Map<String, String> targetMap = new LinkedHashMap<>();
            targetMap.put("id", String.valueOf(resultSet.getInt("c2_id")));
            targetMap.put("name", resultSet.getString("c2_name"));
            targetMap.put("code", resultSet.getString("c2_code"));
            targetMap.put("sign", resultSet.getString("c2_sign"));
            exchange.put("targetCurrency", targetMap);

            exchange.put("rate", resultSet.getBigDecimal("rate"));

            exchangeRates.add(exchange);
        }
        return exchangeRates;
    }

    private List<Map<String, Object>> generateDatesFromResultSetForExchange(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> exchangeRates = new ArrayList<>();
            Map<String, Object> exchange = new LinkedHashMap<>();

            Map<String, String> baseMap = new LinkedHashMap<>();
            baseMap.put("id", String.valueOf(resultSet.getInt("c1_id")));
            baseMap.put("name", resultSet.getString("c1_name"));
            baseMap.put("code", resultSet.getString("c1_code"));
            baseMap.put("sign", resultSet.getString("c1_sign"));
            exchange.put("baseCurrency", baseMap);

            Map<String, String> targetMap = new LinkedHashMap<>();
            targetMap.put("id", String.valueOf(resultSet.getInt("c2_id")));
            targetMap.put("name", resultSet.getString("c2_name"));
            targetMap.put("code", resultSet.getString("c2_code"));
            targetMap.put("sign", resultSet.getString("c2_sign"));
            exchange.put("targetCurrency", targetMap);

            exchange.put("rate", resultSet.getBigDecimal("rate"));

            exchangeRates.add(exchange);

        return exchangeRates;
    }

    private void preparingStatement(PreparedStatement preparedStatement, String baseCurrencyCode,
                                   String targetCurrencyCode, BigDecimal rate, HttpServletResponse resp) throws SQLException {

        if(currenciesDAO.findByCode(baseCurrencyCode) == null || currenciesDAO.findByCode(targetCurrencyCode) == null
                || currenciesDAO.findByCode(baseCurrencyCode).isEmpty() || currenciesDAO.findByCode(targetCurrencyCode).isEmpty()){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new CurrencyNotFoundException("Одна (или обе) валюта из валютной пары не существует в БД");
        }

        preparedStatement.setInt(1, Integer.parseInt(currenciesDAO.findByCode(baseCurrencyCode).get("id")));
        preparedStatement.setInt(2, Integer.parseInt(currenciesDAO.findByCode(targetCurrencyCode).get("id")));
        preparedStatement.setBigDecimal(3, rate);

    }
}
