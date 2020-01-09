Írjunk pár integrációs tesztet a banki alkalmazásunkhoz.


<h2>Előfeladat</h2>
- Módosítsuk picit a servleteinket, hogy az index.jsp oldalra irányítsanak ha még nincs felhasználónk

<h2>Feladat</h2>
- Számlanyitás oldal
- Műveletek

<h1>Megoldás</h1>

<h3>FeluletIntegrationTest</h2>

```java
package hu.webvalto;


import hu.webvalto.service.impl.Bank;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(Arquillian.class)
@RunAsClient
public class FeluletIntegrationTest {
    @ArquillianResource
    private URL deploymentUrl;

    @Deployment
    public static WebArchive createDeployment() {

        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackages(true, "hu.webvalto")
                .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"))
                .addAsWebResource(new File("src/main/webapp/index.jsp"),
                        "/index.jsp")
                .addAsWebResource(new File("src/main/webapp/muveletek.jsp"),
                        "/muveletek.jsp")
                .addAsWebResource(new File("src/main/webapp/feldolgozas.jsp"),
                        "/feldolgozas.jsp");

    }

    @Test
    @InSequence(1)
    public void indexOldalIT() throws IOException {
        String html;

        URL url = new URL(deploymentUrl + "index.jsp");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            html = reader.lines().collect(Collectors.joining());
        }
        assertNotNull(html);
        assertEquals("<html><head>    <title>Banki app</title></head><body><form action=\"SzamlaNyitas\" method=\"POST\">    Kerem adja meg a nevet: <input type=\"text\" name=\"nev\"/><br>    <br>    <input type=\"submit\" value=\"Szamlanyitas\"></form></body></html>", html);
    }

    @Test
    @InSequence(2)
    public void visszairanyitasAFooldalra() throws IOException {
        String html;
        URL url = new URL(deploymentUrl + "feldolgozas.jsp");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            html = reader.lines().collect(Collectors.joining());
        }
        assertNotNull(html);
        assertEquals("<html><head>    <title>Banki app</title></head><body><form action=\"SzamlaNyitas\" method=\"POST\">    Kerem adja meg a nevet: <input type=\"text\" name=\"nev\"/><br>    <br>    <input type=\"submit\" value=\"Szamlanyitas\"></form></body></html>", html);
    }
}

```

<h3>BankAPIIntegrationTest</h3>

```java
package hu.webvalto;


import hu.webvalto.data.FelhasznaloAPI;
import hu.webvalto.data.KonyvelesAPI;
import hu.webvalto.data.impl.Felhasznalo;
import hu.webvalto.data.impl.Konyveles;
import hu.webvalto.service.BankAPI;
import hu.webvalto.service.impl.Bank;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import javax.ejb.EJB;
import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(Arquillian.class)
public class BankAPIIntegrationTest {

    @EJB
    private BankAPI bank;

    @Inject
    private FelhasznaloAPI felhasznalo;

    @Inject
    private Konyveles konyveles;

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class, "test.jar")
                .addClass(BankAPI.class)
                .addClass(Bank.class)
                .addClass(KonyvelesAPI.class)
                .addClass(FelhasznaloAPI.class)
                .addClass(Konyveles.class)
                .addClass(Felhasznalo.class)
                .addPackage("org.opentest4j")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

    }

    @Test
    @InSequence(1)
    public void SzamlanyitasTeszt() throws IOException {
        assertEquals(true, bank.szamlanyitas("adam"));
        assertEquals("adam", felhasznalo.getNev());
    }

    @Test
    @InSequence(2)
    public void EgyenlegTeszt() {
        assertEquals(0, bank.egyenleg());
    }

    @Test
    @InSequence(3)
    public void BetetTeszt() {
        assertEquals(true, bank.betet(200));
        assertEquals(200, bank.egyenleg());
    }

    @Test
    @InSequence(4)
    public void KivetTeszt() {
        assertEquals(true,bank.kivet(99));
        assertEquals(101, bank.egyenleg());
    }

    @Test
    @InSequence(5)
    public void BetetTesztNegativ() {
        assertEquals(false, bank.betet(-100));
        assertEquals(101, bank.egyenleg());
    }

    @Test
    @InSequence(6)
    public void KivetTesztTulNagyOsszeg() {
        assertEquals(false, bank.kivet(1000));
        assertEquals(101, bank.egyenleg());
        assertEquals(101, konyveles.getEgyenleg(null));
    }
}

```