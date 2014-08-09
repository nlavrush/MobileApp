package com.eccentex.dcm.MobileApp.Authentication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created with IntelliJ IDEA.
 * User: Udini
 * Date: 19/03/13
 * Time: 19:10
 */
public class AuthenticatorService extends Service {
    @Override
    public IBinder onBind(Intent intent) {

        Authenticator authenticator = new Authenticator(this);
        return authenticator.getIBinder();
    }
}
