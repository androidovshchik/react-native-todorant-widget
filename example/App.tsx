/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * Generated with the TypeScript template
 * https://github.com/react-native-community/react-native-template-typescript
 *
 * @format
 */

import React from 'react';
import {Alert, Button, SafeAreaView, ScrollView, StatusBar, StyleSheet, Text, TextInput, View,} from 'react-native';

import {
    Colors,
    DebugInstructions,
    Header,
    LearnMoreLinks,
    ReloadInstructions,
} from 'react-native/Libraries/NewAppScreen';

const App = () => {
    return (
        <View style={styles.container}>
            <TextInput
                style={{height: 40, borderColor: 'gray', borderWidth: 1, margin: 16}}
                placeholder="Token"/>
            <View style={styles.button}>
                <Button
                    title="Set token"
                    onPress={() => Alert.alert('Simple Button pressed')}/>
            </View>
            <View style={styles.button}>
                <Button
                    title="Toggle"
                    onPress={() => Alert.alert('Simple Button pressed')}/>
            </View>
            <View style={styles.button}>
                <Button
                    title="Force update"
                    onPress={() => Alert.alert('Simple Button pressed')}/>
            </View>
        </View>
    );
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
