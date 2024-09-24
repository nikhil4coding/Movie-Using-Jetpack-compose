# Movie with Jetpack Compose using MVVM

## What is in the App
The app displays Top rated movies using api from  `https://api.themoviedb.org/3/`.
When the app is initiated, it displays a list of Top Rated movies.
When clicked on Any movie in the list it will navigate to its details on a different view. 
It can also navigate back to the Movie list by pressing the back arrow icon on the Top App bar

## How to build the code
Please Note: The app will not work if the access token is expired. Please change the access token under the `ApiModule` class.

The app can be built and run by importing the project on Android Studio
Or
To *build* the app run the following commands on terminal

On Windows: `gradlew assembleDebug`
On Mac or Linux: `./gradlew assembleDebug`

## How to run the output
To *run* the app on an Android device, please use the following command from terminal

On Windows: `gradlew installDebug`
On Mac or Linux: `./gradlew installDebug`

## How to run tests
To *run* the tests, please use the following command from Terminal

On Windows: `gradlew testDebugUnitTest`
On Mac or Linux: `./gradlew testDebugUnitTest`

## Assumptions
While writing the code, the following assumptions are made
- Device will always be connected to the internet
- App will run on Portrait mode only
- TMDB API service is always available

## Not covered
- Error handling like no network, other specific error
- change in screen orintation
- Not all the business logic is unit tested. Only a few methods are covered under the Unit test to demo.
- Not all the usecase is unit tested. Only one Usecase is unit tested to demo.
- ViewModel is not unit tested

## Extra
This code is to demo Clean Architecture using Jetpack compose, Dagger, Retrofit, coroutines, MVVM pattern and Kotlin in an Android App.
There might be scope for improvement in this piece of code. Any suggestions are welcome.
