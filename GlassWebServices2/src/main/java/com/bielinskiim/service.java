package com.bielinskiim;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author bielinskim
 */
@WebService(serviceName = "hello")
public class service {

    @WebMethod(operationName = "sayHello")
    public String sayHello(@WebParam(name = "firstName") String firstName, @WebParam(name = "lastName") String lastName) {
        return "Witaj " + firstName + " " + lastName + " !";
    }
}
