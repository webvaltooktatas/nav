<form action="feldolgozas.jsp" method="POST">
    Osszeg:<input type="text" name="osszeg"/><br>

    Muvelet:
    Betet<input type="radio" name="muvelet" value="betet"/>
    Kivet<input type="radio" name="muvelet" value="kivet"/>
    Egyenleg<input type="radio" name="muvelet" value="egyenleg"/>
    <br>
    <input type="submit" value="Kuldes">
</form>

<form action="logout.jsp">
    <input type="submit" value="Kijelentkezes"/>
</form>
