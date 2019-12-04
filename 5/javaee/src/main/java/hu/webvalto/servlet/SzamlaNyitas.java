package hu.webvalto.servlet;

import hu.webvalto.service.BankAPI;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("Szamlanyitas")
public class SzamlaNyitas extends HttpServlet {

    @EJB
    private BankAPI bank;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            String nev = request.getParameter("nev");
            if (nev != null) {
                boolean result = bank.szamlanyitas(nev);
                if (!result) {
                    request.getRequestDispatcher("/hibasSzamlanyitas.jsp").forward(request, response);
                } else {
                    request.getSession().setAttribute("bank", bank);
                    request.getSession().setAttribute("bejelentkezve", true);
                    request.getRequestDispatcher("/muveletek.jsp").forward(request, response);

                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
