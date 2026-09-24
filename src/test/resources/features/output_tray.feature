# language: de
Funktionalität: Kleines Ausgabefach

  Szenario: Ausgabefach hat Platz für 3 Dosen
    Angenommen der Automat ist frisch gestartet
    Wenn 3 Euro im Automat sind
    Und ich Cola wähle
    Und ich Orange wähle
    Und ich Zitrone wähle
    Dann liegt eine Dose Cola im Ausgabefach
    Und liegt eine Dose Orange im Ausgabefach
    Und liegt eine Dose Zitrone im Ausgabefach

  Szenario: Ausgabefach ist voll, viertes Getränk wird abgelehnt
    Angenommen der Automat ist frisch gestartet
    Wenn 4 Euro im Automat sind
    Und ich Cola wähle
    Und ich Orange wähle
    Und ich Zitrone wähle
    Und ich Cola wähle
    Dann ist das Ausgabefach voll
    Und am Display wird "Bitte Ausgabefach leeren" angezeigt

  Szenario: Nach dem Leeren des Ausgabefachs kann wieder gewählt werden
    Angenommen der Automat ist frisch gestartet
    Wenn 4 Euro im Automat sind
    Und ich Cola wähle
    Und ich Orange wähle
    Und ich Zitrone wähle
    Und ich die Dose aus dem Ausgabefach nehme
    Und ich Cola wähle
    Dann liegt eine Dose Cola im Ausgabefach
    Und am Display wird "Prost!" angezeigt

  Szenario: Ausgabefach ist leer nach dem Leeren
    Angenommen der Automat ist frisch gestartet
    Wenn 3 Euro im Automat sind
    Und ich Cola wähle
    Und ich Orange wähle
    Und ich Zitrone wähle
    Und ich die Dose aus dem Ausgabefach nehme
    Dann ist das Ausgabefach leer
