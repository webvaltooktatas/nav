package hu.webvalto.servlet;

import hu.webvalto.service.BankAPI;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
                    request.getSession().setAttribute("bejelentkezve", true);
                    //Atiranyitjuk a kerest a muveletek.jsp fele
                    request.getRequestDispatcher("/muveletek.jsp").forward(request, response);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}