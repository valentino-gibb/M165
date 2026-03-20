# Musik Plattform Backend API

Das Backend basiert auf Spring Boot 4.0.4 und MongoDB zur Verwaltung der Musikdaten.

## Datenmodelle

### Artist (Künstler)
- `name`: Name des Künstlers
- `herkunftsland`: Herkunftsland
- `debütJahr`: Jahr des Debüts
- `biografie`: Biografie des Künstlers
- `label`: Label des Künstlers
- `erstellungsDatum`: Erstellungsdatum

### Song (Lied)
- `title`: Songtitel
- `dauer`: Dauer in Sekunden
- `genre`: Musikgenre
- `veröffentlichungsDatum`: Veröffentlichungsdatum
- `explizit`: Ist der Song explizit (Ja/Nein)
- `anzahlStreams`: Anzahl der Streams
- `lyrics`: Liedtext
- `sprache`: Sprache des Songs
- `albumId`: Referenz zum Album

### Album
- `title`: Albumtitel
- `veröffentlichungsDatum`: Veröffentlichungsdatum
- `beschreibung`: Beschreibung des Albums
- `coverbild`: Coverbild-URL
- `gesamtdauer`: Gesamtdauer des Albums in Sekunden
- `artistId`: Referenz zum Künstler
- `songIds`: Liste der Song-IDs im Album
- `erstellungsDatum`: Erstellungsdatum

### User (Benutzer)
- `benutzername`: Benutzername
- `email`: E-Mail-Adresse
- `password`: Passwort (sollte gehashed sein)
- `land`: Land des Benutzers
- `profilbild`: Profilbild-URL
- `artistId`: Referenz zum Artist (optional, wenn Benutzer auch Künstler ist)
- `playlistIds`: Liste der Playlist-IDs
- `erstellungsDatum`: Erstellungsdatum

### Playlist
- `name`: Playlistname
- `beschreibung`: Beschreibung der Playlist
- `erstellungsDatum`: Erstellungsdatum
- `tags`: Liste von Tags
- `coverbild`: Coverbild-URL
- `userId`: Referenz zum Benutzer
- `songIds`: Liste der Song-IDs in der Playlist

## API Endpoints

### Artists

```
POST   /api/artists              - Neuen Künstler erstellen
GET    /api/artists              - Alle Künstler abrufen
GET    /api/artists/{id}         - Künstler nach ID abrufen
GET    /api/artists/name/{name}  - Künstler nach Name suchen
GET    /api/artists/label/{label} - Künstler nach Label filtern
GET    /api/artists/country/{country} - Künstler nach Land filtern
PUT    /api/artists/{id}         - Künstler aktualisieren
DELETE /api/artists/{id}         - Künstler löschen
```

### Songs

```
POST   /api/songs                 - Neuen Song erstellen
GET    /api/songs                 - Alle Songs abrufen
GET    /api/songs/{id}            - Song nach ID abrufen
GET    /api/songs/title/{title}   - Song nach Titel suchen
GET    /api/songs/genre/{genre}   - Songs nach Genre filtern
GET    /api/songs/language/{language} - Songs nach Sprache filtern
GET    /api/songs/album/{albumId} - Songs aus Album abrufen
GET    /api/songs/explicit        - Nur explizite Songs abrufen
PUT    /api/songs/{id}            - Song aktualisieren
POST   /api/songs/{id}/stream     - Stream-Counter erhöhen
DELETE /api/songs/{id}            - Song löschen
```

### Albums

```
POST   /api/albums                - Neues Album erstellen
GET    /api/albums                - Alle Alben abrufen
GET    /api/albums/{id}           - Album nach ID abrufen
GET    /api/albums/title/{title}  - Album nach Titel suchen
GET    /api/albums/artist/{artistId} - Alben vom Künstler abrufen
PUT    /api/albums/{id}           - Album aktualisieren
POST   /api/albums/{albumId}/songs/{songId} - Song zu Album hinzufügen
DELETE /api/albums/{albumId}/songs/{songId} - Song vom Album entfernen
DELETE /api/albums/{id}           - Album löschen
```

### Users

```
POST   /api/users                 - Neuen Benutzer erstellen
GET    /api/users                 - Alle Benutzer abrufen
GET    /api/users/{id}            - Benutzer nach ID abrufen
GET    /api/users/username/{username} - Benutzer nach Name suchen
GET    /api/users/email/{email}   - Benutzer nach E-Mail suchen
PUT    /api/users/{id}            - Benutzer aktualisieren
POST   /api/users/{userId}/artist/{artistId} - Benutzer als Künstler einstellen
DELETE /api/users/{id}            - Benutzer löschen
```

### Playlists

```
POST   /api/playlists             - Neue Playlist erstellen
GET    /api/playlists             - Alle Playlists abrufen
GET    /api/playlists/{id}        - Playlist nach ID abrufen
GET    /api/playlists/name/{name} - Playlist nach Name suchen
GET    /api/playlists/user/{userId} - Playlists vom Benutzer abrufen
PUT    /api/playlists/{id}        - Playlist aktualisieren
POST   /api/playlists/{playlistId}/songs/{songId} - Song zur Playlist hinzufügen
DELETE /api/playlists/{playlistId}/songs/{songId} - Song von Playlist entfernen
DELETE /api/playlists/{id}        - Playlist löschen
```

## Konfiguration

### MongoDB Verbindung
Die MongoDB-Verbindung wird über die `application.properties` konfiguriert:

```properties
spring.data.mongodb.uri=mongodb+srv://[username]:[password]@[cluster].mongodb.net/[databasename]?retryWrites=true&w=majority
```

### Server Port
Der Server läuft standardmäßig auf Port 8080.

## Build und Start

```bash
# Kompilieren
./mvnw clean compile

# Tests ausführen
./mvnw test

# JAR bauen
./mvnw clean package

# Anwendung starten
./mvnw spring-boot:run
# oder
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

## CORS Konfiguration

CORS ist für `http://localhost:5173` (Vue.js Frontend) aktiviert.

## Testing mit curl

Beispiele zum Testen der API:

```bash
# Künstler erstellen
curl -X POST http://localhost:8080/api/artists \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Artist Name",
    "herkunftsland": "Austria",
    "debütJahr": 2010,
    "biografie": "Bio text",
    "label": "Label Name"
  }'

# Alle Künstler abrufen
curl http://localhost:8080/api/artists
```
