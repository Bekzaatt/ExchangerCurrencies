package Servlets;

import Exception.CustomException.CurrencyNotFoundException;
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
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private ExchangeRatesService exchangeRatesService;

    @Override
    public void init() throws ServletException {
        super.init();
        exchangeRatesService = (ExchangeRatesService)getServletContext().getAttribute("exchangeRatesService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Writer writer = resp.getWriter();
        String code = ExchangerUtil.getParameter(req, resp);

        try {
            List<Map<String, Object>> exchanger = exchangeRatesService.findByCode(code.toUpperCase());
            if(exchanger.isEmpty()){
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                throw new CurrencyNotFoundException("Обменный курс для пары не найден");
            }
            resp.setContentType("application/json");
            resp.setStatus(200);
            writer.write(new ObjectMapper().writeValueAsString(exchanger));

        } catch (SQLException e) {
            throw new DBException("Owibka");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();

        String code = ExchangerUtil.getParameter(req, resp);
        String rate = req.getParameter("rate");

        if(rate.isEmpty()){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            throw new ParametersNotFoundException("Отсутствует нужное поле формы");
        }
        try {
            ExchangerUtil.validatingCurrencyExists(code, exchangeRatesService, resp);

            List<Map<String, Object>> exchanger = exchangeRatesService.update(code, new BigDecimal(rate), resp);

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_CREATED);
            writer.write(new ObjectMapper().writeValueAsString(exchanger));

        } catch (SQLException e) {
            throw new DBException("Owibka");
        }

    }
}
