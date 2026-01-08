# Compose Desktop Template

A modern, cross-platform desktop application template built with Jetpack Compose Multiplatform.

This template was created to simplify the process of starting new desktop applications, eliminating the need for repetitive boilerplate code and setup.

## Demo

This template includes a simple demo app that mimics the UI/UX of an AI chat application, showcasing:
- Integration with [apifreellm.com](https://apifreellm.com) free API
- Modern UI built with Compose and JetBrains Jewel components
- Support for both dark and light themes
- Custom icons from Lucide and Jewel

![Screenshot 1](images/screenshot1.png)
*Main application window*

![Screenshot 2](images/screenshot2.png)
*Application in action*

## Features

- 🖥️ Cross-platform support (Windows, macOS, Linux)
- 🎨 Modern UI with Jetpack Compose for Desktop
- 🏗️ CI/CD pipeline for automated builds
- 📦 Packaged as native installers for all platforms
- 🎨 Uses JetBrains Jewel UI components for a polished look
- 🔄 Hot reload for faster development
- 🌓 Dark and light theme support
- 🚀 Optimized for performance and minimal resource usage

## Prerequisites

- JDK 17 or higher ([Download](https://adoptium.net/))
- IntelliJ IDEA (recommended) or Android Studio
- Gradle 8.0 or higher
- macOS 10.14+ / Windows 10+ / Linux (64-bit)

## Getting Started

1. Clone the repository
   ```bash
   git clone https://github.com/yourusername/compose-desktop-template.git
   ```
2. Open the project in IntelliJ IDEA or Android Studio
3. Run the application using the Gradle task: `run`

## Downloads

[![Build Status](https://github.com/eltonkola/DesktopApp/actions/workflows/package-all.yml/badge.svg?branch=main)](https://github.com/eltonkola/DesktopApp/actions/workflows/package-all.yml)

Latest development builds are available from GitHub Actions:

- **Windows**: [Download .msi](https://nightly.link/eltonkola/DesktopApp/workflows/package-all/main/app.msi.zip)
- **macOS**: [Download .dmg](https://nightly.link/eltonkola/DesktopApp/workflows/package-all/main/app.dmg.zip)
- **Linux**: [Download .deb](https://nightly.link/eltonkola/DesktopApp/workflows/package-all/main/app.deb.zip)

> 💡 [View all artifacts from the latest build](https://github.com/eltonkola/DesktopApp/actions?query=workflow%3Apackage-all)
> 
> Note: You'll need to unzip the downloaded files before installing.

## Building

### Local Build

```bash
./gradlew package
```

### Create Distributables

Build platform-specific packages:

```bash
./gradlew packageDmg        # macOS
./gradlew packageMsi        # Windows
./gradlew packageDeb        # Linux (Debian/Ubuntu)
```

> **Note**: The build process will automatically include required JDK modules. To optimize the final binary size, unused JDK modules are excluded based on your application's requirements.

## CI/CD

This project includes GitHub Actions workflow that automatically builds and packages the application for all major platforms:

- Windows (.msi)
- macOS (.dmg)
- Linux (.deb)

The workflow triggers on pushes to the `main` branch and on new releases.

## Dependencies

- Jetpack Compose Multiplatform (1.5.0)
- JetBrains Jewel UI (0.9.0)
- Ktor Client (2.3.0) for networking
- Kotlinx Serialization (1.5.0)
- SLF4J (2.0.7) for logging

## Customization

### Dependencies
- Remove unused libraries (e.g., Ktor, ViewModel) to reduce binary size
- Add new dependencies as needed (e.g., Koin for DI, Coil for image loading)

### Icons & Assets
- Replace the sample icons in the `icons` folder with your own
- Recommended tools:
  - [CloudConvert](https://cloudconvert.com/png-to-icns) for generating `.icns` (macOS) and `.ico` (Windows) files
  - [Figma](https://figma.com) for designing custom UI assets

## Troubleshooting

- If you encounter Gradle issues, try:
  ```bash
  ./gradlew --stop
  ./gradlew clean
  ```
- For build errors, ensure you have the correct JDK version set in your IDE
- Check the [GitHub Issues](https://github.com/yourusername/your-repo/issues) for known issues and solutions

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Acknowledgments

- Built with ❤️ using [Jetpack Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- UI components powered by [JetBrains Jewel](https://github.com/JetBrains/jewel)
- Icons by [Lucide](https://lucide.dev/)

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.
