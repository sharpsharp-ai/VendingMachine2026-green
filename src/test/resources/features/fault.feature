# language: de
Funktionalität: Als Kunde vor einem kaputten Automaten
  möchte ich eine Servicenummer sehen,
  damit ich mein Geld zurückbekomme.

  Szenario: Bei Störung wird Servicenummer angezeigt
    Angenommen der Automat ist frisch gestartet
    Wenn 2 Euro im Automat sind
    Und ich Cola wähle
    Und der Automat eine Störung hat
    Dann am Display wird "Störung – Service: 0800 123 456" angezeigt
    Und sehe ich Münzen im Wert von "TWO_EURO" in der Münzrückgabe
