package hu.webvalto.frontend;

import hu.webvalto.backend.service.BankAPI;
import hu.webvalto.frontend.beans.Szamla;
import hu.webvalto.frontend.beans.SzamlaMuvelet;
import org.slf4j.Logger;

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
