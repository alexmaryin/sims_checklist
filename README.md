# Sims checklists ✈
###### [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
*My first multi platform App on Kotlin for Desktop & Android*

### Application helps to control, proceed and complete checklists of virtual airplanes as far as calculate fuel quantity for your trip and get METAR/TAF with visualized wind director.

At present checklists are implemented only for the one type of aircraft - Cessna 172 Skyhawk, the most popular general aviation in any simulator.

Someday it will be featured custom editor or import mode from simple text files.

*Some develop details:*
- Jetbrains Compose library for Desktop and Jetpack Compose for Android
- Custom Canvas view for wind director
- METAR/TAF info is fetching from https://metartaf.ru/
- Airports information fetches from my own REST API [ourairports-json](https://github.com/alexmaryin/ourairports_json)
- METAR parser from my library [metarKt](https://github.com/alexmaryin/metarKt) published on MavenCentral.
- Awesome library [Decompose](https://github.com/arkivanov/Decompose) for navigation and lifecycle management, thanks to @arkivanov
- Google Material library for UI with dark and light theme support
- MVI architecture.

Glad to see you soon in virtual skies!

✈ [My virtual flights map with Youtube streams is here.](https://www.google.com/maps/d/edit?mid=1MXxtK3NoMSHi8vue_ZfKOSz--Y9yJjU7&usp=sharing) ✈

**License**
```
Copyright 2021 Alex Maryin

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
