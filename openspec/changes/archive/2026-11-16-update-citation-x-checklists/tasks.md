# Tasks

## 1. Prepare

- [x] 1.1 Re-read the seven scans `openspec/specs/citation-x/Citation_X---001.jpg` … `007.jpg` and note which source sections map to checklists `0`–`13` per the spec table; confirm with the old `id: 2` record and the King Air record only as JSON style reference (indentation, `details` formatting). Verify the mapping list covers every actionable scan section and no excluded section ("Quick Cold and Dark", cross-bleed start, visual pattern, remarks/limits prose, abbreviations, changelog).
- [x] 1.2 Confirm the edit boundary: only the `checklists` array of the `id: 2` object in `shared/src/commonMain/composeResources/files/aircraft.json` will change; `name`, `performance`, `photo` and all other aircraft objects stay byte-identical. Verify by noting the JSON line range of the Citation X `checklists` array before editing.

## 2. Author ground-phase checklists (ids 0–6)

- [x] 2.1 `Preflight` (id 0): from page 2 (interior inspection + interior/exterior check), keep ~8–10 terse items (circuit breakers, oxygen mask/pass oxy, emergency lights, fuel & hydraulic quantity, flaps/slat-stab indications, pitot/static check, chocks & covers removed, exterior inspection complete). Verify against page 2 that no item from the scan's dotted lines is missing except those excluded by the concise-wording rule.
- [x] 2.2 `Pre start` (id 1): from page 3 "Cockpit Preparation & APU Start" (STBY power check-then-off, batteries, external power, lights, avionics+EICAS, IRS align, parking brake/EICAS, APU START with compact limits in `details`, APU bleed, fuel boost, warning/CVR tests, trims, radios/ATIS). Verify the APU `details` contains only the numeric checks (101% RPM, DC ≥22V, Amps <300) and the phrase `hold 5 sec` appears nowhere.
- [x] 2.3 `FMS setup` (id 2): from page 3 "FMS Setup" — POS INIT (mandatory, `details` one line: NAV IDENT → verify database → LOAD GPS 1 POS), flight plan enter/SimBrief recall, departure, arrival/approach, PERF INIT, TAKEOFF perf, VNAV profile optional; ≤7 items. Verify remarks text from the red box is not transcribed.
- [x] 2.4 `Before engine start` (id 3): from page 4 left column; `PFD V-Speeds` item carries the flaps-15° takeoff table (V1/VR/V2 rows, weight columns) as `details` values only. Verify the four weight columns match the scan.
- [x] 2.5 `Engine start` (id 4): from page 4 "Engine Start" + "When Both Engines Running" (APU bleed, PAC isol, signs, EICAS SYS ENG, thrust levers CUT-OFF, start 2 then 1 with compact start-sequence `details` (ITT ≤200°C, IDLE at 10% N2, ignition ~33%, starter disengage ~57%), hydraulic/electrical checks, bleed transfer, generators). Verify cross-bleed start procedure is absent.
- [x] 2.6 `After start / Taxi` (id 5): merge page 4 "After Start / Before Taxi" + "Taxi" (flight controls, STBY gyro, anti-ice, signs, pressurization, taxi clearance, taxi lights, brakes/steering, reverser test compact `details`, taxi speed). Verify reverser deploy/stow narrative is reduced to one `details` line.
- [x] 2.7 `Before takeoff` (id 6): from page 5 "Before Takeoff" holding-short list + "When Entering the Runway" (altimeters, heaters, anti-ice, FGCP yaw/Mach/modes, FMA, pressurization, flaps 15°, speed brakes, `Stab Trim` with CG table as `details`, TCAS TA/RA, WX radar, strobes/landing lights, ATC clearance). Verify the trim table numbers (Flaps 5° and 15° rows) match the scan.

## 3. Author flight-phase checklists (ids 7–13)

