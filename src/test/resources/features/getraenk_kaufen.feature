# language: de
Funktionalität: Man kann ein Getränk kaufen

  Szenario: Man hat zu wenig Geld für ein Getränk
    Angenommen der Automat ist frisch gestartet
    Wenn kein Geld im Automat ist
    Und ich Cola wähle
    Dann ist das Ausgabefach leer
    Und am Display wird "zu wenig Geld" angezeigt

  Szenario: Man hat nicht genug Geld für ein Bier
    Angenommen der Automat ist frisch gestartet
    Wenn 1 Euro im Automat sind
    Und ich Bier wähle
    Dann ist das Ausgabefach leer
    Und am Display wird "zu wenig Geld" angezeigt

  Szenario: Man hat genug Geld für ein Getränk
    Angenommen der Automat ist frisch gestartet
    Wenn 2 Euro im Automat sind
    Und ich Cola wähle
    Dann liegt eine Dose Cola im Ausgabefach
    Und am Display wird "Prost!" angezeigt
    Und ein Restgeld von 1 Euro wird angezeigt

  Szenario: Man hat genug Geld für ein Bier
    Angenommen der Automat ist frisch gestartet
    Wenn 2 Euro im Automat sind
    Und ich Bier wähle
    Dann liegt eine Dose Bier im Ausgabefach
    Und am Display wird "Prost!" angezeigt