/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.util.HashMap;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author mateu
 */
@WebService(serviceName = "currency")
public class currency {
    
    HashMap currencies = new HashMap<String, Double>();
    
    public currency() {
        currencies.put("USD", 3.77);
        currencies.put("EUR", 4.56);
        currencies.put("GBP", 5.23);
    }
    
    @WebMethod(operationName = "convert")
    public double convert(@WebParam(name = "value") double value, @WebParam(name = "currency") String currency) {
        double currencyValue = (double) currencies.get("USD");
        if(currencies.get(currency.toUpperCase()) != null) {
            currencyValue = (double) currencies.get(currency.toUpperCase());   
        } 
        return Math.round(value / currencyValue * 100) / 100.0;
    }
    
    @WebMethod(operationName = "currencies")
    public String allCurrencies() {
        StringBuilder sb = new StringBuilder();
        for(Object i : currencies.keySet()) {
            sb.append("Waluta: ");
            sb.append(i);
            sb.append(" Kurs: ");
            sb.append(currencies.get(i));
            sb.append("\n");
        }
        return sb.toString();
    }
}
