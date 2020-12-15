# Diatheke example application

Diatheke is the Conversation engine.
This android application provides a bare-bones example of how integrate the Cobalt's Diatheke SDK into an android application using only text.

## Setup

The client SDK package is published by Cobalt Speech via GithHub Packages.
In order to download the SDK, you need to configure gradle to point to github's package repo.
Documentation from GitHub can be found [here](https://docs.github.com/en/free-pro-team@latest/packages/using-github-packages-with-your-projects-ecosystem/configuring-gradle-for-use-with-github-packages#authenticating-to-github-packages).

You need to follow the "Authenticating to GitHubPackages section", and then the "Installing a package" section.  A quick summary of those sections is provided here:

### Authenticating

GitHub needs you to authenticate with them when pulling down a public package.  This can be done by adding the following block to your root `build.gradle` file.
```groovy
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/cobaltspeech/sdk-diatheke")
        credentials {
                username = GITHUB_USER  // Your github username
                password = GITHUB_TOKEN // Personal Access Token created from https://github.com/settings/tokens
        }
    }
}
```

**Note:** These credentials shouldn’t be added to your git repository!
Instead, you should define `GITHUB_USER` and `GITHUB_TOKEN` in an untracked properties file or
pass them directly using the `gradlew -P` parameter.

### Installing

Now, you can install the sdk via the regular dependencies setup in your application's build.gradle.

```groovy
dependencies {
    implementation 'com.cobaltspeech.diatheke:sdk-diatheke:2+' // Pick the appropriate version here.
    // SDK-Diatheke requires a few other dependencies
    implementation 'com.google.protobuf:protobuf-javalite:3.13.0'
    implementation 'com.squareup.okhttp:okhttp:2.7.5'
    implementation 'io.grpc:grpc-core:1.33.0'
    implementation 'io.grpc:grpc-stub:1.33.0'
    implementation 'io.grpc:grpc-okhttp:1.33.0'
}
```

