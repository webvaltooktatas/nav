Készítsünk egy webservice-t amivel létrehozhatunk új felhasználót.

<h2>Feladat</h2>
- Felhasználó létrehozását támogató webservice készítése
- WSDL alapján kliens generálása
- Kliens írása, ami létrehozza “Petra” nevű felhasználót

<h2>Előfeltétel</h2>

Elő kell konfigolni a Wildfly-t, hogy létre legyen hozva a QUEUE vagy TOPIC, amelyikre küldeni és fogadni fogunk.

Alternatívaként elindíthatjuk a Wildfly-t a standalone-full.xml konfigurációval ahol már van egy QUEUE létrehozva.

A linkelt megoldás ezt fogja használni.

Wildfly indítása a standalone-full.xml konfigurációval:

```

standalone.sh --server-config=standalone-full.xml

```
