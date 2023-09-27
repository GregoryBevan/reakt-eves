# ReaKt.EveS CLI
This CLI will help you in the use of ReaKt.EveS library.

## Plugin configuration
Add the following plugin to your Gradle build file :
```kotlin
plugins {
   id("me.elgregos.reakteves.cli") version "0.0.1" 
}
```

## Task initDomain 
You should first determine the business domain of your application.

In order to help you generate artifacts (classes, interfaces) for this domain, you can use the **initDomain** task :
```
./gradlew initDomain
or
gradle initDomain
```