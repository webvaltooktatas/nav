<%@ page import="hu.webvalto.service.BankAPI" %>
<%
    BankAPI bank = (BankAPI) session.getAttribute("bank");
    String muvelet = request.getParameter("muvelet");
    String osszeg = request.getParameter("osszeg");
    if (muvelet != null) {

        if (muvelet.equals("betet")) {
            boolean status = bank.betet(Integer.parseInt(osszeg));
            if (status) {
                out.print("Osszeg hozzaadva!");
            } else {
                out.println("Adjon meg nagyobb osszeget!");
            }
        } else if (muvelet.equals("kivet")) {
            boolean status = bank.kivet(Integer.parseInt(osszeg));
            if (status) {
                out.print("Osszeg levonva!");
            } else {
                out.println("Adjon meg kisebb osszeget!");
            }
        } else {
            out.println("Egyenleg: " + bank.egyenleg());
        }
    }
%>
<hr/>
<jsp:include page="muveletek.jsp"></jsp:include>