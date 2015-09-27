package com.heytz.gwsdkwrapper;

import android.content.Context;
import com.xtremeprog.xpgconnect.XPGWifiDevice;
import com.xtremeprog.xpgconnect.XPGWifiSDK;
import com.xtremeprog.xpgconnect.XPGWifiSDK.XPGWifiConfigureMode;
import com.xtremeprog.xpgconnect.XPGWifiSDKListener;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * This class wrapping Gizwits WifiSDK called from JavaScript.
 */
public class gwsdkwrapper extends CordovaPlugin {

    private CallbackContext airLinkCallbackContext;
    private Context context;

    private XPGWifiSDKListener wifiSDKListener = new XPGWifiSDKListener() {
        @Override
        public void didSetDeviceWifi(int error, XPGWifiDevice device) {
            JSONObject json = new JSONObject();
            if (error == 0) {

                try {
                    json.put("did", device.getDid());
                    json.put("ipAddress", device.getIPAddress());
                    json.put("macAddress", device.getMacAddress());
                    json.put("passcode", device.getPasscode());
                    json.put("productKey", device.getProductKey());
                    json.put("productName", device.getProductName());
                    json.put("remark", device.getRemark());
                    //json.put("ui", device.getUI());
                    json.put("isConnected", device.isConnected());
                    json.put("isDisabled", device.isDisabled());
                    json.put("isLAN", device.isLAN());
                    json.put("isOnline", device.isOnline());
                    json.put("error", "");
                } catch (JSONException e) {
                    //e.printStackTrace();
                }

                if (device.getDid().length() == 22 && device.getProductKey().length() == 32) {
                    airLinkCallbackContext.success(json);
                }
            } else {
                if (device.getProductKey().length() == 32) {
                    try {
                        json.put("did", device.getDid());
                        json.put("ipAddress", device.getIPAddress());
                        json.put("macAddress", device.getMacAddress());
                        json.put("passcode", device.getPasscode());
                        json.put("productKey", device.getProductKey());
                        json.put("productName", device.getProductName());
                        json.put("remark", device.getRemark());
                        //json.put("ui", device.getUI());
                        json.put("isConnected", device.isConnected());
                        json.put("isDisabled", device.isDisabled());
                        json.put("isLAN", device.isLAN());
                        json.put("isOnline", device.isOnline());
                        json.put("error", "");
                    } catch (JSONException e) {
                        //e.printStackTrace();
                    }
                    airLinkCallbackContext.error(json);
                } else {
                    try {
                        json.put("error", "timeout");
                    } catch (JSONException e) {
                        //e.printStackTrace();
                    }
                    airLinkCallbackContext.error(json);
                }
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
            this.setDeviceWifi(args.getString(0), args.getString(1), args.getString(2), callbackContext);
            return true;
        }
        if (action.equals("dealloc")){
            this.dealloc();
            return true;
        }
        return false;
    }
    private void setDeviceWifi(String wifiSSID, String wifiKey, String appId, CallbackContext callbackContext) {

        if(null == XPGWifiSDK.sharedInstance())
            XPGWifiSDK.sharedInstance().startWithAppID(context, appId);

        // set listener
        XPGWifiSDK.sharedInstance().setListener(wifiSDKListener);

        // start sdk log;
        //XPGWifiSDK.sharedInstance().setLogLevel(XPGWifiSDK.XPGWifiLogLevel.XPGWifiLogLevelAll, "error.txt", true);

        if (wifiSSID != null && wifiSSID.length() > 0 && wifiKey != null && wifiKey.length() > 0) {
            airLinkCallbackContext = callbackContext;

            ArrayList<XPGWifiSDK.XPGWifiGAgentType> atList = new ArrayList<>();
            atList.add(XPGWifiSDK.XPGWifiGAgentType.XPGWifiGAgentTypeHF);
            XPGWifiSDK.sharedInstance()
                    .setDeviceWifi(wifiSSID, wifiKey, XPGWifiConfigureMode.XPGWifiConfigureModeAirLink, null,
                            45000, atList);
        } else {
            callbackContext.error("args is empty or null");
        }
    }

    private void dealloc(){
        XPGWifiSDK.sharedInstance().setListener(null);
    }
}
