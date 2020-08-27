## React Native Todorant Widget
> Todorant widget on Android

### Installation

```bash
npm install react-native-todorant-widget --save
```

### Project setup and initialization

* In `android/settings.gradle`

```gradle
...
include ':react-native-todorant-widget', ':app'
project(':react-native-todorant-widget').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-shared-preferences/android')
```

* In `android/app/build.gradle`

```gradle
dependencies {
    ...
    implementation project(":react-native-todorant-widget")
    ...
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


### Usage

Enable/disable widgets for user at all

```javascript
TodorantWidget.toggle(true);
```

Force update all widgets on user's home screen

```javascript
TodorantWidget.forceUpdateAll();
```

### Credits

[Vlad Kalyuzhnyu](https://github.com/androidovshchik)
