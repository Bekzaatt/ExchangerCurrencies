package Servlets;

import Exception.CustomException.DBException;
import Exception.CustomException.ParametersNotFoundException;
import Service.Interface.ExchangeRatesService;
import Util.ExchangerUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/exchange/*")
public class ExchangeServlet extends HttpServlet {
    private ExchangeRatesService exchangeRatesService;

    @Override
    public void init() throws ServletException {
        super.init();
        exchangeRatesService = (ExchangeRatesService)getServletContext().getAttribute("exchangeRatesService");
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        try {
            String baseCurrencyCode = req.getParameter("from");
            String targetCurrencyCode = req.getParameter("to");
            String amount = req.getParameter("amount");
            if(baseCurrencyCode.isEmpty() || targetCurrencyCode.isEmpty() || amount.isEmpty() ||
                baseCurrencyCode == null || targetCurrencyCode == null || amount == null){
                throw new ParametersNotFoundException("Parameters not found");
            }
            
            List<Map<String, Object>> exchanger = exchangeRatesService.exchange(baseCurrencyCode, targetCurrencyCode, amount);
            resp.setContentType("application/json");
            writer.write(new ObjectMapper().writeValueAsString(exchanger));

        } catch (SQLException e) {
            throw new DBException("Owibka");
        }

    }
}
