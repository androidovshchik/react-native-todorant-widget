/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * Generated with the TypeScript template
 * https://github.com/react-native-community/react-native-template-typescript
 *
 * @format
 */

import React, {Component} from 'react';
import {AppState, AppStateStatus, BackHandler, Button, StyleSheet, TextInput, ToastAndroid, View,} from 'react-native';
import SharedPreferences from 'react-native-shared-preferences';
import TodorantWidget from 'react-native-todorant-widget';

class App extends Component {

    state = {
        token: '',
        password: '',
        appState: AppState.currentState
    }

    constructor(props: any) {
        super(props)
        SharedPreferences.getItem("token", value => {
            this.setState({token: value})
        })
        SharedPreferences.getItem("password", value => {
            this.setState({password: value})
        })
    }

    componentDidMount() {
        AppState.addEventListener("change", this._handleAppStateChange);
    }

    componentWillUnmount() {
        AppState.removeEventListener("change", this._handleAppStateChange);
    }

    private _handleAppStateChange = (nextAppState: AppStateStatus) => {
        console.log(`nextAppState=${nextAppState}`);
        this.setState({appState: nextAppState});
        TodorantWidget.getNewArgs((args?: object) => {
            console.log(`args=${JSON.stringify(args)}`);
        })
    };

    render() {
        // see https://github.com/androidovshchik/react-native-todorant-widget/blob/master/example/android/app/src/main/java/com/todorant/example/MainActivity.kt
        ToastAndroid.show(JSON.stringify(this.props, null, 2), ToastAndroid.LONG);
        return (
            <View style={styles.container}>
                <TextInput
                    style={{height: 40, borderColor: 'gray', borderWidth: 1, margin: 16}}
                    placeholder="Type token"
                    onChangeText={value => this.setState({token: value})}
                    value={this.state.token}/>
                <TextInput
                    style={{height: 40, borderColor: 'gray', borderWidth: 1, margin: 16, marginTop: 0}}
                    placeholder="Type password"
                    onChangeText={value => this.setState({password: value})}
                    value={this.state.password}/>
                <View style={styles.button}>
                    <Button
                        title="Set token"
                        onPress={() => {
                            SharedPreferences.setItem('token', this.state.token)
                            TodorantWidget.forceUpdateAll()
                            BackHandler.exitApp()
                        }}/>
                </View>
                <View style={styles.button}>
                    <Button
                        title="Set password"
                        onPress={() => {
                            SharedPreferences.setItem('password', this.state.password)
                            TodorantWidget.forceUpdateAll()
                            BackHandler.exitApp()
                        }}/>
                </View>
                <View style={styles.button}>
                    <Button
                        title="Force update"
                        onPress={() => {
                            TodorantWidget.forceUpdateAll()
                            BackHandler.exitApp()
                        }}/>
                </View>
                <View style={styles.button}>
                    <Button
                        title="Get new args"
                        onPress={() => {
                            TodorantWidget.getNewArgs((args?: object) => {
                                ToastAndroid.show(JSON.stringify(args, null, 2), ToastAndroid.LONG);
                            })
                        }}/>
                </View>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        display: "flex",
        flexDirection: "column"
    },
    button: {
        marginStart: 16,
        marginEnd: 16,
        marginBottom: 16
    },
});

export default App;
