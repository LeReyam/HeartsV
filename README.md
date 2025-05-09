# HeartsV

![Build](https://github.com/LeReyam/HeartsV/actions/workflows/ci.yml/badge.svg)
[![Coverage Status](https://coveralls.io/repos/github/LeReyam/HeartsV/badge.svg)](https://coveralls.io/github/LeReyam/HeartsV)

Ein Scala-Projekt zur Umsetzung des Kartenspiels Hearts im Rahmen einer Vorlesung.

## Projektstruktur

- `src/main/scala/de/htwg/se/Hearts` – Hauptcode
- `src/test//scala/de/htwg/se/Hearts` – Tests
- `build.sbt` – Build-Konfiguration
- `.github/workflows/ci.yml` – GitHub Actions Workflow

## Continuous Integration

Dieses Projekt verwendet GitHub Actions zur automatischen Ausführung von Tests bei jedem Commit. Die Testabdeckung wird mit scoverage ermittelt und über Coveralls veröffentlicht.

## Tests ausführen

Lokal kannst du die Tests und die Coverage-Berichte mit folgendem Befehl ausführen:

```bash
sbt clean coverage test coverageReport
