# language: de
Funktionalität: Als ehrlicher Automatenbetreiber
  möchte ich Wechselgeld herausgeben, wenn jemand Abbruch drückt,
  damit ich nicht eines Tages vor Gericht lande.

  Szenario: Wechselgeld wird ausgeworfen
    Angenommen der Automat ist frisch gestartet
    Wenn ich Münzen im Wert von "TWO_EURO" einwerfe
    Und ich den Vorgang abbreche
    Dann habe ich ein Guthaben von "NONE" Euro
    Und sehe ich Münzen im Wert von "TWO_EURO" in der Münzrückgabe
    Wenn ich die Münzrückgabe entleere
    Dann sehe ich Münzen im Wert von "NONE" in der Münzrückgabe
    
