# Coworking space

Dies ist ein minimaler Prototype für ein Coworking Space System, welches mit Quarkus entwickelt wurde

## Erste Schritte

1. Erstelle eine Kopie (fork) von diesem Projekt.
1. Stelle sicher, dass Docker installiert ist und läuft.
1. Stelle sicher, dass Visual Studio Code und die Erweiterung Remote Container installiert ist.
1. Klone (clone) das Projekt lokal, um damit arbeiten zu können.
1. Öffne das Projekt mit Visual Studio Code.
1. Öffne das Projekt im Entwicklungscontainer.
1. Starte das Projekt mit dem Kommando `Quarkus: Debug current Quarkus Project`
1. Probiere die Client-Applikation unter http://localhost:8080 aus.
1. Schaue die API auf http://localhost:8080/q/swagger-ui/ an.

## Datenbank

Die Daten werden in einer PostgreSQL-Datenbank gespeichert. In der Entwicklungsumgebung wird diese in der [docker-compose-yml](./.devcontainer/docker-compose.yml) konfiguriert.

### Datenbankadministration

Über http://localhost:5050 ist PgAdmin4 erreichbar. Damit lässt sich die Datenbank komfortabel verwalten. Der Benutzername lautet `zli@example.com` und das Passwort `zli*123`. Die Verbindung zur PostgreSQL-Datenbank muss zuerst mit folgenden Daten konfiguriert werden:
 - Host name/address: `db`
 - Port: `5432`
 - Maintenance database: `postgres`
 - Username: `postgres`
 - Password: `postgres`

### Testdaten der Datenbank

Die Testdaten sind unter "/workspace/src/main/resources/testdata.sql" definiert und werden im "/workspace/src/main/resources/application.properties" geladen.

## Automatische Tests

Die automatischen Tests können mit `./mvnw quarkus:test` ausgeführt werden. Für die automatischen Tests wird nicht die PostgreSQL-Datenbank verwendet, sondern eine H2-Datenbank, welche sich im Arbeitsspeicher während der Ausführung befindet.

Die automatischen Tests werden in dem Verzeichnis gefunden:
/workspace/src/test/java/ch/zli/m223

### Änderungen zur Schnittstellenplanung

Die Verwaltung der Buchungsanfragen, durch den Admin, laufen nun über /bookings. Es wird geprüft, ob der User im SecurityContext die Gruppe "Admin" hat. So muss man keine neuen extra Controller / Service für den Admin erstellen.
Zudem ist die Schnittstelle des Passworts ändern anders. Der User kann seine Account Daten nun über users/{id} mit der PUT methode ändern.

### Disclaimer

Das README.md ist eine Vorlage aus dem punchclock Projekt. Zudem wurde der Code mit Hilfe von ChatGPT erstellt.