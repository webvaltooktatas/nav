# NAV - JAVA EE oktatás

Kedves résztvevők,

Ezen a github oldalon fogják mindig megtalálni a már átvett gyakorlatok anyagát.

Az egyes gyakorlatok külön mappába lettek szervezve, a könnyebb átláthatóság végett.

Az egyes mappák alatt kettő állapotát fogják megtalálni az adott gyakorlati anyagnak:
- "javaee" mappában a kiinduló állapotot
- "MEGOLDAS" mappában pedig az adott gyakorlati anyag megoldását

Ezen felül minden gyakorlati feladat root könyvtárában található egy README.MD mely leírja az adott gyakorlati feladatot.

<h2>Egy kis emlékezető a Maven parancsokról</h2>

`mvn install` – az alkalmazásunk lefordítása
`mvn wildfly:start -f pom.xml` – Wildfly alkalmazásszerver indítása a Wildlfy plugin segítségével (amennyiben még nem volt, ez letölt egy wildfly szervert számunkra)
`mvn wildfly:deploy -f pom.xml` – Alkalmazás kitelepítése az alkalmazászerverre

Nyissuk meg a böngészőt és írjuk be: `http://localhost:8080/javaee`

`mvn wildfly:shutdown -f pom.xml` – Alkalmazásszerver leállítása

