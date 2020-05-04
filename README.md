# DSS Tools

Digital Signature Service tools.

## Functionality
Provides miscellaneous transformation and validation tools within the domain
of digital signatures.

#### Message Digest
* Generate multiple message digests from source file at once.
* XML files can be processed by chosen canonicalization method.
* Transformed XML file can be saved as new file.  

## Requirements
* Java 11+
* JavaFX 11+ SDK

## How to build this project
This quick setup guide is intended for Java 11 and InteliJ IDEA running on Win10.
You should have Java 11 and InteliJ IDEA already installed. 

#### Download the JavaFX SDK﻿
1. Download the JavaFX 11 from https://gluonhq.com/products/javafx/
2. Unzip the file to the desired directory.
3. Add an environment variable pointing to the lib directory of the runtime:
    ```
    set PATH_TO_FX="path\to\javafx-sdk-14\lib"
    ```

#### Add the JavaFX library﻿
1. Open this project in InteliJ IDEA. 
2. From the main menu, select: **File** > **Project Structure...**
3. Open the **Global Libraries** section.
4. Click the "**+**" sign (_New Global Library_), and select Java.
5. Specify the path to the **lib** folder in the unzipped JavaFX SDK package. 

#### Add VM options
1. From the main menu select: **Run** > **Edit Configurations...**
2. In the **VM options** field for this application, specify:
    ```
    --module-path ${PATH_TO_FX} --add-modules javafx.controls,javafx.fxml
    ```
#### Run the application﻿
1. From the main menu, select: **Run** > **Run 'ValidatorApplication'** or press **Shift+F10**.