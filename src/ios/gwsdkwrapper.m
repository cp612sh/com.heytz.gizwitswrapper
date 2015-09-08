/********* gwsdkSetDeviceWifi.m Cordova Plugin Implementation *******/

#import <Cordova/CDV.h>
#import <XPGWifiSDK/XPGWifiSDK.h>

@interface gwsdkSetDeviceWifi: CDVPlugin<XPGWifiSDKDelegate> {
    // Member variables go here.
    NSString * _appId;
}

-(void)setDeviceWifi:(CDVInvokedUrlCommand *)command;

@property (strong,nonatomic) CDVInvokedUrlCommand * commandHolder;

@end

@implementation gwsdkSetDeviceWifi

@synthesize commandHolder;

-(void)pluginInitialize{
}

-(void)initSdkWithAppId:(CDVInvokedUrlCommand *) command{
    if(!_appId){
        _appId = command.arguments[2];
        [XPGWifiSDK startWithAppID:_appId];
    }
}

-(void) setDelegate{
    if(!([XPGWifiSDK sharedInstance].delegate)){
        [XPGWifiSDK sharedInstance].delegate = self;
    }
}

-(void)setDeviceWifi:(CDVInvokedUrlCommand *)command
{
    [self initSdkWithAppId:command];
    [self setDelegate];
    
    /**
     * @brief 配置路由的方法
     * @param ssid：需要配置到路由的SSID名
     * @param key：需要配置到路由的密码
     * @param mode：配置方式 SoftAPMode=软AP模式 AirLinkMode=一键配置模式
     * @param softAPSSIDPrefix：SoftAPMode模式下SoftAP的SSID前缀或全名（XPGWifiSDK以此判断手机当前是否连上了SoftAP，AirLinkMode该参数无意义，传nil即可）
     * @param timeout: 配置的超时时间 SDK默认执行的最小超时时间为30秒
     * @see 对应的回调接口：[XPGWifiSDK XPGWifiSDK:didSetDeviceWifi:result:]
     */
    self.commandHolder = command;
    [[XPGWifiSDK sharedInstance] setDeviceWifi:command.arguments[0]
                                           key:command.arguments[1]
                                          mode:XPGWifiSDKAirLinkMode
                              softAPSSIDPrefix:nil timeout:59];
}

/**
 * @brief 回调接口，返回设备配置的结果
 * @param device：已配置成功的设备
 * @param result：配置结果 成功 - 0 或 失败 - 1 如果配置失败，device为nil
 * @see 触发函数：[XPGWifiSDK setDeviceWifi:key:mode:]
 */
- (void)XPGWifiSDK:(XPGWifiSDK *)wifiSDK didSetDeviceWifi:(XPGWifiDevice *)device result:(int)result{
    NSString *uid = self.commandHolder.arguments[4];
    NSString *token = self.commandHolder.arguments[5];
    NSLog(@"======uid===%@", uid);
    NSLog(@"======token=%@", token);
    if (result == 0  && device.did.length > 0) {
        // successful
        NSLog(@"======did===%@", device.did);
        NSLog(@"======passCode===%@", device.passcode);
        NSDictionary *d = [NSDictionary dictionaryWithObjectsAndKeys:
                           device.did, @"did",
                           device.ipAddress, @"ipAddress",
                           device.macAddress, @"macAddress",
                           device.passcode, @"passcode",
                           device.productKey, @"productKey",
                           device.productName, @"productName",
                           device.remark, @"remark",
                           device.isConnected, @"isConnected",
                           device.isDisabled, @"isDisabled",
                           device.isLAN, @"isLAN",
                           device.isOnline, @"isOnline",
                           @"",@"error",
                           nil];
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:d];
        //[pluginResult setKeepCallbackAsBool:false];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:self.commandHolder.callbackId];
    }
}

- (void)dealloc
{
    NSLog(@"//====dealloc...====");
    [XPGWifiSDK sharedInstance].delegate = nil;
}


- (void)dispose{
    NSLog(@"//====disposed...====");
}
@end
