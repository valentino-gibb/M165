# 🎵 Music Platform - Frontend & Backend

Ein modernes, sauberes System zur Verwaltung von Musikdaten bestehend aus einem Vue.js 3 Frontend und einem Spring Boot Backend mit MongoDB.

## 📋 Übersicht

Das System verwaltet 5 Hauptressourcentypen:
- 🎤 **Künstler** - Künstlerprofile und Informationen
- 💿 **Alben** - Musikalben mit Künstler-Referenzen
- 🎶 **Songs** - Einzelne Lieder mit Metadaten
- 👤 **Benutzer** - Benutzerprofile und Authentifizierung
- 📋 **Playlists** - Benutzer-erstellte Song-Sammlungen

## 🚀 Quick Start

### Voraussetzungen
- Java 17+
- Node.js 18+
- MongoDB (Cloud oder lokal)
- Maven

### Backend starten

```bash
cd backend
./mvnw spring-boot:run
```

Der Backend-Server läuft auf `http://localhost:8080`

**MongoDB Konfiguration**: Die Verbindung wird in `application.properties` konfiguriert:
```properties
spring.data.mongodb.uri=mongodb+srv://[username]:[password]@[cluster].mongodb.net/[database]?retryWrites=true&w=majority
```

### Frontend starten

```bash
cd frontend
npm install
npm run dev
```

Der Frontend-Server ist erreichbar unter `http://localhost:5173` oder `http://localhost:5174`

## 🎨 Frontend - Funktionen

### Sauberes, modernes Design
- Weißes, minimalistisches Theme
- Responsive Grid-Layouts
- Smooth Transitions und Hover-Effekte
- Responsive für mobile Geräte

### CRUD Operationen
Jede Ressource hat:
- ✅ **Create** - Neue Einträge hinzufügen via Modal-Form
- ✅ **Read** - Daten anzeigen in Listen/Tabellen
- ✅ **Update** - Einträge bearbeiten
- ✅ **Delete** - Einträge löschen mit Bestätigung

### Navigation
Einfache Tab-Navigation zwischen:
1. **Künstler** - Künstlerprofile verwalten
2. **Alben** - Alben mit Cover-Bildern anzeigen
3. **Songs** - Tabellenauflistung aller Songs
4. **Benutzer** - Benutzerprofile verwalten
5. **Playlists** - Playlists erstellen und bearbeiten

### Formular-Features
- Validierung erforderlicher Felder
- Intuitive Eingabefelder
- Dropdown-Selektionen für Beziehungen
- Textarea für längere Texte
- Datum-Picker für Veröffentlichungsdaten

## 🛠️ Technologie Stack

### Frontend
- **Vue.js 3** - Progressive JavaScript Framework
- **Vite** - Next-Generation Frontend Build Tool
- **Vanilla CSS** - Reines CSS (keine Abhängigkeiten)

### Backend
- **Spring Boot 4.0.4** - Java Web Framework
- **Spring Data MongoDB** - Daten-Repository Pattern
- **MongoDB** - NoSQL Datenbank
- **Maven** - Build-Tool

## 📁 Projektstruktur

```
M165/
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   │   ├── ArtistList.vue
│   │   │   ├── AlbumList.vue
│   │   │   ├── SongList.vue
│   │   │   ├── UserList.vue
│   │   │   └── PlaylistList.vue
│   │   ├── App.vue (Hauptkomponente)
│   │   ├── api.js (API-Service)
│   │   ├── main.js
│   │   └── style.css (Globale Styles)
│   ├── index.html
│   ├── vite.config.js
│   └── package.json
│
└── backend/
    ├── src/main/java/com/musicplatform/backend/
    │   ├── model/ (Entitäten)
    │   ├── repository/ (Daten-Zugriff)
    │   ├── service/ (Business-Logik)
    │   ├── controller/ (REST APIs)
    │   └── BackendApplication.java
    ├── src/main/resources/
    │   └── application.properties
    ├── pom.xml
    └── mvnw
```

## 🔌 API Endpoints

### Künstler
```
POST   /api/artists              - Neuer Künstler
GET    /api/artists              - Alle Künstler
GET    /api/artists/{id}         - Künstler nach ID
PUT    /api/artists/{id}         - Künstler aktualisieren
DELETE /api/artists/{id}         - Künstler löschen
```

