Használjunk Bean validációt, a manuális ellenőrzések helyett.

<h2>Feladat</h2>
- Név 3-nál hosszabbnak kell lennie
- Betét / kivét 0-nál nagyobb számnak kell lennie
- Irassuk ki a validációs hiba üzeneteket


<h1>Megoldás</h1>

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
    @Size(min = 3, message = "Névnek 3 vagy hosszabbnak kell lennie!")
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

    @NotNull(groups = SzamlaMuvelet.class)
    @Positive(groups = SzamlaMuvelet.class)
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

<h3>MuveletAPI</h3>

```java

package hu.webvalto.frontend;

import hu.webvalto.backend.service.BankAPI;
import hu.webvalto.frontend.beans.Szamla;
import hu.webvalto.frontend.beans.SzamlaMuvelet;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.Serializable;
import java.util.Set;

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
        } else {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Szamla>> violations = validator.validate(szamla, SzamlaMuvelet.class);
            for (ConstraintViolation<Szamla> constraintViolation : violations) {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                facesContext.addMessage("muveletiForm", new FacesMessage(constraintViolation.getMessage()));
            }
            if (violations.size() == 0) {
                if (szamla.getMuvelet().equals("betet")) {
                    szamla.setUzenet(bank.betet(szamla.getOsszeg()) ? "Sikeres muvelet" : "Sikertelen muvelet!");
                } else if (szamla.getMuvelet().equals("kivet")) {
                    szamla.setUzenet(bank.kivet(szamla.getOsszeg()) ? "Sikeres muvelet" : "Sikertelen muvelet!");
                }
            }
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

<h3>index.xhtml</h3>

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
            <h:messages style = "color:red;margin:8px;" />
        </div>
    </h:form>
</h:body>
</html>

```

<h3>muveletek.xhtml</h3>

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
            <h:messages style = "color:red;margin:8px;" />
        </div>

        <h:commandButton value="Kuldes" action="#{muveletAPI.feldolgozas}"/>
        <h:commandButton value="Kijelentkezes" action="#{muveletAPI.kijelentkezes}"/>
    </h:form>
</h:body>
</html>

```