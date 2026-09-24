# language: de
Funktionalität: Als Kunde
  möchte ich sehen, wie viel Geld ich schon eingeworfen habe,
  damit ich nicht zu viel in den Automaten werfe.

  Szenario: Guthaben wird angezeigt
    Angenommen der Automat ist frisch gestartet
    Wenn ich Münzen im Wert von "ONE_EURO" einwerfe
    Dann habe ich ein Guthaben von "ONE_EURO" Euro
