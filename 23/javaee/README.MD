Készítsük el a küldő és fogadó bean-eket, hogy létrehozhassunk felhasználót JMS üzenettel.

<h2>Feladat</h2>
- Küldő JMS bean létrehozása
- Fogadó MDB létrehozása
- Felhasználó létrehozása JMS üzeneten keresztül: “UJSZAMLA|PETYA” üzenet alapján

<h2>Előfeltétel</h2>

Elő kell konfigolni a Wildfly-t, hogy létre legyen hozva a QUEUE vagy TOPIC, amelyikre küldeni és fogadni fogunk.

Alternatívaként elindíthatjuk a Wildfly-t a standalone-full.xml konfigurációval ahol már van egy QUEUE létrehozva.

A linkelt megoldás ezt fogja használni.

Wildfly indítása a standalone-full.xml konfigurációval:

```

standalone.sh --server-config=standalone-full.xml

```
