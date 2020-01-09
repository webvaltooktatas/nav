package hu.webvalto;

import hu.webvalto.data.FelhasznaloAPI;
import hu.webvalto.data.KonyvelesAPI;
import hu.webvalto.data.impl.Felhasznalo;
import hu.webvalto.data.impl.Konyveles;
import hu.webvalto.service.BankAPI;
import hu.webvalto.service.impl.Bank;
import hu.webvalto.servlet.SzamlaNyitas;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import javax.ejb.EJB;
import java.io.File;
import java.net.URL;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(Arquillian.class)
@RunAsClient
@Ignore
public class BetetIntegrationTest {

    @ArquillianResource
    private URL deploymentUrl;

    @Drone
    private WebDriver browser;

    @FindBy(id = "nev")
    private WebElement nev;

    @FindBy(id = "submit")
    private WebElement kuldesGomb;

    @FindBy(id = "betetmuvelet")
    private WebElement betetmuvelet;

    @FindBy(id = "osszeg")
    private WebElement osszeg;

    @Deployment
    public static WebArchive createDeployment() {

        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClass(BankAPI.class)
                .addClass(Bank.class)
                .addClass(KonyvelesAPI.class)
                .addClass(FelhasznaloAPI.class)
                .addClass(Konyveles.class)
                .addClass(Felhasznalo.class)
                .addClass(SzamlaNyitas.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"))
                .addAsWebResource(new File("src/main/webapp/index.jsp"),
                        "/index.jsp")
                .addAsWebResource(new File("src/main/webapp/muveletek.jsp"),
                        "/muveletek.jsp")
                .addAsWebResource(new File("src/main/webapp/feldolgozas.jsp"),
                        "/feldolgozas.jsp");
    }

    @Test
    public void tizForintHozzaadasaASzamlamhoz() throws Exception {
        //Kitelepitett alkalmazas URL-jenek megnyitasa
        browser.get(deploymentUrl.toExternalForm());
        Thread.sleep(2000l);
        //Szamla letrehozasa
        nev.sendKeys("lajos");
        Thread.sleep(2000l);
        guardHttp(kuldesGomb).click();
        Thread.sleep(3000l);
        //Betet muvelet elvegzese
        betetmuvelet.click();
        Thread.sleep(2000l);
        osszeg.sendKeys("10");
        Thread.sleep(2000l);
        guardHttp(kuldesGomb).click();
        Thread.sleep(3000l);
        //Ellenorizzuk, hogy a muvelet sikeresen veget ert
        assertEquals(true,browser.getPageSource().contains("Osszeg hozzaadva!"));
    }
}
