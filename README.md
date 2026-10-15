# Getränkeautomat

Startstand für das Training. Die Seite ist fertig, der Automat dahinter tut noch nichts: Jeder Klick
kommt an, ändert aber nichts. Genau ein Szenario ist rot, „Freies Getränk". Alles Weitere entsteht
Story für Story, testgetrieben; die Stories stehen in `specs/stories.md`.
Code auf Englisch, Fachsprache, Oberfläche und Szenarien auf Deutsch.

Fertig ist eine Änderung, wenn `mvn -q verify` ohne Ausgabe und mit Exit-Code 0 endet.

## Starten

Voraussetzung: JDK 17 oder neuer und Maven. Chrome nur für den Rauchtest.

```bash
mvn -q verify                  # alle Tests und alle Gates; keine Ausgabe heißt grün
mvn test -Dtest=RunSmokeTest   # der Rauchtest im Browser, braucht Chrome; läuft nicht in mvn verify
mvn compile exec:java          # Automat starten, dann http://localhost:7070 öffnen
mvn package                    # Fat-Jar bauen …
java -jar target/getraenkeautomat.jar   # … und starten (PORT=8080 wählt einen anderen Port)
```

Es gibt kein Nachfüllen: Ein Neustart setzt den Automaten zurück.

IntelliJ: Die Run-Konfigurationen liegen im Repo unter `.idea/runConfigurations/`. „Alle Tests" läuft alles,
die Szenarien eingeschlossen, weil `RunCucumberTest` sie als JUnit-Test kapselt. „Alle Szenarien (Cucumber direkt)"
startet nur die Feature-Dateien über Cucumbers eigenen Runner, praktisch für ein einzelnes Szenario aus dem Editor.
„Rauchtest (sichtbar)" lässt den Selenium-Test in einem sichtbaren Chrome laufen.
Ein Rechtsklick auf eine Feature-Datei oder ein Szenario nutzt ebenfalls Cucumbers Runner.

## Mit opencode arbeiten

Das Repo bringt opencode feste Rollen mit und für jede Rolle einen Command.
Nichts installieren: `opencode` im Projektordner starten (oder aus IntelliJ anbinden), dann kennt es die Commands.

```text
/spec 1                              Spec nach specs/1-alles-umsonst/spec.md
/akzeptanztest 1                     Szenarien nach features/, Schritte nach VendingMachineSteps.java
/implementiere Ein Getränk wählen    genau ein Szenario grün, Test zuerst
/review                              Befunde mit Datei:Zeile, Smell, Vorschlag
```

Vor jedem Command eine neue Session (`/new`): der Command bringt alles mit, was die Rolle wissen muss.
Nach jedem grünen Szenario selbst committen, keine Rolle darf `git commit`.

Vom Terminal aus: `scripts/bis-gruen.sh "Ein Getränk wählen"` ruft den Implementierer bis zu fünfmal, bis `mvn -q verify` grün ist.

| Datei | Wirkung |
|---|---|
| `AGENTS.md` | Befehle, Struktur, Arbeitsweise, Code-Regeln. Liest jede Rolle in jeder Session |
| `src/test/resources/features/AGENTS.md` | Regeln für Szenarien. Lädt opencode, sobald eine Feature-Datei gelesen wird |
| `opencode.json` | die Rollen mit ihren Rechten, die Bash-Whitelist |
| `.opencode/commands/*.md` | die Commands; lesbares Markdown, das ist der Prompt |
| `.opencode/skills/*/SKILL.md` | Checklisten, die die Commands einfügen |
| `scripts/bis-gruen.sh` | Implementierer in Schleife, bis grün, höchstens fünf Runden |

| Rolle | Darf ändern |
|---|---|
| `spec-autor` | nur `specs/` |
| `test-autor` | Feature-Dateien, `*Steps.java`, `ParameterTypes.java`, `Fake*.java` |
| `implementierer` | alles außer Feature-Dateien, Schritten, `specs/`, `pom.xml`, `config/`, `.opencode/`, `AGENTS.md` |
| `reviewer` | nichts |

In der Shell dürfen alle Rollen dasselbe: mvn, ls, cat, grep, git status/diff/log und `scripts/steps-glossar.sh`.
Keine Rolle darf ins Netz, Fragen stellen oder außerhalb des Projekts arbeiten. Was nicht auf der Whitelist
steht, ist verboten, auch `sed -i` und `python3`: Dateien ändert nur das Edit-Werkzeug, und das prüft die Rechte.

## Stand

Drei Klassen in `src/main/java`:

| Klasse | Was sie tut |
|---|---|
| `VendingMachine` | ignoriert jede Aktion und meldet immer den Zustand nach dem Einschalten. Hier entstehen die Regeln |
| `Drink` | die vier Fächer mit Namen, in ihrer Reihenfolge auf der Front; Preise kommen mit Story 2 |
| `Clock` | die Uhr des Automaten, eine Methode `now()`; `Main` gibt die Systemzeit, die Tests eine `FakeClock`, die sie stellen |
| `Main` | startet Javalin, liefert die Seite aus und übersetzt zwischen HTTP, JSON und dem Automaten |

