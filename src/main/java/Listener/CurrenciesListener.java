package Listener;

import DAO.Interface.CurrenciesDAO;
import DAO.Impl.CurrenciesDAOImpl;
import Service.Implementation.CurrenciesServiceImpl;
import Service.Interface.CurrenciesService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class CurrenciesListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        CurrenciesDAO currenciesDAO = new CurrenciesDAOImpl();
        CurrenciesService currenciesService = new CurrenciesServiceImpl(currenciesDAO);

        sce.getServletContext().setAttribute("currencyService", currenciesService);


    }
}
