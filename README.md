# Sims checklists ✈
###### [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![Version](https://img.shields.io/github/v/release/alexmaryin/sims_checklist)](https://github.com/alexmaryin/sims_checklist/releases)

*My first multiplatform App in Kotlin for Desktop & Android*

### Application helps to control, proceed and complete checklists of virtual airplanes as far as calculate fuel quantity for your trip and get METAR/TAF with visualized wind director.

At present checklists are implemented for Cessna 172 Skyhawk, the most popular general aviation in any simulator, Cirrus SR-22, the luxury one, and ultimate Cessna Citation X.
QFE Helper! You can easily convert QFE air pressure given you by ATC to QNH for modern aircrafts which do not support QFE. Also feature allows you to convert Height given in meters AGL (above ground level) to feet ASL (above sea level). 
Top list of recent airports on METAR screen for faster access.

*Some develop details:*
- Compose Multiplatform project in Kotlin 2.20
- Custom drawing for wind director
- METAR/TAF info is fetching from https://checkwx.com/
- Airports information fetches from CSV files (https://davidmegginson.github.io/ourairports-data) and maps to local multiplatform sqlite database (incredibly fast performance).
- METAR parser from my own library [metarKt](https://github.com/alexmaryin/metarKt) published on MavenCentral.
- Awesome library [Decompose](https://github.com/arkivanov/Decompose) for navigation and lifecycle management, thanks to @arkivanov
- Google Material library for UI with dark and light theme support.
- MVI architecture.

Glad to see you soon in virtual skies!

**License**

Copyright 2023-2025 Alex Maryin

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

