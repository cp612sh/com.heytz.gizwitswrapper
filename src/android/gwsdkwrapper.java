package com.heytz.gwsdkwrapper;

import android.content.Context;
import android.util.Log;
import com.xtremeprog.xpgconnect.XPGWifiDevice;
import com.xtremeprog.xpgconnect.XPGWifiErrorCode;
import com.xtremeprog.xpgconnect.XPGWifiSDK;
import com.xtremeprog.xpgconnect.XPGWifiSDK.XPGWifiConfigureMode;
import com.xtremeprog.xpgconnect.XPGWifiSDKListener;
import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * This class wrapping Gizwits WifiSDK called from JavaScript.
 */
public class gwsdkwrapper extends CordovaPlugin {

    private CallbackContext airLinkCallbackContext;
    private Context context;
    private String _appId;
    private XPGWifiSDK _shareInstance;
    private String _currentDeviceMac;

    private XPGWifiSDKListener wifiSDKListener = new XPGWifiSDKListener() {
        @Override
        public void didDiscovered(int result, List<XPGWifiDevice> devicesList) {
            if (result == XPGWifiErrorCode.XPGWifiError_NONE && devicesList.size() > 0) {

                for (int i = 0; i < devicesList.size(); i++) {
                    if((_currentDeviceMac!=null)&&(devicesList.get(i).getMacAddress().indexOf(_currentDeviceMac)>-1)) {
                        _currentDeviceMac=null;
                        JSONObject json = new JSONObject();
                        try {
                            json.put("productKey", devicesList.get(i).getProductKey());
                            json.put("did", devicesList.get(i).getDid());
                            json.put("macAddress", devicesList.get(i).getMacAddress());
                            json.put("passcode", devicesList.get(i).getPasscode());
                        }catch (JSONException e) {
                            //e.printStackTrace();
                            Log.e("====parseJSON====", e.getMessage());
                            PluginResult pr = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
                            airLinkCallbackContext.sendPluginResult(pr);
                        }
                        PluginResult pr = new PluginResult(PluginResult.Status.OK, json);
                        // pr.setKeepCallback(true);
                        airLinkCallbackContext.sendPluginResult(pr);
                    }
                }

            }
        }
        @Override
        public void didSetDeviceWifi(int error, XPGWifiDevice device) {
            dealloc();
            if (error == 0 && device.getMacAddress().length() > 0) {
                Log.e("====didSetDeviceWifi callback===getMacAddress:=", device.getMacAddress());
                Log.e("====didSetDeviceWifi callback===getIPAddress:=", device.getIPAddress());
                _currentDeviceMac=device.getMacAddress();
            }
            // do nothing...
            else if (error == XPGWifiErrorCode.XPGWifiError_CONNECT_TIMEOUT) {
                //dealloc();
                PluginResult pr = new PluginResult(PluginResult.Status.ERROR, error);
                airLinkCallbackContext.sendPluginResult(pr);
            } else {
                //dealloc();
            }
        }
    };


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        // your init code here
        context = cordova.getActivity().getApplicationContext();
    }


    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("setDeviceWifi")) {
            if (_appId == null || (_appId.compareToIgnoreCase(args.getString(2)) != 0)) {
                _appId = args.getString(2);
                XPGWifiSDK.sharedInstance().startWithAppID(context, _appId);
                //_shareInstance = XPGWifiSDK.sharedInstance();
            }
            this.setDeviceWifi(args.getString(0), args.getString(1), callbackContext);
            return true;
        }
        if (action.equals("dealloc")) {
            this.dealloc();
            return true;
        }
        return false;
    }

    private void setDeviceWifi(String wifiSSID, String wifiKey, CallbackContext callbackContext) {
        if (null == _shareInstance) {
            _shareInstance = XPGWifiSDK.sharedInstance();
            // set listener
            _shareInstance.setListener(wifiSDKListener);

            if (wifiSSID != null && wifiSSID.length() > 0 && wifiKey != null && wifiKey.length() > 0) {
                airLinkCallbackContext = callbackContext;

                // 这里如果使用ArrayList<E>会导致JAVA编译版本不兼容
//            ArrayList<XPGWifiSDK.XPGWifiGAgentType> atList = new ArrayList<>();
//            atList.add(XPGWifiSDK.XPGWifiGAgentType.XPGWifiGAgentTypeHF);
//                _shareInstance.setDeviceWifi(wifiSSID, wifiKey, XPGWifiConfigureMode.XPGWifiConfigureModeAirLink, null,
//                        18000);
                //15.11.24 切换成新接口
                _shareInstance.setDeviceWifi(wifiSSID,wifiKey,XPGWifiConfigureMode.XPGWifiConfigureModeAirLink,null,18000,null);
            } else {
                callbackContext.error("args is empty or null");
            }
        }
    }

    private void dealloc() {
        _shareInstance = null;
        _currentDeviceMac=null;
    }
}
