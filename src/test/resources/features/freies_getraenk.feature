# language: de
Funktionalität: Freies Getränk
  Der erste Schritt: ein Getränk ohne Bezahlung.
  Wer ein Fach wählt, bekommt die Dose.
#
  Szenario: Ein Getränk wählen
    Angenommen der Automat ist frisch gestartet
    Wenn ich Cola wähle
    Dann liegt eine Dose Cola im Ausgabefach

  Szenario: Preise für Getränke werden angezeigt
    Angenommen der Automat ist frisch gestartet
    Dann hat Cola den korrekten Preis

 Szenario: Kein Bier vor 4
   Angenommen der Automat ist frisch gestartet
   Und es ist 15:59 Uhr
   Wenn ich Bier wähle
   Dann liegt nichts im Ausgabefach

  Szenario: Kein Bier ab Mitternacht
    Angenommen der Automat ist frisch gestartet
    Und es ist 00:00 Uhr
    Wenn ich Bier wähle
    Dann liegt nichts im Ausgabefach

  Szenario: Es gibt Bier nach 4
    Angenommen der Automat ist frisch gestartet
    Und es ist 16:00 Uhr
    Wenn ich Bier wähle
    Dann liegt eine Dose Bier im Ausgabefach

  Szenario: Es gibt Bier bis vor Mitternacht
    Angenommen der Automat ist frisch gestartet
    Und es ist 23:59 Uhr
    Wenn ich Bier wähle
    Dann liegt eine Dose Bier im Ausgabefach