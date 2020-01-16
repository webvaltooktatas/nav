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

        boolean wsORrsHivas = httpRequest.getRequestURI().contains("BankWS") || httpRequest.getRequestURI().contains("BankRS");

        boolean bejelentkezve = (session != null && session.getAttribute("bejelentkezve") != null);

        if (wsORrsHivas) {
            chain.doFilter(request, response);
        } else if (bejelentkezve && (bejelentkezesiKeres || bejelentkezesOldal)) {
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
