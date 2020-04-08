package com.android.jesse.log.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.android.jesse.log.CommonInfoWriter;


/**
 * @author dongyk on 2019/2/22
 */
public class NetworkManager {


    public static final String OFFLINE = "OFFLINE";
    public static final String WIFI = "WIFI";
    public static final String SECOND_GENERATION = "2G";
    public static final String THIRD_GENERATION = "3G";
    public static final String FOUR_GENERATION = "4G";
    public static final String UNKNOWN = "UNKNOWN";

    private NetworkManager(){

    }

    private static class NetworkManagerHolder{
        private static final NetworkManager instance = new NetworkManager();
    }

    public static NetworkManager getInstance(){
        return NetworkManagerHolder.instance;
    }

    public void registerNetworChangeListener(Context context) {
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                CommonInfoWriter.getInstance().writeCommonInfo();
            }
        }, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public String getNetworkType(Context context) {
        String strNetworkType = OFFLINE;
        if (context == null) return strNetworkType;
        ConnectivityManager contextService = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);// 获取网络服务
        if (contextService == null) return strNetworkType;
        NetworkInfo networkInfo = contextService.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = WIFI;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();

                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = SECOND_GENERATION;
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = THIRD_GENERATION;
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = FOUR_GENERATION;
                        break;
                    default:
                        if (TextUtils.isEmpty(_strSubTypeName)){
                            strNetworkType = UNKNOWN;
                            break;
                        }
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = THIRD_GENERATION;
                        } else {
                            strNetworkType = _strSubTypeName;
                        }
                        break;
                }
            }
        }
        return strNetworkType;
    }
}
