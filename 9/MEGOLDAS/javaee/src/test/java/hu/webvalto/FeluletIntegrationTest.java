package hu.webvalto;


import hu.webvalto.service.impl.Bank;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
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
@Ignore
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
        assertEquals("<html><head>    <title>Banki app</title></head><body><form id=\"szamlaForm\" action=\"SzamlaNyitas\" method=\"POST\">    Kerem adja meg a nevet: <input id=\"nev\" type=\"text\" name=\"nev\"/><br>    <br>    <input id=\"submit\" type=\"submit\" value=\"Szamlanyitas\"></form></body></html>", html);
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
        assertEquals("<html><head>    <title>Banki app</title></head><body><form id=\"szamlaForm\" action=\"SzamlaNyitas\" method=\"POST\">    Kerem adja meg a nevet: <input id=\"nev\" type=\"text\" name=\"nev\"/><br>    <br>    <input id=\"submit\" type=\"submit\" value=\"Szamlanyitas\"></form></body></html>", html);
    }
}
