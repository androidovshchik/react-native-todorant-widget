declare module "react-native-todorant-widget" {
    const toggle: (enable: boolean) => void;
    const forceUpdateAll: () => void;
    const getNewArgs: (callback: (args?: object) => void) => void;
}
