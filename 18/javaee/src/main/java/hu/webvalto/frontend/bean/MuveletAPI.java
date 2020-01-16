package hu.webvalto.frontend.bean;


import hu.webvalto.backend.service.impl.Bank;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Named
@ManagedBean
@SessionScoped
public class MuveletAPI implements Serializable {

    @Inject
    private Szamla szamla;

    @EJB
    private Bank bank;

    public String szamlaNyitas() {
        if (bank.szamlanyitas(szamla.getNev())) {
            szamla.setUzenet("");
            this.getRequest().getSession().setAttribute("bejelentkezve", true);
            return "muveletek.xhtml";
        }
        return "";
    }

    private HttpServletRequest getRequest() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        Object requestObj = context.getRequest();

        return (HttpServletRequest) requestObj;
    }
}
