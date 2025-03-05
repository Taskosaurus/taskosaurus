"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = __importDefault(require("express"));
const json_server_1 = __importDefault(require("json-server"));
const app = (0, express_1.default)();
// Setze den Port
const port = 3000;
// JSON-Server Setup
const router = json_server_1.default.router('db.json'); // Datenbank, die die Datei db.json liest
const middlewares = json_server_1.default.defaults(); // Standard-Middleware von json-server
// Nutze json-server Middleware
app.use(middlewares);
app.use(json_server_1.default.bodyParser); // Damit POST- und PUT-Anfragen funktionieren
// Einfache Express-Route für eine benutzerdefinierte Antwort
app.get('/secret', (_req, res) => {
    const answer = 'You found my secret!';
    res.send(answer);
});
// Zähler-Route, die den Zugriff auf den "Zähler" gibt
let counter_user = 0;
app.use((req, _res, next) => {
    if (req.url.includes('html') || req.url === '/') {
        counter_user++;
        console.log(`Requests: ${counter_user}`);
    }
    next();
});
// MIDDLEWARE für statische Dateien
app.use(express_1.default.static(__dirname + '/../public'));
// Nutze json-server Routen
app.use('/api', router); // Alle API-Routen von json-server unter /api
// SERVER starten
app.listen(port, () => {
    console.log('*** Server gestartet ***');
    console.log(`Erreichbar unter http://localhost:${port}`);
});
