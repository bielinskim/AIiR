/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Marcin
 */
@WebService(serviceName = "Hello")
public class Hello {

    /**
     * This is a sample web service operation
     * @param firstName
     * @param lastName
     * @return 
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "firstName") String firstName, @WebParam(name = "lastName") String lastName) {
        return "Witaj " + firstName + " " + lastName + " !";
    }
}
