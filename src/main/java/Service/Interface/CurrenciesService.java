package Service.Interface;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface CurrenciesService {
    List<Map<String, String>> findAll() throws SQLException;

    Map<String, String> findByCode(String code) throws SQLException;

    Map<String, String> save(String name, String code, String sign) throws SQLException;
}
