
var exec = require(cordova/exec);

var XPGWifiSDKWrapper = function () {};

XPGWifiSDKWrapper.getCurrentSSID = function (win, fail) {
        if (typeof win != "function") {
            console.log("getCurrentSSID first parameter must be a function to handle SSID.");
            return;
        }
        exec(win, fail, 'XPGWifiSDKWrapper', 'getConnectedSSID', []);
    }


module.exports = XPGWifiSDKWrapper;
