<form action="feldolgozas.jsp" method="POST">
    Osszeg:<input id="osszeg" type="text" name="osszeg"/><br>

    Muvelet:
    Betet<input id="betetmuvelet" type="radio" name="muvelet" value="betet"/>
    Kivet<input id="kivetmuvelet" type="radio" name="muvelet" value="kivet"/>
    Egyenleg<input id="egyenlegmuvelet" type="radio" name="muvelet" value="egyenleg"/>
    <br>
    <input id="submit" type="submit" value="Kuldes">
</form>

<form action="logout.jsp">
    <input type="submit" value="Kijelentkezes"/>
</form>
