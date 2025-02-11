<div align="center">

<a href="https://serlith.net">
  <img src="assets/jellyfish_banner.png" alt="Jellyfish">
</a>

## 🪼 Jellyfish 🪼
Jellyfish is a [Paper](https://github.com/PaperMC/Paper) fork implementing patches from [Pufferfish](https://github.com/pufferfish-gg/Pufferfish/).

</div>

## License
All patches are licensed under the MIT license.

[![MIT License](https://img.shields.io/github/license/PurpurMC/Purpur?&logo=github)](LICENSE)

See [PaperMC/Paper](https://github.com/PaperMC/Paper), and [PaperMC/Paperweight](https://github.com/PaperMC/paperweight) for the license of material used by this project.

## Building and setting up

#### Initial setup
First, <u>clone</u> this repository. Do not download it.

Then run the following command in the root directory:

```
./gradlew applyAllPatches
```

The project is now ready for use in your IDE.

#### Creating a patch

See [CONTRIBUTING.md](CONTRIBUTING.md).

#### Compiling

Use the command `./gradlew build` to build the API and server. Compiled JARs
will be placed under `jellyfish-api/build/libs` and `jellyfish-server/build/libs`.
**These JARs are not used to start a server.**

To compile a server-ready purpurclip jar, run `./gradlew createMojmapBundlerJar`.
To install the `jellyfish-api` and `purpur` dependencies to your local Maven repo, run `./gradlew publishToMavenLocal`. The compiled purpurclip jar will be in `jellyfish-server/build/libs`.

# Credits:

1. PaperMC Team
2. Pufferfish Team
3. PurpurMC Team, even if this is not a direct Purpur fork, we do use their project setup and a couple patches.

