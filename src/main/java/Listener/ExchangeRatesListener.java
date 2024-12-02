package Listener;

import DAO.Impl.ExchangeRatesDaoImpl;
import DAO.Interface.ExchangeRatesDAO;
import Service.Implementation.ExchangeRatesServiceImpl;
import Service.Interface.ExchangeRatesService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebListener;

import java.io.IOException;
@WebListener
public class ExchangeRatesListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce){
        ExchangeRatesDAO exchangeRatesDAO = new ExchangeRatesDaoImpl();
        ExchangeRatesService exchangeRatesService = new ExchangeRatesServiceImpl(exchangeRatesDAO);

        sce.getServletContext().setAttribute("exchangeRatesService", exchangeRatesService);

    }
}