Beträge sind `int` in Cent, Meldungen sind Strings. Die Seite (`src/main/resources/public/`) formatiert
selbst, zeigt so viele Fächer, wie der Zustand liefert, einen Preis nur, wenn der Automat einen kennt,
und blinkt rot, wenn `refused` gesetzt ist.

`src/test/resources/features/freies_getraenk.feature` ist das erste Szenario und rot: Wer ein Fach wählt,
bekommt die Dose. `mvn -q verify` und die Pipeline sind rot, bis es grün ist. Weitere Szenarien gibt es nicht.

## Stories und Specs

| Wo | Was |
|---|---|
| `specs/stories.md` | die elf Stories mit Akzeptanzkriterien, in Reihenfolge |
| `specs/glossar.md` | Fachbegriffe und alle vorhandenen Schritte; erzeugt von `scripts/steps-glossar.sh`, nach jedem neuen Schritt neu laufen lassen |
| `specs/<nr>-<name>/spec.md` | je Story eine halbe Seite: Story, Regeln, offene Fragen |
| `src/test/resources/features/<name>.feature` | je Story die Szenarien; sie sind die Taskliste |

## Tests und Gates

`mvn -q verify` lässt alles laufen und bricht beim ersten Verstoß ab:

| Gate | Werkzeug | Wo |
|---|---|---|
| Akzeptanz: Szenarien direkt gegen `VendingMachine` | Cucumber, JUnit-4-Runner | `VendingMachineSteps`, `features/` |
| Unit-Tests | JUnit 4, Hamcrest, Mockito | `src/test/java` |
| HTTP: echter Javalin auf freiem Port, das JSON, das die Seite bekommt | Javalin Testtools | `WebTest` |
| Rauchtest: echter Chrome lädt die Seite; nur per `-Dtest=RunSmokeTest`, nicht in `mvn verify` | Cucumber, Selenium | `smoke/` |
| Architektur: nur `Main` kennt Javalin und JSON, niemand hängt von `Main` ab | ArchUnit | `ArchitekturTest` |
| Stil: Methoden höchstens 20 Zeilen, Dateien höchstens 200, Komplexität höchstens 6 | Checkstyle | `config/checkstyle.xml` |
| Abdeckung: mindestens 80 % der Zeilen außerhalb von `Main` laufen in Tests | JaCoCo | `pom.xml`, Bericht in `target/site/jacoco/` |

Vorgehen pro Story: Spec, Szenarien, dann je Szenario roter Test, Code, Refactoring, Commit.

## Rauchtest im Browser (Selenium)

`smoke/automat_im_browser.feature` lädt die Seite in einem echten Chrome und prüft Display und Fächer.
Das Page Object `MachinePage` wartet auf den erwarteten Zustand, alle halbe Sekunde nachsehend,
höchstens 10 Sekunden.

```bash
mvn test -Dtest=RunSmokeTest                       # kopflos
mvn test -Dtest=RunSmokeTest -Dsmoke.headed=true   # Browser sichtbar
```

Chrome muss installiert sein; den passenden chromedriver lädt Selenium Manager beim ersten Lauf nach
`~/.cache/selenium`. Schlägt ein Schritt fehl, hängt ein Screenshot im Bericht `target/smoke-report.html`.

## Pipeline (GitHub Actions)

`.github/workflows/ci.yml` hat drei Jobs, die parallel laufen:

| Job | Was er tut | Ergebnis |
|---|---|---|
| `build` | `mvn package` ohne Tests | Fat-Jar als Artefakt |
| `test` | alles außer dem Rauchtest, `mvn verify` | Testergebnisse als Check am Commit, JaCoCo und Cucumber-Bericht als Artefakte |
| `rauchtest` | nur `RunSmokeTest` im vorinstallierten Chrome des Runners | Testergebnisse als Check, Cucumber-Bericht mit Screenshot bei Fehlschlag |

Das Maven-Repository wird zwischen den Läufen aufgehoben.

## HTTP-API

Jede Aktion antwortet mit dem neuen Zustand; die Seite rendert ihn komplett neu.

```
GET  /api/state
POST /api/insert/{cents}     50 | 100 | 200
POST /api/select/{drink}     COLA | ORANGE | LEMON | BEER
POST /api/cancel
POST /api/take-drinks        Ausgabefach leeren
POST /api/take-coins         Münzrückgabe leeren
```

Der Zustand: `credit` in Cent, `message`, `refused`, `slots` (Nummer, Getränk, Name, Preis in Cent, Dosen),
`outputTray` (Dosen), `coinReturn` (Münzen in Cent).
