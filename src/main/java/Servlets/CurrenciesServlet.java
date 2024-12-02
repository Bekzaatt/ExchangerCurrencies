package Servlets;


import Exception.CustomException.CurrencyExistsException;
import Exception.CustomException.CurrencyNotFoundException;
import Exception.CustomException.ParametersNotFoundException;
import Model.Currencies;
import Service.Interface.CurrenciesService;

import Util.CurrencyUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Exception.CustomException.DBException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Currency;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet{
    private CurrenciesService currenciesService;

    @Override
    public void init() throws ServletException {
        super.init();
        currenciesService = (CurrenciesService)getServletContext().getAttribute("currencyService");
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        try {
            resp.setContentType("application/json");
            writer.write(new ObjectMapper().writeValueAsString(currenciesService.findAll()));
        } catch (SQLException e) {
            throw new DBException("Ошибка");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        Currencies currency = CurrencyUtil.getParameter(req, resp);

        String code = currency.getCode();
        String name = currency.getFullName();
        String sign = currency.getSign();

        try {
            Map<String, String> result = currenciesService.findByCode(code);
            if(!result.isEmpty()){
                throw new CurrencyExistsException("Валюта с таким кодом уже существует");
            }
        }catch (SQLException ex){
        }

        try {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            writer.write(new ObjectMapper().writeValueAsString(currenciesService.save(name, code, sign)));
            resp.setStatus(201);
        } catch (SQLException e) {
            throw new DBException("Ошибка");
        }
    }
}
