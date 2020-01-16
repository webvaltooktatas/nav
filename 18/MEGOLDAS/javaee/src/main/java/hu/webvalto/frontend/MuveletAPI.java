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
