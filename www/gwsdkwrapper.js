var exec = require('cordova/exec');

exports.setDeviceWifi = function (ssid, wifikey, appId, success, error) {
    exec(success, error, "gwsdkwrapper", "setDeviceWifi", [ssid, wifikey, appId]);
};

exports.deAlloc = function(success, error){
    exec(success, error, "gwsdkwrapper", "dealloc",[]);
};
