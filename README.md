# React Native Todorant Widget

Android's Native key value storage system in React Native

## Installation

```bash
npm install react-native-todorant-widget --save
```

## Project setup and initialization


* In `android/settings.gradle`

```gradle
...
include ':react-native-shared-preferences', ':app'
project(':react-native-shared-preferences').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-shared-preferences/android')
```

* In `android/app/build.gradle`

```gradle
...
dependencies {
    /* YOUR DEPENDENCIES HERE */
    compile "com.facebook.react:react-native:+"
    compile project(":react-native-shared-preferences") // <--- add this
}

```

* Register Module (in MainApplication.java)

```java
import in.sriraman.sharedpreferences.RNSharedPreferencesReactPackage;  // <--- import

public class MainActivity extends ReactActivity {
  ......

  @Override
  protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
          new MainReactPackage(),
          new RNSharedPreferencesReactPackage() // <--- Add this
      );
  }

  ......

}
```


## Usage

#### Import

```javascript
var TodorantWidget = require('react-native-todorant-widget');
```

#### Configure name of preferences file. (Optional. Must be called before other functions.)

```javascript
TodorantWidget.toggle(true);
```

#### Set Item

```javascript
TodorantWidget.forceUpdateAll();
```

## Credits

[Vlad Kalyuzhnyu](https://github.com/androidovshchik)
