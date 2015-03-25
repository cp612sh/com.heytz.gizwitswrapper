//#import <XPGWifiSDK/XPGWifiSDK.h>
#import <Cordova/CDVPlugin.h>

@interface XPGWifiSDKWrapper : CDVPlugin

-(void)getConnectedSSID:(CDVInvokedUrlCommand*)command;

@end