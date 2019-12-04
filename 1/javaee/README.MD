<h1>Kiindulo projektunk</h1>

<h2>Feladat</h2>
- Az alkalmazasunk irja ki, hogy "Hello vilag!"
- Futtassuk az alkalmazasunk es nezzuk meg bongeszoben, hogy mukodik-e. (http://localhost:8000/javaee)

<h2>Architektura</h2>
- JDK 1.8
- JAVA EE 8
- MAVEN 3
- WILDFLY 

<h2>Elofeltetelek</h2>
- JDK 1.8 letoltese
- Maven 3.X telepitese

<h2>Leiras</h2>

A kiindulo projektunk egy JAVA EE 8-as kis hello vilag alkalmazas, mely jo kiindulasi alapot fog biztositani szamunkra, 
hogy az oktatas soran megepitsuk es szep lassan tovabb okositsuk az alkalmazasunk.

<h2>Pluginek</h2>

A kenyelem szempontjabol a kiindulo alkalmazasunk elore fel lesz konfigolva, hogy WAR webes achivkent legyen csomagolva, 
valamint a Wildfly plugin segitsegevel az alkalmazasszerver automatikusan le legyen toltve es kenyelmesen tudjunk ra 
az alkalmazasunk kitelepiteni.

<h3>Parancsok</h3>
- Alkalmazas fuggosegeinek letoltese: ```mvn clean install```
- Wildfly alkalmazasszerver elinditasa: ```mvn wildfly:start -f pom.xml```
- Wildfly alkalmazasszerverre telepitese az alkalmazasnak: ```mvn wildfly:deploy -f pom.xml```
- Wildfly alkalmazasszerverrol az alkalmazas eltavolitasa: ```mvn wildfly:undeploy -f pom.xml```
- Wildfly alkalmazasszerver leallitasa: ```mvn wildfly:shutdown -f pom.xml```