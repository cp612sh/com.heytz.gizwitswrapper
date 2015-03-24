//#import <XPGWifiSDK/XPGWifiSDK.h>
#import <Cordova/CDVPlugin.h>

@interface XPGWifiSDKWrapper : CDVPlugin <XPGWifiDeviceDelegate,XPGWifiSDKDelegate>{
//    @private BOOL __locationStarted;
//    @private BOOL __highAccuracyEnabled;
//    CDVLocationData* locationData;
}

-(void)getConnectedSSID:(CDVInvokedUrlCommand*)command;