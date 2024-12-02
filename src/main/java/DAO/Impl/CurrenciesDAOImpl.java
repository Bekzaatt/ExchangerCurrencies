package DAO.Impl;

import DAO.Interface.CurrenciesDAO;

import java.sql.*;
import java.util.*;

public class CurrenciesDAOImpl implements CurrenciesDAO {
    private static Connection connection;
    private static final String URL = "jdbc:sqlite:C:\\Users\\Asus\\IdeaProjects\\ExchangeCurrencies\\ExchangerCurrencies.sqlite";

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
    public List<Map<String, String>> findAll() throws SQLException {
        PreparedStatement prepareStatement = connection.prepareStatement(
                "SELECT * FROM Currencies"
        );
        ResultSet resultSet = prepareStatement.executeQuery();

        List<Map<String, String>> currenciesList = new ArrayList<>();
        while (resultSet.next()){
            Map<String, String> currencies = new LinkedHashMap<>();
            currencies.put("id", String.valueOf(resultSet.getInt("id")));
            currencies.put("code", resultSet.getString("code"));
            currencies.put("fullName", resultSet.getString("fullName"));
            currencies.put("sign", resultSet.getString("sign"));
            currenciesList.add(currencies);
        }

        return currenciesList;
    }

    @Override
    public Map<String, String> findByCode(String code) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "select * from Currencies where code = ?"
        );
        preparedStatement.setString(1, code);
        ResultSet resultSet = preparedStatement.executeQuery();
        Map<String, String> currency = generateDateFromResultSet(resultSet);

        return currency;
    }

    @Override
    public Map<String, String> save(String name, String code, String sign) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "insert into Currencies(code, fullName, sign) values (?, ?, ?)"
        );
        preparedStatement.setString(1, code);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, sign);
        preparedStatement.executeUpdate();


        return findByCode(code);
    }

    private Map<String, String> generateDateFromResultSet(ResultSet resultSet) throws SQLException {
        Map<String, String> currency = new LinkedHashMap<>();
        while (resultSet.next()){
            currency.put("id", String.valueOf(resultSet.getInt("id")));
            currency.put("code", String.valueOf(resultSet.getString("code")));
            currency.put("fullName", String.valueOf(resultSet.getString("fullName")));
            currency.put("sign", String.valueOf(resultSet.getString("sign")));
        }
        return currency;
    }
}
