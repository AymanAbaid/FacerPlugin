# FACER-AS

FACER-AS is an Android Studio plugin designed to provide an interface for [FACER](https://github.com/shamsa-abid/FACER_Artifacts), which is a an API usage-based code-recommender for Java for Android.

## Installation 

### Pre-requisites

1. FACER (https://github.com/shamsa-abid/FACER_Artifacts/)
2. Android Studio 4.1.2 (https://developer.android.com/studio/archive)
3. IntelliJ IDEA CE 2020.1.4 (https://www.jetbrains.com/idea/download/other.html)

### Project Setup

Clone the repository and import into IntelliJ IDEA CE as an existing project. To setup the correct IDE path for  running the project on Android Studio, go to the build.gradle file in the root folder of the project and set the ideDirectory variable for MacOS as:

``` java
runIde{
    ideDirectory 'C:/Program Files/Android/Android Studio/'
}
```
For Windows, give the path to the Android Studio installation folder e.g.:

``` java
runIde{

    ideDirectory '/Applications/Android Studio.app/Contents/'
}
```

## Executing the Project

### With Test Instance of IDE

To test the project in a test instance of Android Studio using the "runIde" Gradle task, enable Gradle Tool Window in IntelliJ IDEA CE. Go to **View > Tool Windows > Gradle** option if it is not already visible in your IntelliJ IDEA, and run the task under **<Project> > Tasks > intellij > runIde**.\
**Note:** This is a separate test instance ad your regular Android Studio will not have the plugin installed in this manner.

### With Installed Instance of IDE

To test the project in the system installed instance of Android Studio, run the "buildPlugin" Gradle task at **<Project> > Tasks > intellij > buildPlugin** in the Gradle Tool Window. The task assembles plugin and prepares zip archive for deployment. The archive should be available in **/build/distributions/** folder of the project.\
In order to install this plugin snapshot in Android Studio, launch Android Studio and go to **File > Settings > Plugins > Settings Icon > Install Plugin from Disk...**, and choose the snapshot zip file. Restart Android Studio to show the FACER option in top Menu bar.

## Plugin Configuration
On first launch of FACER-AS, the Configuration Setup dialog will appears (or use FACER on menu bar and select Configuration option). Add the URL of database created during FACER setup, the path to Lucene Index folder, the stopwords.txt file path, and a destination path for the User Interaction logs. After the setup is complete, the user can click Ok option, which updates the plugin configuration settings and the plugin is enabled.
    ![ui config](https://user-images.githubusercontent.com/29062159/119324436-421ac180-bc99-11eb-8576-fdfe916814bc.png)

## Usage

You can select any text in the editor and select FACER > Enable FACER option or use Ctrl + 1 to engage the plugin. This will launch a configuration window to setup the database URL, Lucene Index folder path (see [FACER](https://github.com/shamsa-abid/FACER_Artifacts) for details), a stopwords.txt file path containing stopwords for dataset, and the destination path for the user interaction logs file. After you complete the configuration step, a popup will appear with the option **Get FACER recommendations* will appear. Select that option to view recommendation results and explore the plugin further.
