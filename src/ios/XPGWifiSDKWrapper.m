//#import "XPGWifiSDKWrapper.h"
#import <SystemConfiguration/CaptiveNetwork.h>

@implementation XPGWifiSDKWrapper

-(void)getConnectedSSID:(CDVInvokedUrlCommand*)command{


    NSString *ssid = nil;
        NSArray *ifs = (__bridge   id)CNCopySupportedInterfaces();
        NSLog(@"ifs:%@",ifs);
        for (NSString *ifnam in ifs) {
            NSDictionary *info = (__bridge id)CNCopyCurrentNetworkInfo((__bridge CFStringRef)ifnam);
            NSLog(@"dici：%@",[info  allKeys]);
            if (info[@"SSID"]) {
                ssid = info[@"SSID"];
            }
        }
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:ssid];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end