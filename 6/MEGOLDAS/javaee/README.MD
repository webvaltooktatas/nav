Készítsük el a banki alkalmazásunk első formjait. Lehessen megadni felhasználót és összeget amire a műveletek el akarjuk végezni.


<h2>Feladat</h2>
- Módosítani a számlanyitást, hogy megadhassunk felhasználót
    - 3 vagy hosszabb nevet kelljen megadni, hibauzenet ha nem igy teszunk
- Módosítani a műveletek oldalt, hogy megadhassuk az összeget, valamint a kiválaszthassuk a kért műveletet
- Lehessen kijelentkezni
- Bejelentkezés filter elkészítése


<h2>Megoldas</h2>

<h3>muveletek.jsp</h3>

```jsp
<form action="feldolgozas.jsp" method="POST">
    Osszeg:<input type="text" name="osszeg"/><br>

    Muvelet:
    Betet<input type="radio" name="muvelet" value="betet"/>
    Kivet<input type="radio" name="muvelet" value="kivet"/>
    Egyenleg<input type="radio" name="muvelet" value="egyenleg"/>
    <br>
    <input type="submit" value="Kuldes">
</form>

<form action="logout.jsp">
    <input type="submit" value="Kijelentkezes"/>
</form>
```

<h3>index.jsp</h3>

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Banki app</title>
</head>
<body>
<form action="SzamlaNyitas" method="POST">
    Kerem adja meg a nevet: <input type="text" name="nev"/><br>
    <br>
    <input type="submit" value="Szamlanyitas">
</form>

</body>
</html>


```

<h3>logout.jsp</h3>

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.getSession().invalidate();
    response.sendRedirect(request.getContextPath() + "/index.jsp");
%>
```

<h3>hibasSzamlanyitas.jsp</h3>

```jsp
<span style="color:red">Kerem legalabb 3 karakter vagy hosszabb nevet adjon meg!</span>
<jsp:include page="index.jsp"></jsp:include>
```

<h3>Szamlanyitas.java</h3>

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

    // A HTTP POST kereseket kiszolgalo metodusunk
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            String nev = request.getParameter("nev");
            if (nev != null) {
                boolean result = bank.szamlanyitas(nev);
                if (!result) {
                    request.getRequestDispatcher("/hibasSzamlanyitas.jsp").forward(request, response);
                } else {
                    // Munkamenetbe beallitjuk a banki API-nkat, hogy a tobbi servlet is elerje
                    request.getSession().setAttribute("bank", bank);
                    //Atiranyitjuk a kerest a muveletek.jsp fele
                    request.getRequestDispatcher("/muveletek.jsp").forward(request, response);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

```

<h3>Bank.java</h3>

```java
package hu.webvalto.service.impl;

import hu.webvalto.data.FelhasznaloAPI;
import hu.webvalto.data.KonyvelesAPI;
import hu.webvalto.service.BankAPI;

import javax.ejb.Stateless;
import javax.inject.Inject;


@Stateless
public class Bank implements BankAPI {

    @Inject
    private KonyvelesAPI konyveles;

    @Inject
    private FelhasznaloAPI felhasznalo;

    @Override
    public boolean kivet(int osszeg) {
        //Ne vehessunk ki tobbet, mint amink van
        Integer egyenleg = konyveles.getEgyenleg(felhasznalo.getNev());
        if (osszeg > 0 && osszeg <= egyenleg) {
            konyveles.setEgyenleg(felhasznalo.getNev(), egyenleg - osszeg);
            return true;
        }
        return false;

    }

    @Override
    public boolean betet(int osszeg) {
        if (osszeg > 0) {
            Integer egyenleg = konyveles.getEgyenleg(felhasznalo.getNev());
            konyveles.setEgyenleg(felhasznalo.getNev(), egyenleg + osszeg);
            return true;
        }
        return false;
    }

    @Override
    public int egyenleg() {
        return konyveles.getEgyenleg(felhasznalo.getNev());
    }

    @Override
    public boolean szamlanyitas(String nev) {
        if (nev.length() >= 3) {
            felhasznalo.setNev(nev);
            return true;
        }
        return false;
    }
}
```

<h3>BejelentkezesiFilter</h3>

```java
package hu.webvalto.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("*")
public class BejelentkezesFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);

        String bejelentkezesURI = httpRequest.getContextPath() + "/SzamlaNyitas";

        boolean bejelentkezesiKeres = httpRequest.getRequestURI().equals(bejelentkezesURI);

        boolean bejelentkezesOldal = httpRequest.getRequestURI().endsWith("index.jsp");

        boolean bejelentkezve = (session != null && session.getAttribute("bejelentkezve") != null);

        if (bejelentkezve && (bejelentkezesiKeres || bejelentkezesOldal)) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/muveletek.jsp");
            dispatcher.forward(request, response);

        } else if (bejelentkezve || bejelentkezesiKeres) {
            chain.doFilter(request, response);

        } else {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);

        }
    }

    @Override
    public void destroy() {

    }
}

```