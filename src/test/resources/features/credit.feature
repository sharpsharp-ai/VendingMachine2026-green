# language: de
Funktionalität: Als Kunde
  möchte ich sehen, wie viel Geld ich schon eingeworfen habe,
  damit ich nicht zu viel in den Automaten werfe.

  Das eingeworfene Geld wird als „Guthaben: x,xx €“ angezeigt.
  Der Automat nimmt 50 ct, 1 € und 2 €; die Münzen addieren sich.

  Szenario: Guthaben wird angezeigt
    Angenommen der Automat ist frisch gestartet
    Wenn ich Münzen im Wert von "ONE_EURO" einwerfe
    Dann habe ich ein Guthaben von "ONE_EURO" Euro
