Cseréljük le a JSP oldalainkat, hogy használjanak JSF technológiát inkább!

<h2>Feladat</h2>
- JSP oldalak átírása
- Számla managed bean létrehozása
- MuveletAPI managed bean létrehozása

<h1>Megoldás</h1>

<h3>web.xml</h3>

```xml

<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
						http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">

    <display-name>JSP Example</display-name>

    <data-source>
        <name>java:app/bank_in_web_xml</name>
        <class-name>com.mysql.cj.jdbc.MysqlDataSource</class-name>
        <server-name>localhost</server-name>
        <database-name>bank</database-name>
        <user>root</user>
        <password>1234</password>
        <transactional>true</transactional>
        <isolation-level>TRANSACTION_READ_COMMITTED</isolation-level>
        <initial-pool-size>2</initial-pool-size>
        <max-pool-size>10</max-pool-size>
        <min-pool-size>5</min-pool-size>
    </data-source>

    <welcome-file-list>
        <welcome-file>index.xhtml</welcome-file>
    </welcome-file-list>

    <!--
       FacesServlet is main servlet responsible to handle all request.
       It acts as central controller.
       This servlet initializes the JSF components before the JSP is displayed.
    -->

    <servlet>
        <servlet-name>Faces Servlet</servlet-name>
        <servlet-class>javax.faces.webapp.FacesServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <servlet-mapping>
        <servlet-name>Faces Servlet</servlet-name>
        <url-pattern>/faces/*</url-pattern>
    </servlet-mapping>

    <servlet-mapping>
        <servlet-name>Faces Servlet</servlet-name>
        <url-pattern>*.jsf</url-pattern>
    </servlet-mapping>

    <servlet-mapping>
        <servlet-name>Faces Servlet</servlet-name>
        <url-pattern>*.faces</url-pattern>
    </servlet-mapping>

    <servlet-mapping>
        <servlet-name>Faces Servlet</servlet-name>
        <url-pattern>*.xhtml</url-pattern>
    </servlet-mapping>


</web-app>

```

<h3>index.html</h3>

```xml

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:h="http://java.sun.com/jsf/html">

<h:head>
    <title>Számla nyitás</title>
</h:head>
<h:body>
    <h:form>
        <h:outputText value="Kérem adja meg a nevét: "/>
        <h:inputText value="#{szamla.nev}"></h:inputText>
        <div>
            <h:commandButton action="#{muveletAPI.szamlaNyitas}" value="Számla nyitás"></h:commandButton>
        </div>
        <div>
            <h:outputText value="#{szamla.uzenet}"/>
        </div>
    </h:form>
</h:body>
</html>

```

<h3>muveletek.xthml</h3>

```xml

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:h="http://java.sun.com/jsf/html">

<h:head>
    <title>Szamlanyitas</title>
</h:head>
<h:body>
    <h:form id="muveletiForm">
        <div>
            <h:outputText value="Udv, #{szamla.nev}"/>
            <br/>
        </div>
        <h:outputText value="Osszeg: "/>
        <h:inputText id="osszeg" value="#{szamla.osszeg}"/>
        <br/>

        <h:outputText value="Muvelet: "/>
        <br/>
        <h:selectOneRadio value="#{szamla.muvelet}">
            <f:selectItem itemValue="betet" itemLabel="Betet"/>
            <f:selectItem itemValue="kivet" itemLabel="Kivet"/>
            <f:selectItem itemValue="egyenleg" itemLabel="Egyenleg"/>
        </h:selectOneRadio>
        <br/>
        <div>
            <h:outputText value="#{szamla.uzenet}"/>
        </div>

        <h:commandButton value="Kuldes" action="#{muveletAPI.feldolgozas}"/>
        <h:commandButton value="Kijelentkezes" action="#{muveletAPI.kijelentkezes}"/>
    </h:form>
</h:body>
</html>

```

<h3>BejelentkezesiFilter</h3>

```java

package hu.webvalto.frontend.filter;

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

        boolean bejelentkezesOldal = httpRequest.getRequestURI().endsWith("index.xhtml");

        boolean bejelentkezve = (session != null && session.getAttribute("bejelentkezve") != null);

        if (bejelentkezve && (bejelentkezesiKeres || bejelentkezesOldal)) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/muveletek.xhtml");
            dispatcher.forward(request, response);

        } else if (bejelentkezve || bejelentkezesiKeres) {
            chain.doFilter(request, response);

        } else {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/index.xhtml");
            dispatcher.forward(request, response);

        }
    }

    @Override
    public void destroy() {

    }
}

```

<h3>Szamla</h3>

```java

package hu.webvalto.frontend.beans;

import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.validation.constraints.*;
import java.io.Serializable;

@Named
@ManagedBean
@SessionScoped
public class Szamla implements Serializable {
    private String nev;

    private Integer osszeg;
    private String uzenet;
    private String muvelet;

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public Integer getOsszeg() {
        return osszeg;
    }

    public void setOsszeg(Integer osszeg) {
        this.osszeg = osszeg;
    }

    public String getUzenet() {
        return uzenet;
    }

    public void setUzenet(String uzenet) {
        this.uzenet = uzenet;
    }

    public String getMuvelet() {
        return muvelet;
    }

    public void setMuvelet(String muvelet) {
        this.muvelet = muvelet;
    }
}



```

<h3>MuveleteAPI</h3>

```java

package hu.webvalto.frontend;

import hu.webvalto.backend.service.BankAPI;
import hu.webvalto.frontend.beans.Szamla;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Named
@SessionScoped
public class MuveletAPI implements Serializable {

    @EJB
    private BankAPI bank;

    @Inject
    private Szamla szamla;


    public void feldolgozas() {
        if (szamla.getMuvelet().equals("egyenleg")) {
            szamla.setUzenet("Az egyenleg: " + bank.egyenleg());
        } else if (szamla.getMuvelet().equals("betet")) {
            szamla.setUzenet(bank.betet(szamla.getOsszeg()) ? "Sikeres muvelet" : "Sikertelen muvelet!");
        } else if (szamla.getMuvelet().equals("kivet")) {
            szamla.setUzenet(bank.kivet(szamla.getOsszeg()) ? "Sikeres muvelet" : "Sikertelen muvelet!");
        }

        szamla.setOsszeg(0);
    }

    public String kijelentkezes() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index?faces-redirect=true";

    }

    public String szamlaNyitas() {
        if (bank.szamlanyitas(szamla.getNev())) {
            szamla.setUzenet("");
            this.getRequest().getSession().setAttribute("bejelentkezve", true);
            return "muveletek.xhtml";
        }
        szamla.setUzenet("Sikertelen számlanyitás");
        return "";
    }

    private HttpServletRequest getRequest() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        Object requestObj = context.getRequest();

        return (HttpServletRequest) requestObj;
    }
}


```

