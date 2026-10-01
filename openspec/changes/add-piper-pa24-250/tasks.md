# Tasks: Add Piper PA-24-250 Comanche

## 1. Prepare

- [x] 1.1 Review the two page images `openspec/specs/piper-pa24/PA-24-250 Chklst_1.png` / `_2.png` as transcription ground truth (PDF text layer has corrupted ligatures), and re-read an existing record (e.g. King Air 350) in `shared/src/commonMain/composeResources/files/aircraft.json` for schema/style reference.
- [x] 1.2 Confirm no existing aircraft uses `id: 4` and that no `piper_pa24` drawable exists yet in `composeResources/drawable`.

## 2. Photo asset

- [x] 2.1 Extract the embedded header JPEG (N5448P side view) from page 1 of `PA-24-250 Comanche Checklist 58-60v1.1.pdf` (e.g. via `pypdf`) and save as `shared/src/commonMain/composeResources/drawable/piper_pa24.jpg`; if unusable, crop the header image from `PA-24-250 Chklst_1.png` instead.
- [x] 2.2 Verify the saved file is a valid JPEG of reasonable quality (opens and renders).

## 3. Add the aircraft record

- [x] 3.1 Append a new object to the top-level array in `aircraft.json` with `id: 4`, `name: "Piper PA-24-250 Comanche"`, `photo: "piper_pa24"`.
- [x] 3.2 Add the `performance` block: `fuelCapacity: 94`, `averageFuelFlow: 10.5`, `averageCruiseSpeed: 140`.

## 4. Author the normal checklists (ids 0–13, in order)

- [x] 4.1 `Before starting` (id 0): external visual inspection, fuel quantity, oil 7–9 qt, control lock, documents, ignition key OFF, avionics master OFF, gear selector Down, master ON, fuel gauges, lights check, flaps Down, master OFF, oxygen quantity.
- [x] 4.2 `Starting the engine` (id 1): parking brake, belts, avionics OFF, fuel selector, breakers, carb heat full cold, alternate static closed, controls, door, throttle 1/4 in (1/2 when hot), mixture full rich, prop full forward, beacon ON, master ON, electric fuel pump on/check/off, prime 3–5, magnetos both, prop "clear", starter max 15 s; then "immediately after the engine fires" oil-pressure/1000 RPM steps and engine-stop fallback as a `details` block.
- [x] 4.3 `Before taxi` (id 2): flaps retract, primer locked, gear selector center OFF, gear indicator green, avionics master ON, transponder 1200 STBY, horizon set, ROC zero, altimeter field height, pitot heat check/off, parking brake free.
- [x] 4.4 `Taxi` (id 3): warm-up note (800–1200 RPM, 2–4 min) as `details`, brakes CHECK, steering CHECK.
- [x] 4.5 `Run up` (id 4): parking brake, controls, mixture, prop, oil temp/pressure, 2000 RPM with the 2200-RPM caution as `details`, mag check @15 in Hg (175/50 RPM limits as `details`), carb heat check with unfiltered-air caution, vacuum 5.0 in Hg, ammeter, prop cycle 3× with drop/500-RPM cautions as `details`, throttle idle check, 1000 RPM.
- [x] 4.6 `Before take off` (id 5): door, seat belts, fuel selector, fuel pump ON, flaps as desired, trims set, engine gauges normal, strobes ON, engine-warmth note as `details`.
- [x] 4.7 `Take off` (id 6): compass/instruments, transponder ALT, pitot heat, mixture rich, throttle full forward, accelerate to VR 85 MPH (74 kt) with normal/short-takeoff (65–75 MPH) rotation, brakes tap, gear retract/amber, flaps retract >84 mph & >200 ft — inline PDF values only, no extra table embeds.
- [x] 4.8 `After take off` (id 7): `Climb` sub-block (VX 84 / VY 105 / en-route 120 MPH) as a grouped `details` item, CHT green, prop/power above 1000 ft AGL, fuel pump OFF-check, gear selector center OFF, trims.
- [x] 4.9 `Enroute` (id 8): power per POH table / 75 % max cruise, leaning above 3000 ft, 30-min tank-switch sequence (pump ON → select opposite → pump OFF → pressure check) as grouped steps.
- [x] 4.10 `Descent` (id 9): prop cruise RPM, MP 15–17 in Hg, CHT green airspeed, engine-clearing every 30 s note and 100 °C floor as `details`.
- [x] 4.11 `Landing` (id 10): four legs `Pattern Entry` / `Downwind` / `Base` / `Final Approach` preserved as grouped items with their sub-steps (belts, fuel fullest, mixture, prop, carb heat, 120 MPH; gear down ≤148 mph/129 kt + green, brakes, pump, landing lights, flaps rest 1 <125; 95 MPH rest 2; 90 MPH flaps as required), power-approach note and 84 MPH short-field figures as `details`.
- [x] 4.12 `After landing` (id 11): fuel pump OFF, flaps retract, strobes OFF (NOT rotating beacon), mixture lean as required.
- [x] 4.13 `Engine shut down` (id 12): parking brake, transponder/radios OFF, 1800 RPM 15–20 s, 1200 RPM 10–20 s, mixture idle cutoff, magnetos OFF, key remove, electrical switches OFF from R/H side.
- [x] 4.14 `Before leaving aircraft` (id 13): fuel tank OFF, controls lock, fresh air inlets close, hours note, pitot cover, tie-downs SECURE.

## 5. Author the emergency checklists (ids 14–17)

- [x] 5.1 `Engine fire when starting` (id 14): mixture pull out, throttle pull out, battery OFF, plane leave.
- [x] 5.2 `Engine failure in flight` (id 15): best glide 105 MPH; `Restart` sub-block (pump ON, fullest tank, carb heat, mixture rich, magnetos check) and `If Prop is not turning` sub-block (prop full fwd, throttle 1/10, primer, starter, fuel pump stays on, nearest airfield) as grouped `details` items.
- [x] 5.3 `Engine fire in flight` (id 16): mixture pull out, heating/ventilation OFF.
- [x] 5.4 `Landing without motor` (id 17): seat belt, search field, radio 121.5, best glide 105, gear-only-on-firm-ground note as `details`, mixture/magnetos/fuel/electrical OFF, doors unlock.

## 6. Verify

- [x] 6.1 Validate `aircraft.json` parses and the array has five entries with unique ids `0`–`4`.
- [x] 6.2 Verify the PA-24 record deserializes into `Aircraft` (existing `commonTest` suite or equivalent) with eighteen checklists, ids/captions matching the spec table.
- [x] 6.3 Spot-check exclusions and cautions: no `Go around` or `Speeds (IAS)` content, 2200-RPM run-up caution present as `details`.
- [x] 6.4 Build/run the app; confirm the Comanche card shows the extracted photo and all 18 checklists render.
