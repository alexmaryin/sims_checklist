# App data storage

All writable app files live under a single per-user root that follows OS conventions:

| Platform | Root |
|----------|------|
| Windows  | `%APPDATA%\SimsChecklist` |
| macOS    | `~/Library/Application Support/SimsChecklist` |
| Linux    | `~/.local/share/SimsChecklist` |
| Android  | the app's internal files directory (`filesDir`) |

The root is created automatically on first use.

## Layout

```
<root>/
  airports.db          airports database (desktop)
  aircraft/            per-aircraft JSON files and custom icons
    catalog.json       manifest of bundled aircraft files
    .deployed-version  app version that last deployed the bundled files
    cessna172.json     bundled aircraft copies (deployed from resources)
    cirrus_sr22.json
    citationx.json
    king_air_350.json
    piper_pa24.json
    custom_<id>.json   user-added aircraft
    custom_<id>.jpg    user-added aircraft icon
```

- On Android the airports database stays at the platform-standard database path
  (`getDatabasePath("airports.db")`); aircraft files live in `filesDir/aircraft/`.
- Airport update CSV downloads are written under the root and deleted after a
  successful database conversion.

## Aircraft catalog deployment

Bundled aircraft ship as one JSON file per aircraft in app resources
(`composeResources/files/aircraft/`). On first start, and whenever the app version
differs from `.deployed-version`, the bundled files are copied into `<root>/aircraft/`,
overwriting bundled copies only. User-added `custom_*` files are never touched.

Aircraft are read from `<root>/aircraft/` first. If a bundled aircraft's file is missing
or invalid, the app falls back to the bundled resource copy. A user-added aircraft with
a missing or invalid file is skipped with a warning.

## Migration from older versions (desktop)

Older versions stored the airports database in `~/.simschecklist/airports.db`.
On first start after upgrading, that file is moved into the new root (content verified;
the legacy folder is removed when empty). If the move fails, the legacy file is kept and
a fresh database is created in the new root — airports can simply be re-downloaded.

**Downgrade note:** if you later install an older app version, it will look for the
database in `~/.simschecklist` again, find nothing, and create an empty database there.
Re-download the airports database from the Airports database screen; no other data is
lost (aircraft app files are ignored by older versions, which read the bundled catalog).
