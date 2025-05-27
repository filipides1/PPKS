# Programska potpora komunikacijskim sustavima
Ovo je moj projekt iz predmeta "Programska potpora komunkikacijskim sustavima" na Fakultetu elektrotehnike i računarstva u Zagrebu.

# Instalacija
Frontend - potrebno se pozicionirati u frontend direktorij (`auction-frontend`) i pokrenuti:
```bash
npm install
```
Backend - potrebno se pozicionirati u backend direktorij (`auction-backend`) i pokrenuti gradle:
```bash
./gradlew build
```
Database - potrebno je uzeti skrptu za izgradnju baze podataka i pokrenuti je u PostgreSQL-u. Skripta se nalazi u direktoriju `auction-database` i zove se `script.sql`.

# Pokretanje aplikacije
Frontend - potrebno se pozicionirati u frontend direktorij (`auction-frontend`) i pokrenuti:
```bash
npm start
```
Backend - potrebno se pozicionirati u backend direktorij (`auction-backend`) i pokrenuti:
```bash
./gradlew bootRun
```

# Korištene tehnologije
- Frontend: Angular, TypeScript, HTML, SCSS
- Backend: Java, Spring Boot, PostgreSQL
- Baza podataka: PostgreSQL
- Alati: Gradle, npm

# Potrebni alati
- Node.js (za frontend)
- Angular CLI (za frontend)
- Java JDK 17 ili noviji (za backend)
- Gradle (za backend)
- PostgreSQL (za bazu podataka)


