// URL des Endpunkts
const url = 'http://localhost:8080';

// Das JSON-Objekt, das an den Server gesendet wird
const groupData = {
  name: "testName" // Beispiel-Parameter
};

// Fetch-Request (POST), um die Gruppe zu erstellen
fetch(url + "/api/group/create", {
  method: 'POST', // HTTP-Methode
  headers: {
    'Content-Type': 'application/json' // JSON-Daten senden
  },
  body: JSON.stringify(groupData) // Das JSON-Objekt als String an den Server senden
})
  .then(response => {
    if (response.ok) {
      // Wenn die Antwort erfolgreich ist (Status 200–299)
      return response.json(); // Die Antwort als JSON parsen
    } else {
      throw new Error('Fehler beim Erstellen der Gruppe');
    }
  })
  .then(data => {
    console.log('Erfolgreich erstellt:', data); // Hier kannst du die Antwort verarbeiten

    // Den Link von der Antwort holen
    const groupLink = data.link;
    
    // Den Link verwenden, um der Gruppe beizutreten
    if (groupLink) {
      console.log('Versuche, der Gruppe beizutreten:', groupLink);

      console.log(url + groupLink)

      // Zweite POST-Anfrage senden, um der Gruppe beizutreten
      fetch(url + groupLink, {
        method: 'POST', // POST-Methode für Beitritt
        headers: {
          'Content-Type': 'application/json' // Falls du JSON-Daten senden musst
        },
        // Optional: Du kannst Daten an den Server senden, wenn nötig
        body: JSON.stringify({
          // Hier können die Daten stehen, die du für den Beitritt sendest, z.B. Benutzer-ID
          name: 'cooler tüp', // Beispiel-Daten
        })
      })
        .then(response => {
          if (response.ok) {
            return response.json(); // Antwort als JSON verarbeiten
          } else {
            throw new Error('Fehler beim Beitritt zur Gruppe');
          }
        })
        .then(joinResponse => {
          console.log('Erfolgreich der Gruppe beigetreten:', joinResponse);
        })
        .catch(error => {
          console.error('Fehler beim Beitritt:', error);
        });
    }
  })
  .catch(error => {
    console.error('Fehler:', error); // Fehlerbehandlung
  });
