# Sims Checklists - Project Overview

## Project Overview

**Sims Checklists** is a Kotlin Multiplatform application for Desktop & Android that helps users manage aviation checklists, calculate fuel quantities, and access METAR/TAF weather information with wind visualization.

### Purpose
The application assists pilots (primarily flight simulation enthusiasts) with:
- Pre-flight checklists for popular aircraft
- Fuel planning based on distance, wind, and alternate airports
- METAR/TAF weather data retrieval with wind direction visualization
- Airport database exploration with runway information
- QFE to QNH pressure conversion for modern aircraft
- Cold temperature altitude correction for instrument approaches

### Architecture
- **MVI (Model-View-Intent)** architecture pattern
- **Compose Multiplatform** for UI (Kotlin 2.20)
- **Decompose** for navigation and lifecycle management
- **Koin** for dependency injection
- **Room** database for local data storage
- **Ktor** for HTTP client

### Supported Aircraft
- Cessna 172 Skyhawk
- Cirrus SR-22
- Cessna Citation X (X-plane 12 default edition)

## Project Structure

```
sims_checklist/
├── shared/          # Common Kotlin Multiplatform code
│   ├── src/
│   │   ├── commonMain/    # Shared code for all platforms
│   │   ├── androidMain/   # Android-specific implementations
│   │   ├── desktopMain/   # Desktop-specific implementations
│   │   └── commonTest/    # Shared tests
│   └── schemas/           # Room database schemas
├── android/         # Android application module
├── desktop/         # Desktop application module
└── config/          # Configuration files
```

### Key Modules (shared/src/commonMain/kotlin)
- `feature/` - UI screens and business logic (MVI pattern)
  - `checklists/` - Checklist management
  - `fuelcalculator/` - Fuel calculation
  - `metarscreen/` - METAR/TAF weather display
  - `airportsBase/` - Airport database browser
  - `qfeHelper/` - Pressure conversion utility
  - `coldTemperature/` - Cold temperature altitude correction with FAA segment-based corrections
- `services/` - Backend services (METAR, airports, cold temperature)
- `di/` - Dependency injection modules
- `decompose/` - Navigation configuration
- `commonUi/` - Shared UI components

## Building and Running

### Prerequisites
- JDK 21
- Android SDK (for Android builds)
- API key for checkwx.com (METAR service)

### Setup
1. Create a local.properties file in the project root
2. Add your Weather API key:
   ```
   WXAPI_KEY=your_api_key_here
   ```

### Build Commands

**Desktop Application:**
```bash
# Run desktop app
./gradlew :desktop:run

# Build desktop distribution
./gradlew :desktop:package
./gradlew :desktop:packageDeb    # Linux
./gradlew :desktop:packageDmg    # macOS
./gradlew :desktop:packageMsi    # Windows
```

**Android Application:**
```bash
# Install on connected device
./gradlew :android:installDebug

# Build APK
./gradlew :android:assembleDebug
```

**Shared Module:**
```bash
# Compile shared code
./gradlew :shared:compileKotlinDesktop
./gradlew :shared:compileKotlinAndroid
```

### Running from IDE
- Use the provided run configurations in `.run/` directory
- Desktop: `desktop.run.xml`
- Android: `android.run.xml`

## Development Conventions

### Code Style
- Kotlin coding conventions
- Compose Multiplatform for all UI code
- Material Design 3 components with light/dark theme support
- Adaptive layouts for different screen sizes
- Use Material3 segmented buttons for multi-option selections
- Cards use default surface colors for theme adaptation

### Testing
- Unit tests in `commonTest` source set
- Use `./gradlew test` to run all tests
- Platform-specific tests in respective source sets

### Key Dependencies
- **Compose Multiplatform** - UI framework
- **Decompose** - Navigation and component lifecycle
- **Koin** - Dependency injection
- **Room** - Local database
- **Ktor** - HTTP client
- **kotlinx.serialization** - JSON serialization
- **kotlinx.datetime** - Date/time handling
- **metarKt 1.2.1** - METAR parsing and cold temperature corrections (custom library)

### Data Sources
- **METAR/TAF**: https://checkwx.com/
- **Airport Database**: https://ourairports.com/ (CSV files)

### Important Notes
- Runway headings are in **true courses** (not magnetic)
- Wind information from METAR is referenced to **true north**
- Cold Temperature Corrector uses FAA AIM 7-3-6 segment-based correction method
- The app does not collect any user data (see policy.md)

## Version Management
The project uses [refreshVersions](https://github.com/jmfayard/refreshVersions) for dependency management:
```bash
# Check for dependency updates
./gradlew refreshVersions
```

## License
Apache License 2.0 - Copyright 2023-2025 Alex Maryin
