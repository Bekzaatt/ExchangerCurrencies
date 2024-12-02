package Servlets;

import Exception.CustomException.CodeNotFoundException;
import Exception.CustomException.CurrencyNotFoundException;
import Exception.CustomException.DBException;
import Service.Interface.CurrenciesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Currency;
import java.util.Map;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private CurrenciesService currenciesService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        currenciesService = (CurrenciesService)getServletContext().getAttribute("currencyService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        String pathInfo = req.getPathInfo();
        String code = pathInfo.substring(1);
        if(code.toUpperCase().matches("[A-Z]{3}")){
            try {
                Map<String, String> currency = currenciesService.findByCode(code.toUpperCase());
                if(Integer.parseInt(currency.get("id")) <= 0){
                    throw new CurrencyNotFoundException("Валюта не найдена");
                }
                else{
                    resp.setContentType("application/json");
                    writer.write(new ObjectMapper().writeValueAsString(currency));
                    resp.setStatus(200);
                }
            } catch (SQLException e) {
                throw new DBException("Ошибка");
            }
        }else throw new CodeNotFoundException("Код валюты отсутствует в адресе");
    }
}
