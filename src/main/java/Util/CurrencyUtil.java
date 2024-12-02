package Util;

import Exception.CustomException.ParametersNotFoundException;
import Model.Currencies;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CurrencyUtil {
    public static Currencies getParameter(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Gson gson = new Gson();
        Currencies currency = gson.fromJson(req.getReader(), Currencies.class);

        String code = currency.getCode();
        String name = currency.getFullName();
        String sign = currency.getSign();


        if (name == null || code == null || sign == null
                || name.isEmpty() || code.isEmpty() || sign.isEmpty()){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            throw new ParametersNotFoundException("Отсутствует нужное поле формы");
        }

        return currency;
    }
}
