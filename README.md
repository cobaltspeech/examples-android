# Android Sample App

<img src="https://raw.githubusercontent.com/cobaltspeech/examples-android/master/screenshot/photo_2020-03-11_18-03-51.jpg?token=ACSISJ4ILFORQ6MOUNCU4K26QM64M" width="200">

This app demonstrates Cubic speech recognition experience using the device's built-in microphone.

### Terms of use

`The application requires access to the microphone!`

To change the settings for connecting to the Cubic service, click on the gear icon. In the settings you can specify the host and port of the connection, as well as the security of the connection.

<img src="https://raw.githubusercontent.com/cobaltspeech/examples-android/master/screenshot/photo_2020-03-11_18-03-48.jpg?token=ACSISJYFPP2LNO4L2KNG4QK6QM63K" width="200">

To select the recognition model (language, recording frequency), click on the drop-down list at the top of the screen.

<img src="https://raw.githubusercontent.com/cobaltspeech/examples-android/master/screenshot/photo_2020-03-11_18-03-54.jpg?token=ACSISJ4WW6LVUJ2CO4PEQO26QM752" width="200">

After selecting a model, the microphone button is activated. While holding the microphone button, the application will start transmitting sound recording to the Cubic server, after releasing the button, the speech text will be displayed on the screen.

<img src="https://raw.githubusercontent.com/cobaltspeech/examples-android/master/screenshot/photo_2020-03-11_18-03-44.jpg?token=ACSISJYD3BSZ2XLCPRQCXMK6QM6ZO" width="200">

You can clear the screen with the result by clicking on the button with the basket

### Dev documentation

1) If your project uses git, you can add dependency through the **submodule**. To do this, execute the git command while in the branch of your project

 `git submodule add https://github.com/cobaltspeech/sdk-cubic`
 
 If you do not want to create dependency of git modules, you need to fetch repository to the root of the project. 
 
 https://github.com/cobaltspeech/sdk-cubic
 
 After successful execution of the command, the **sdk-cubic** folder will appear in the root of your project, and the git dependency in the file, **.gitmodules** will be added.
 
2) For integrate CubicSDK with Android, you just need to add the **cubicsdk** module dependency to the **build.gradle**.

To complete the integration of CubicSDK with Android, you just need to add the **cubicsdk ** module dependency to **build.gradle**.

`implementation project(path: ':sdk-cubic:examples:android:cubicsdk')`

Do not forget to check for the correct path to the **sdk-cubic** module for the project in **settings.gradle** file.

`include ':app', ':sdk-cubic:examples:android:cubicsdk'`

After that, it remains to synchronize the gradle so that the CubicSDK can load the dependencies of the proto-buf models.

**CubicManager** - the main class described by the **ICubicManager** interface for working with the Cubic service. For convenience, it has two constructors that differ in one parameter, Lifecycle. This parameter allows you to automatically connect to the service if it is transferred or use manual connection if it is not specified.

**OnCubicChangeListener** - the interface of communication with **CubicManager**, through this interface the user receives information about the connection status, used ACP models, service errors and recognition results.
