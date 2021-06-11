


# FACER-AS

FACER-AS is an Android Studio plugin designed to provide an interface for [FACER](https://github.com/shamsa-abid/FACER_Artifacts), which is a an API usage-based code-recommender for Java. Please follow the steps below for detailed setup guidelines.
In order to do a quick setup and install FACER-AS plugin in your Android Studio, please refer to [FACER-AS Quick Setup](#facer-as-quick-setup). To setup the IntelliJ IDEA project of FACER-AS, please review [FACER-AS IntelliJ IDEA Project Setup](#facer-as-intellij-idea-project-setup).

## FACER-AS Quick Setup 

### Pre-requisites
To plug and play FACER-AS, download and install the following dependencies: 

1. Android Studio 4.1.2 (https://developer.android.com/studio/archive)

### Resources
In order to do a quick setup, download the following:

1. FACER-AS Plugin Package (https://bit.ly/2RNzejs)
2. FACER-AS Resources (https://bit.ly/3zltzSE)

Extract the contents of the FACER-AS Resources RAR file to a folder in your system. The path to this folder will be configured in FACER-AS (see [Plugin Configuration](#plugin-configuration)) for input and output resources location.

### Installation

In order to install this plugin snapshot in Android Studio, launch Android Studio and go to **File > Settings > Plugins > Settings Icon > Install Plugin from Disk...**, and choose the FACER-AS Plugin Package zip file downloaded earlier. Restart Android Studio to show the FACER option in top Menu bar. See [Usage](#usage) for functional details.

## FACER-AS IntelliJ IDEA Project Setup 

### Pre-requisites
To setup FACER-AS IntelliJ IDEA project, download and install the following dependencies: 

1. Android Studio 4.1.2 (https://developer.android.com/studio/archive)
2. IntelliJ IDEA CE 2020.1.4 ([https://www.jetbrains.com/idea/download/other.html](https://www.jetbrains.com/idea/download/other.html))

### Resources
In order to execute the project, download the following:

1. FACER-AS Resources (https://bit.ly/3zltzSE)

Extract FACER-AS Resources to a folder in your system. The path to this folder will be configured in FACER-AS (see [Plugin Configuration](#plugin-configuration)) for input and output resources location.

### Project Setup

Clone the repository and import into IntelliJ IDEA CE as an existing project. To setup the correct IDE path for  running the project on Android Studio, go to the build.gradle file in the root folder of the project and set the ideDirectory variable for MacOS as:

``` java
runIde {
    ideDirectory 'C:/Program Files/Android/Android Studio/'
}
```
For Windows, give the path to the Android Studio installation folder e.g.:

``` java
runIde {
    ideDirectory '/Applications/Android Studio.app/Contents/'
}
```

### Executing the Project

#### *With Test Instance of IDE*

To test the project in a test instance of Android Studio using the "runIde" Gradle task, enable Gradle Tool Window in IntelliJ IDEA CE. Go to **View > Tool Windows > Gradle** option if it is not already visible in your IntelliJ IDEA, and run the task under **<Project> > Tasks > intellij > runIde**. See [Usage](#usage) for functional details.
**Note:** This is a separate test instance and your regular Android Studio will not have the plugin installed in this manner. 

#### *With Installed Instance of IDE*

To test the project in the system installed instance of Android Studio, run the "buildPlugin" Gradle task at **<*Project*> > Tasks > intellij > buildPlugin** in the Gradle Tool Window. The task assembles plugin and prepares zip archive for deployment. The archive should be available in **/build/distributions/** folder of the project.\
In order to install this plugin snapshot in Android Studio, launch Android Studio and go to **File > Settings > Plugins > Settings Icon > Install Plugin from Disk...**, and choose the FACER-AS Plugin Package zip file. Restart Android Studio to show the FACER option in top Menu bar. See [Usage](#usage) for functional details.

## Plugin Configuration
On first launch of FACER-AS, the Configuration Setup dialog will appears (or use FACER on menu bar and select Configuration option). Add the URL of database created during FACER setup, the path to Lucene Index folder, the stopwords.txt file path, and a destination path for the User Interaction logs. After the setup is complete, click Ok option to update the plugin configuration settings.    

![ui_config_2021_11_06](https://user-images.githubusercontent.com/80214279/121684090-b34ed700-cad7-11eb-8da5-bc48652259af.png)
    
## Usage

You can select any text in the editor and select FACER > Enable FACER option or use Ctrl + 1 to engage the plugin. This will launch a configuration window to setup the database URL, Lucene Index folder path (see [FACER](https://github.com/shamsa-abid/FACER_Artifacts) for details), a stopwords.txt file path containing stopwords for dataset, and the destination path for the user interaction logs file. After you complete the configuration step, a popup will appear with the option **Get FACER recommendations* will appear. Select the option to view recommendation results and explore the plugin further.
![ui_stage_1_2021_11_06](https://user-images.githubusercontent.com/80214279/121686173-46890c00-cada-11eb-9c31-d225bc5091e4.png)
    
<!--   youtube link, drive link, add usage for stage 2, code file, called methods, usage   -->!