- [x] 3.1 `Climb` (id 7): from page 5 "Takeoff" acceleration-altitude config + "After Takeoff / Climb" (gear/flaps UP at 170 KIAS, thrust levers CLB, taxi lights OFF, engine sync, anti-ice, pressurization, climb speed 250 below 10,000 ft, APU SHUTDOWN before FL310 with one-line `details`, landing lights OFF @10,000 ft, altimeters STD at transition). Verify rotate/roll narration from "Normal Takeoff" is absent.
- [x] 3.2 `Cruise` (id 8): from page 5 (thrust levers CRU, anti-ice, pressurization, fuel balance monitor, `TOD X-CHECK` with the rule-of-thumb formula and 2°/3°/4° factors as `details`). Verify the TOD `details` is formula-only.
- [x] 3.3 `Descent` (id 9): from page 5 "Descent Preparation" + "Descent" (clearance AS REQ, initiate at TOD, approach/landing data, FMS approach/landing pages, ILS frequency, minimums, FGCP altitude, pressurization, signs, altimeters local QNH at transition, landing lights ON @10,000 ft). Verify VNAV flying-narrative bullets are absent.
- [x] 3.4 `Approach` (id 10): from pages 5–6 — `VREF/VAPP` item carries the full-flaps landing speed table as `details`; single `PERFORM` item carries the config schedule (`@20 NM 190-200 / @12-15 NM 150-160, Flaps 15 @170 / @1 500 ft Gear DOWN, Flaps FULL / min VREF+10`); `Landing Check` (gear 3 green, flaps FULL, landing lights ON, engine sync OFF); ignition NORM, fuel crossfeed OFF, lights, engine sync, APU AS REQ; one `Go-around` item with compact `details`. Verify the visual pattern approach and ILS remarks box are not transcribed as separate blocks.
- [x] 3.5 `After landing` (id 11): from page 7 "After Vacating the Runway" + "Taxi In" (landing off/taxi on, anti-coll GND REC, reversers stowed, speed brakes zero, flaps up, WX radar off, TCAS STBY, heaters, anti-ice, APU AS REQ). Verify no APU START item exists here.
- [x] 3.6 `Parking` (id 12): from page 7 "Parking" + "When ITT Has Stabilized" (parking brake, taxi lights off, anti-ice all off, signs, ground rec off, pressurization zero diff, thrust levers CUT-OFF). Verify EFB ground-services narration appears at most as `Chocks, covers, GPU AS DES` `details` on one item.
- [x] 3.7 `Shutdown` (id 13): from page 7 "SHUTDOWN" (IRS off, STBY gyro caged, STBY power off, avionics+EICAS off, emergency lights off, fuel crossfeed off, gravity X-flow off, APU SHUTDOWN with one-line `details`, external lights off, batteries off).

## 4. Integrate and verify

- [x] 4.1 Validate `aircraft.json` parses (lint the whole file) and the Citation X record has checklist ids `0`–`13` unique and sequential, all other aircraft objects untouched (git diff shows changes only inside the Citation X `checklists` array).
- [x] 4.2 Run the existing `commonTest` suite (or an added deserialize check for `id: 2`) and verify it deserializes with 14 checklists whose captions match the spec table order.
- [x] 4.3 Grep the Citation X record for banned artifacts: `PACS`, `hold 5 sec`, `Vref... 132`, `145 Kt`, `"LINE"`, `Replay`; verify zero matches, and verify `details` strings contain no `(dep. on weight)`-style qualifiers.
- [ ] 4.4 Spot-check every numeric `details` (V1/VR/V2, VREF/VAPP, stab trim, TOD, start limits, APU limits) against the corresponding scan page; run the app and open all 14 Citation X checklists to confirm rendering and progress toggling work.

## 5. User amendments (review round)

- [x] 5.1 Remove the `FMS setup` checklist; renumber remaining checklist ids to `0`–`12`. Verify captions/ids in `aircraft.json` match the updated 13-checklist spec table.
- [x] 5.2 Remove the `Taxi speed` item from `After start / Taxi` (10 items remain). Verify by parsing the record.
- [x] 5.3 Remove the `Approach`, `Landing check`, `Landing roll` and `Go-around` items from `Approach` (8 switch items remain). Verify by parsing the record.
- [x] 5.4 Sync proposal/design/spec delta to the 13-checklist scope and extend `CitationXChecklistsTest` with the removal assertions; verify with `openspec validate --strict` and a green `:shared:desktopTest`.

## Workflow follow-up

- Archive the change after review; archiving backfills the new `citation-x-data` capability spec (the source folder `openspec/specs/citation-x/` keeps the scans unchanged).
