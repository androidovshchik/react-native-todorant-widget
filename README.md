## React Native Todorant Widget
> Todorant widget on Android

### Install

```bash
npm install react-native-todorant-widget --save
```

### Setup

* In `android/settings.gradle`

```gradle
//...
include ':react-native-todorant-widget'
```

* In `android/app/build.gradle`

```gradle
dependencies {
    //...
    implementation project(":react-native-todorant-widget")
    //...
}
```

### Usage

Import

```typescript
import TodorantWidget from 'react-native-todorant-widget';
```

Enable/disable widgets for user at all

```javascript
TodorantWidget.toggle(true);
```

Force update all widgets on user's home screen

```javascript
TodorantWidget.forceUpdateAll();
```

### Credits

Author [Vlad Kalyuzhnyu](https://github.com/androidovshchik)
