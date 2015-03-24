
var XPGWifiSDKWrapper = {};

XPGWifiSDKWrapper.getCurrentSSID = function (win, fail) {
        if (typeof win != "function") {
            console.log("getCurrentSSID first parameter must be a function to handle SSID.");
            return;
        }
        cordova.exec(win, fail, 'XPGWifiSDKWrapper', 'getConnectedSSID', []);
    }


module.exports = XPGWifiSDKWrapper;
