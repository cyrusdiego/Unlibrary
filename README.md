# Unlibrary
![CI badge](https://github.com/CMPUT301F20T24/Unlibrary/workflows/Android/badge.svg)

Unlibrary is an app that allows people to borrow books amongst their community instead of using the library.

* [Installation](#installation)
* [Testing](#testing)
* [Documentation](#documentation)
* [Troubleshooting](#troubleshooting)
* [Maintainers](#maintainers)

## Installation
Clone this repository and import it into Android Studio.

```
git clone https://github.com/CMPUT301F20T24/Unlibrary.git
```
## Compatible Device
- Google Pixel XL

## Testing

### Prerequisites
- [NodeJS](https://nodejs.org/en/)

### Set-up
```bash
cd firestore-emulator
```

```bash
npm install  # Install firebase CLI
```

### Running unit tests

> :warning: **Why are some tests commented out?** We implemented Hilt/DI quite late in the project and missed some refactorings and in order to test some of the modules, we needed to refactor production code. Given the amount of time we have left, we decided not to finish some of the unit tests.

#### From CLI
```bash
cd firestore-emulator
npm run test
```

#### From Android Studio
1. In a shell, run the following commands to start the emulator
   
    ```bash
    cd firestore-emulator
    npm run start-emulator
    ```

2. Open the Unlibrary project in Android Studio
3. Right click on the `test` directory (not `androidTest`) in *Project View*, and click `Run`

### Running UI/instrumented tests
With the project open in Android Studio, right click on `androidTest` directory in *Project View* and click `Run`

> :warning: **UI tests may fail when run on physical device** read [Troubleshooting](#troubleshooting) below

## Documentation
- Wiki page [here](https://github.com/CMPUT301F20T24/Unlibrary/wiki)
- Generated Javadoc can be found in ./doc/javadoc or [Javadoc - Overview](https://cmput301f20t24.github.io/Unlibrary/)

## Troubleshooting

- Failing UI tests
  
  Error message along the lines of: Unable to find view in hierarchy
  
  Fix: Use the emulator to run UI tests instead of a physical device, and disable animations from Developer options inside the emulator. We found that UI indices can change when running on physical device. Since our UI checks are done in Firebase Test Labs which uses the emulator, we fixed our tests to that environment.

## Maintainers
- Armianto Sumitro
- Cyrus Diego
- Taranjot Singh
- Golnoush Hassanzadeh
- Daniel Rojas-Cardona
- Caleb Schoepp
