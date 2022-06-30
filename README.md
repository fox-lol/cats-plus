# Cats Plus

TODO: description

### Developing

Clone the repository for local work:

```bash
# Clone and navigate to repository
git clone https://github.com/fox-lol/cats-plus
cd cats-plus

# Generate Minecraft source code
./gradlew genSourcesWithQuiltflower
```

Then run the mod against a specified modloader:

```bash
# Run with Fabric
./gradlew fabric:runClient

# Run with Forge
./gradlew forge:runClient
```

Run a server by substituting `runClient` with `runServer`.

### Build

Create a release build by running:

```bash
./gradlew build
```

### License

Released under [GNU General Public License v3.0](https://github.com/fox-lol/cats-plus/blob/main/LICENSE).
