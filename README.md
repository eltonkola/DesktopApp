# Compose Desktop Template

A modern, cross-platform desktop application template built with Jetpack Compose Multiplatform.

This template was created to simplify the process of starting new desktop applications, eliminating the need for repetitive boilerplate code and setup.

## Screenshots
This is a simple demo app that mimics the ui/ux on an ai chat app, and uses apifreellm.com free api to showcase the interaction. It uses a mix of compose and jewels components, with icons from Lucide and jewels itself. It uses jewels theme, and supports dark and light mode. 
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

## Prerequisites

- JDK 17 or higher
- IntelliJ IDEA (recommended) or Android Studio
- Gradle 8.0 or higher

## Getting Started

1. Clone the repository
2. Open the project in IntelliJ IDEA or Android Studio
3. Run the application using the Gradle task: `run`

## Building

### Local Build

```bash
./gradlew package
```

### Create Distributables

```bash
./gradlew packageDmg        # macOS
./gradlew packageMsi        # Windows
./gradlew packageDeb        # Linux (Debian/Ubuntu)
```

## CI/CD

This project includes GitHub Actions workflow that automatically builds and packages the application for all major platforms:

- Windows (.msi)
- macOS (.dmg)
- Linux (.deb)

The workflow triggers on pushes to the `main` branch and on new releases.

## Dependencies

- Jetpack Compose Multiplatform
- JetBrains Jewel UI
- Ktor for networking
- Kotlinx Serialization
- SLF4J for logging

## Customization

### Dependencies
- Remove unused libraries (e.g., Ktor, ViewModel)
- Add new dependencies as needed (e.g., Koin, Coil)

### Icons
- Replace the sample icons in the `icons` folder with your own
- Use [CloudConvert](https://cloudconvert.com/png-to-icns) to generate `.icns` and `.ico` files

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.
