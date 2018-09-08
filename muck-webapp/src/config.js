// @flow
/**
 * Get the configuration setting with the given name
 * @param name the name of the configuration setting
 * @return the configuration setting
 */
export default function getConfig(name: string): any {
    return process.env[`REACT_APP_${name}`] || window.ConfigurationOptions[name];
}
