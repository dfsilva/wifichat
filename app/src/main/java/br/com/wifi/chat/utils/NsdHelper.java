package br.com.wifi.chat.utils;

/**
 * Created by KayO on 07/12/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;
import android.widget.Toast;


public class NsdHelper {

    //public static final String SERVICE_TYPE = "_http._tcp.";
    public static final String SERVICE_TYPE = "_WIFI_CHAT._tcp.";
    //public static final String SERVICE_TYPE = "_NSDChat._tcp.local.";
    public static final String TAG = NsdHelper.class.getName();

    Activity mContext;


    NsdManager mNsdManager;
    NsdManager.ResolveListener mResolveListener;
    NsdManager.DiscoveryListener mDiscoveryListener;
    NsdManager.RegistrationListener mRegistrationListener;

    private boolean registerListenerInUse = false;
    private boolean discoverListenerInUse = false;


    public String mServiceName = null;
    public NsdServiceInfo mService;

    public NsdHelper(Activity context) {
        mContext = context;
        mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
    }

    public void initializeNsd() {
        initializeDiscoveryListener();
        initializeRegistrationListener();
    }

    public void initializeDiscoveryListener() {
        mDiscoveryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(TAG, "Service discovery started");
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                Log.d(TAG, "Service discovery success" + service);
                if (!service.getServiceType().equals(SERVICE_TYPE)) {
                    Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
                } else if (service.getServiceName().equals(mServiceName)) {
                    Log.d(TAG, "Same machine: " + mServiceName);
                } else {
                    mNsdManager.resolveService(service, new MyResolveListener());
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                Log.e(TAG, "service lost" + service);
                if (mService == service) {
                    mService = null;
                }
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "Discovery stopped: " + serviceType);
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }
        };
    }


    public void initializeRegistrationListener() {
        mRegistrationListener = new NsdManager.RegistrationListener() {

            @Override
            public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
                mServiceName = NsdServiceInfo.getServiceName();
                Toast.makeText(mContext, "Registration successful: " + mServiceName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRegistrationFailed(NsdServiceInfo arg0, int arg1) {
                Toast.makeText(mContext, "Registration failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo arg0) {
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
            }

        };
    }

    public void registerService(int port) {
        if (!registerListenerInUse) {
            NsdServiceInfo serviceInfo = new NsdServiceInfo();
            serviceInfo.setPort(port);
            serviceInfo.setServiceName(mServiceName);
            serviceInfo.setServiceType(SERVICE_TYPE);
            mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
            registerListenerInUse = true;
        }
    }

    public void discoverServices() {
        if (!discoverListenerInUse) {
            mNsdManager.discoverServices(
                    SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);

            //set flag to show register Listener is in use
            discoverListenerInUse = true;
        }
    }

    public void stopDiscovery() {
        mNsdManager.stopServiceDiscovery(mDiscoveryListener);
    }

    public NsdServiceInfo getChosenServiceInfo() {
        return mService;
    }

    public void tearDown() {
        mNsdManager.unregisterService(mRegistrationListener);
    }

    private class MyResolveListener implements NsdManager.ResolveListener {
        @Override
        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
            Log.e(TAG, "Resolve failed" + errorCode);
        }

        @Override
        public void onServiceResolved(NsdServiceInfo serviceInfo) {
            Log.e(TAG, "Resolve Succeeded. " + serviceInfo);
            if (serviceInfo.getServiceName().startsWith(mServiceName)) {
                Log.d(TAG, "Same IP.");
                return;
            }

            mService = serviceInfo;
            Toast.makeText(mContext, mService.toString(), Toast.LENGTH_SHORT).show();


            //TODO: ADICIONAR O CONTATO ENCONTRADO NO BANCO DE DADOS

//            final Contact foundContact = new Contact(mService.getServiceName(), null, null, null);
//            foundContact.setIpAddress((Inet4Address) mService.getHost());
//            foundContact.setPort(mService.getPort());
//            foundContact.setOnline(true);
//            //get rid of possible duplicates
//            /*if (currentContactList != null)
//                for (int i = 0;i< currentContactList.size();i++) {
//                    if (currentContactList.get(i).getPhoneNumber() != null && (currentContactList.get(i).getPhoneNumber().equals(foundContact.getPhoneNumber())))
//                        currentContactList.remove(currentContactList.get(i));
//                }*/
//            if (currentContactList != null)
//                for (Contact contact : currentContactList) {
//                    if (contact.getName().equals(foundContact.getName()))
//                        currentContactList.remove(contact);
//                }
//
//
//            mContext.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                    //add newly found contact at the top of the list
//                    currentContactList.add(0, foundContact);
//                    discoveryListAdapter.setAllContacts(currentContactList);
//                    discoveryListAdapter.notifyDataSetChanged();
//                }
//            });


        }
    }
}
