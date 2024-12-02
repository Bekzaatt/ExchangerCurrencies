package Service.Implementation;

import DAO.Interface.CurrenciesDAO;
import Service.Interface.CurrenciesService;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class CurrenciesServiceImpl implements CurrenciesService {
    private CurrenciesDAO currenciesDAO;

    public CurrenciesServiceImpl(CurrenciesDAO currenciesDAO) {
        this.currenciesDAO = currenciesDAO;
    }

    @Override
    public List<Map<String, String>> findAll() throws SQLException {
        return currenciesDAO.findAll();
    }

    @Override
    public Map<String, String> findByCode(String code) throws SQLException {
        return currenciesDAO.findByCode(code);
    }

    @Override
    public Map<String, String> save(String name, String code, String sign) throws SQLException {
        return currenciesDAO.save(name, code, sign);
    }
}
