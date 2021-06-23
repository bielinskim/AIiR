package com.bielinskim;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author bielinskim
 */
@WebService(serviceName = "calculator")
public class calculator {

    @WebMethod(operationName = "add")
    public double add(@WebParam(name = "first") double first, @WebParam(name = "second") double second) {
        return first + second;
    }
    
    @WebMethod(operationName = "sub")
    public double sub(@WebParam(name = "first") double first, @WebParam(name = "second") double second) {
        return first - second;
    }
    
    @WebMethod(operationName = "mul")
    public double mul(@WebParam(name = "first") double first, @WebParam(name = "second") double second) {
        return first * second;
    }
    
    @WebMethod(operationName = "div")
    public double div(@WebParam(name = "first") double first, @WebParam(name = "second") double second) {
        return first / second;
    }
}
