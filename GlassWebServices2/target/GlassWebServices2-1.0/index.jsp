<%-- 
    Document   : index
    Created on : 25 kwi 2021, 16:23:49
    Author     : bielinskim
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
    </body>
</html>
<%
    try { // Call Web Service Operation
        com.bielinskim.Calculator_Service service = new com.bielinskim.Calculator_Service();
        com.bielinskim.Calculator port = service.getCalculatorPort();
        // TODO initialize WS operation arguments here
        double first = 0.0d;
        double second = 0.0d;
        // TODO process result here
        double result = port.add(first, second);
        System.out.println("Result = "+result);
        } catch (Exception ex) {
// TODO handle custom exceptions here
}
    %>