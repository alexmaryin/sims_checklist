## 1. Prepare

- [x] 1.1 Re-read the extracted source text of `openspec/specs/king-air-checklist/checklists.pdf` (use `pdftotext -layout` if needed) and the existing records in `shared/src/commonMain/composeResources/files/aircraft.json` for style reference.
- [x] 1.2 Confirm the drawable `king_air_350.jpg` exists in `composeResources/drawable` (photo key `king_air_350`) and that no existing aircraft uses `id: 3`.

## 2. Add the aircraft record

- [x] 2.1 Append a new object to the top-level array in `aircraft.json` with `id: 3`, `name: "Beech King Air 350"`, and `photo: "king_air_350"`.
- [x] 2.2 Add the `performance` block: `fuelCapacity: 539`, `averageFuelFlow: 125`, `averageCruiseSpeed: 310`.

## 3. Author the ten checklists (ids 0–9, in order)

- [x] 3.1 `Preflight` (id 0): transcribe PREFLIGHT items (documents, parking brake, gear, trims, battery bus, voltmeter checks, fuel quantity, flaps check, battery OFF, oxygen preflight).
- [x] 3.2 `Engine start (cold)` (id 1): transcribe doors/load/passenger/cabin, fuel panel, pilot panel, right-then-left engine start sequence; keep the per-engine start steps as a multi-line `details` block, ending with generators ON and 1050 RPM.
- [x] 3.3 `Before taxi` (id 2): transcribe avionics/GPS/AP, NAV/CDI, transponder STBY, altimeter, lights, standby display, prop sync, flaps UP, instruments/controls checks, parking brake release, brakes/steering check.
- [x] 3.4 `Before takeoff` (id 3): transcribe avionics/NAV/transponder/altimeter checks, cabin pressurization, trim TAKEOFF, flaps APPROACH, autofeather ARM, prop feather check, fuel, V1/VR/V2 SET, anti-ice sub-panel (as `details`), lights, CAS, ATC; attach takeoff power limits (`100% TQ / 820° ITT / 104% N1 / 1700 RPM`) and V1/VR/V2 speeds as a `details` comment.
- [x] 3.5 `Climb` (id 4): transcribe taxi lights OFF, flaps UP, gear retracted, yaw damper ON, anti-ice, pressurization; attach cruise-climb power limits, recommended 1600 RPM, and the cruise-climb speed schedule (SL→35,000 ft) as a `details` comment.
- [x] 3.6 `Cruise` (id 5): transcribe autofeather OFF, pressurization set/check, anti-ice, auto-ignition, cabin signs; attach max/normal cruise power table and maximum range power (45% TQ) as a `details` comment.
- [x] 3.7 `Descent` (id 6): transcribe avionics/ILS/NAV set, approach speeds confirm, pressurization, cabin signs, anti-ice, fuel balance, prop 1500 RPM, descent speed/power, 10,000 ft and transition-altitude items; attach TOD rule and 3° sink-rate (5×GS) as a `details` comment.
- [x] 3.8 `Approach` (id 7): transcribe ATC, NAV/CDI VLOC, autofeather ARM, cabin signs, pressurization, altimeter; attach the visual/ILS approach speed-and-configuration schedule (150–160 KIAS, flaps/gear/VREF ~100–110 KIAS) as `details` comments.
- [x] 3.9 `After landing` (id 8): transcribe taxi/landing/strobe lights OFF, engine auto-ignition OFF, engine anti-ice ON, transponder STBY, flaps RETRACT, cabin pressurization CHECK 0.
- [x] 3.10 `Parking` (id 9): transcribe PARKING / SHUT DOWN items (parking brake SET, avionics OFF, autofeather OFF, exterior/interior lights OFF, ITT stabilized, condition levers FUEL CUTOFF, props FEATHER, overhead OFF, battery/generators OFF below 15% N1).

## 4. Verify

- [x] 4.1 Validate `aircraft.json` is well-formed JSON (parse/lint) and that the array now has four entries with unique ids `0`–`3`.
- [x] 4.2 Verify the King Air record deserializes into `Aircraft` (run the existing `commonTest` suite or an equivalent deserialize check) with ten checklists, captions and ids matching the spec table.
- [x] 4.3 Spot-check that at least the `Cruise` and `Climb` checklists carry `details` comments containing the PDF reference numbers (power/speed figures).
- [x] 4.4 Build/run the app (or the aircraft list screen) to confirm the King Air 350 appears with its photo and opens its checklists without errors.
