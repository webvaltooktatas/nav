Keszitsuk el a banki alkalmazasunk elso feluleteit a megtanult JSP es Servlet megoldasok hasznalataval.

A cel, hogy legyen par feluletunk ahol a mar megirt funkciokat elerjuk.

<h3>Feladat</h3>
- Elkesziteni a szamlanyitas oldalt
- Novelni az egyenlegunk
- Csokkenteni az egyenlegunk
- Lekerdezni az egyenlegunk


<h1>MEGOLDAS</h1>

Szamlanyitas servlet

```java

package hu.webvalto.servlet;

import hu.webvalto.service.BankAPI;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// A servletunk a "/SzamlaNyitas" vegponton hallgatozik
@WebServlet("/SzamlaNyitas")
public class SzamlaNyitas extends HttpServlet {

    // Banki EJB osztalyunk beinjektalasa
    @EJB
    private BankAPI bank;

    // A HTTP GET kereseket kiszolgalo metodusunk
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Munkamenetbe beallitjuk a banki API-nkat, hogy a tobbi servlet is elerje
            request.getSession().setAttribute("bank", bank);
            //Atiranyitjuk a kerest a muveletek.jsp fele
            request.getRequestDispatcher("/muveletek.jsp").forward(request, response);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
```
Feldolgozas.jsp

```jsp
<%@ page import="hu.webvalto.service.BankAPI" %>
<%
    BankAPI bank = (BankAPI) session.getAttribute("bank");
    String muvelet = request.getParameter("muvelet");
    String ertek = request.getParameter("ertek");

    if (muvelet != null) {

        if (muvelet.equals("betet")) {
            boolean status = bank.betet(Integer.parseInt(ertek));
            if (status) {
                out.print("Osszeg hozzaadva!");
            } else {
                out.println("Adjon meg nagyobb osszeget!");
            }
        } else if (muvelet.equals("kivet")) {
            boolean status = bank.kivet(Integer.parseInt(ertek));
            if (status) {
                out.print("Osszåçeg levonva!");
            } else {
                out.println("Adjon meg kisebb osszeget!");
            }
        } else {
            out.println("Egyenleg: " + bank.egyenleg());
        }
    }
%>
<hr/>
<jsp:include page="muveletek.jsp"></jsp:include>
```

index.jsp

```jsp

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Banki app</title>
</head>
<body>
    <a href="SzamlaNyitas">Nyissunk szamlat</a>
</body>
</html>

```

muveletek.jsp

```jsp

<div>
    <a href="feldolgozas.jsp?muvelet=betet&ertek=10">10 Forint betet</a>
</div>
<div>
    <a href="feldolgozas.jsp?muvelet=kivet&ertek=10">10 Forint kivet</a>
</div>
<div>
    <a href="feldolgozas.jsp?muvelet=egyenleg">egyenleg lekerdezese</a>
</div>
```