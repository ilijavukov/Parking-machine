# dom_1 — Parking machine

Java project that simulates parking machines and payments by tariff zones.

How it works:

- `Tarifnik` — stores prices for tariff zones.
- `ParkingAutomat` — charges for parking and keeps the collected amount per machine.
- `TarifiranoVozilo` / `Tarifirano` — vehicle model with a tariff zone.
- `AktivnaParkingZona` — runs a thread that simulates vehicle arrivals and delegates payments to machines.
- `Main` — example usage: configures the tariff, opens the zone, waits, and closes the simulation.

Input

- The program has no interactive input; it runs a simulation (no CLI arguments).

Sample output:


Kreiran tarifnik: Tarifnik[100, 80, 50]
Kreiran prototip automat sa ID: 1
Kreirana zona: Centar sa 3 automata
Otvaranje zone...
Zona je otvorena. Vozila počinju da pristižu...

STANJE POSLE PRVE FAZE:
Centar(240): 1(120), 2(60), 3(60)
Ukupno naplaćeno: 240

Zatvaranje zone...
KONAČNO STANJE:
Centar(520): 1(300), 2(120), 3(100)
Ukupno naplaćeno: 520

SIMULACIJA ZAVRŠENA

Project structure:

- `src/dom_1/Main.java` — starts the simulation
- `src/dom_1/AktivnaParkingZona.java` — simulation and thread
- `src/dom_1/ParkingAutomat.java` — payment logic
- `src/dom_1/Tarifnik.java` — prices per zone
