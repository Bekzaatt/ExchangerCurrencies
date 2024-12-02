package Servlets;

import Exception.CustomException.DBException;
import Service.Interface.ExchangeRatesService;
import Util.ExchangerUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private ExchangeRatesService exchangeRatesService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        exchangeRatesService = (ExchangeRatesService)getServletContext().getAttribute("exchangeRatesService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();

        try {
            resp.setContentType("application/json");
            writer.write(new ObjectMapper().writeValueAsString(exchangeRatesService.findAll()));
            resp.setStatus(200);
        } catch (SQLException e) {
            throw new DBException("Owibka");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        try {
            Map<String, String> exchangeRate = ExchangerUtil.validateExchanger(req, resp, exchangeRatesService);
            String baseCurrencyCode = exchangeRate.get("baseCurrencyCode");
            String targetCurrencyCode = exchangeRate.get("targetCurrencyCode");
            String rate = exchangeRate.get("rate");

            List<Map<String, Object>> exchanger =
                    exchangeRatesService.save(baseCurrencyCode, targetCurrencyCode, new BigDecimal(rate), resp);

            writer.write(new ObjectMapper().writeValueAsString(exchanger));

        } catch (SQLException e) {
            throw new DBException("Owibka");
        }
    }


}
