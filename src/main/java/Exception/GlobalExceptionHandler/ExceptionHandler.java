package Exception.GlobalExceptionHandler;


import Exception.CustomException.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@WebServlet("/*")
public class ExceptionHandler implements Filter{
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (DBException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("message", ex.getMessage());
            servletResponse.setContentType("application/json");
            servletResponse.getWriter().write(new ObjectMapper().writeValueAsString(error));
        } catch (CodeNotFoundException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("message", ex.getMessage());
            servletResponse.setContentType("application/json");
            servletResponse.getWriter().write(new ObjectMapper().writeValueAsString(error));
        } catch (CurrencyNotFoundException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("message", ex.getMessage());
            servletResponse.setContentType("application/json");
            servletResponse.getWriter().write(new ObjectMapper().writeValueAsString(error));
        } catch (ParametersNotFoundException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("message", ex.getMessage());
            servletResponse.setContentType("application/json");
            servletResponse.getWriter().write(new ObjectMapper().writeValueAsString(error));
        } catch (CurrencyExistsException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("message", ex.getMessage());
            servletResponse.setContentType("application/json");
            servletResponse.getWriter().write(new ObjectMapper().writeValueAsString(error));
        }
    }
}