### Alben
```
POST   /api/albums               - Neues Album
GET    /api/albums               - Alle Alben
GET    /api/albums/{id}          - Album nach ID
GET    /api/albums/artist/{id}   - Alben vom Künstler
PUT    /api/albums/{id}          - Album aktualisieren
DELETE /api/albums/{id}          - Album löschen
```

### Songs
```
POST   /api/songs                - Neuer Song
GET    /api/songs                - Alle Songs
GET    /api/songs/{id}           - Song nach ID
GET    /api/songs/genre/{genre}  - Songs nach Genre
GET    /api/songs/album/{id}     - Songs aus Album
POST   /api/songs/{id}/stream    - Stream Counter erhöhen
PUT    /api/songs/{id}           - Song aktualisieren
DELETE /api/songs/{id}           - Song löschen
```

### Benutzer
```
POST   /api/users                - Neuer Benutzer
GET    /api/users                - Alle Benutzer
GET    /api/users/{id}           - Benutzer nach ID
PUT    /api/users/{id}           - Benutzer aktualisieren
DELETE /api/users/{id}           - Benutzer löschen
```

### Playlists
```
POST   /api/playlists            - Neue Playlist
GET    /api/playlists            - Alle Playlists
GET    /api/playlists/{id}       - Playlist nach ID
GET    /api/playlists/user/{id}  - Playlists vom User
PUT    /api/playlists/{id}       - Playlist aktualisieren
DELETE /api/playlists/{id}       - Playlist löschen
```

## 🎯 Beispiel-Workflow

1. **Künstler erstellen**
   - Gehen Sie zu "Künstler" Tab
   - Klicken Sie "+ Neuer Künstler"
   - Füllen Sie Name, Land, Debütjahr, Label aus
   - Klicken Sie "Speichern"

2. **Album erstellen**
   - Gehen Sie zu "Alben" Tab
   - Klicken Sie "+ Neues Album"
   - Wählen Sie einen Künstler
   - Geben Sie Titel und Beschreibung ein
   - Klicken Sie "Speichern"

3. **Songs hinzufügen**
   - Gehen Sie zu "Songs" Tab
   - Klicken Sie "+ Neuer Song"
   - Wählen Sie das Album
   - Geben Sie Title, Genre, Dauer ein
   - Klicken Sie "Speichern"

4. **Playlist erstellen**
   - Gehen Sie zu "Playlists" Tab
   - Klicken Sie "+ Neue Playlist"
   - Geben Sie Name und Beschreibung ein
   - Klicken Sie "Speichern"

## 🎨 Design Highlights

- **Weiß & Minimalistisch** - Sauberes, modernes Design
- **Responsive Grid** - Automatische Anpassung auf verschiedenen Bildschirmgrößen
- **Smooth Animations** - Transition Effects bei Hover und Click
- **Card-basiert** - Intuitive Card-Layouts für visuellen Appeal
- **Modal Dialogs** - Non-intrusive Forms für Daten-Eingabe
- **Table View** - Effiziente Datendarstellung für Songs und Benutzer
- **Dark Borders** - Subtile Abgrenzung zwischen Elementen

## 🔧 Entwicklung

### Frontend Hot Reload
Während der Entwicklung unterstützt Vite automatisches Reload bei Änderungen.

### Backend kompilieren
```bash
cd backend
./mvnw clean compile
```

### Tests ausführen
```bash
./mvnw test
```

### Build für Production
```bash
# Frontend
cd frontend
npm run build

# Backend
cd backend
./mvnw clean package
```

## 📝 Lizenz

- Dieses Projekt ist für Lehr- und Demonstrationszwecke erstellt.

## 🤝 Support

Für Probleme oder Fragen:
1. Überprüfen Sie, dass Backend auf Port 8080 läuft
2. Überprüfen Sie MongoDB-Verbindung
3. Überprüfen Sie die Browser-Konsole auf JavaScript-Fehler
4. Überprüfen Sie die Backend-Logs für API-Fehler

---

**Kurz:** Backend mit `./mvnw spring-boot:run` starten, Frontend mit `npm run dev` starten, dann unter http://localhost:5173 zugreifen! 🎵
