# Ktor Single Page Application Feature [![Build Status](https://travis-ci.org/lamba92/ktor-spa.svg?branch=master)](https://travis-ci.org/lamba92/ktor-spa) [![](https://jitpack.io/v/lamba92/ktor-spa.svg)](https://jitpack.io/#lamba92/ktor-spa)

Installable feature to handle SPAs easily in Ktor!

Written in Kotlin with ❤️

## Usage

Just install the feature in your application with:

```kotlin
install(SinglePageApplication)
```

By default the app is served from the root folder of bundled resources with `index.html` as main page. You can customize stuff like so:

```kotlin
install(SinglePageApplication){

    // main page file name to be served
    defaultPage = "myPage.html"
    
    // folder in which look for you spa files, either
    // inside bundled resources or current working directory
    folderPath = "not/root/folder/"
    
    // The url at which tour spa should be served. This
    // is usefull if you want to serve a spa not at the
    // root of your website
    spaRoute = "/something"
    
    // uses files in the current working directory instead
    // of resources
    useFiles = true
    
    // ignores a url if contains this regex 
    ignoreIfContains = Regex(...)
    
}
```

**All the routes you set up in your Ktor application have higher priority and will shadow eventual SPA routes so keep that in mind.** 

## Under the hood

The feature intercepts all 404s not intercepted by the router and instead of responding an HTTP 404 it serves the `index.html` (or whatever you called it) with HTTP 200 status.

**NB**: Remember to setup a 404 in your spa!

## Install [![](https://jitpack.io/v/lamba92/ktor-spa.svg)](https://jitpack.io/#lamba92/ktor-spa)

Add the [JitPack.io](http://jitpack.io) repository to the project `build.grade`:
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

Then import the latest version in the `build.gradle` of the modules you need:

```groovy
dependencies {
    implementation 'com.github.lamba92:ktor-spa:{latest_version}'
}
```

If using Gradle Kotlin DSL:
```kotlin
repositories {
    maven(url = "https://jitpack.io")
}
...
dependencies {
    implementation("com.github.lamba92", "ktor-spa", "{latest_version}")
}
```
For Maven:
```xml
<repositories>
   <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
   </repository>
</repositories>
...
<dependency> 	 
   <groupId>com.github.Lamba92</groupId>
   <artifactId>ktor-spa</artifactId>
   <version>{latest_version}</version>
</dependency>
```
