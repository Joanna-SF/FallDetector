package org.falldetectives.falldetector;

import android.content.Context;
import android.os.Handler;
import android.widget.TextView;

import Bio.Library.namespace.BioLib;

public class BioLibInstance {
    private static BioLib instance;

    private void BioLibSingleton() {
        // Private constructor to prevent instantiation
    }


    public static BioLib getInstance(Context context, Handler handler, TextView text) {
        if (instance == null) {
            try {
                //lib- new instance of BioLib class, "this" used because the BioLibUserActivity has the mHandler class
                instance = new BioLib(context, handler);

                text.append("Init BioLib \n");
            } catch (Exception e) {
                text.append("Error to init BioLib \n");
                e.printStackTrace();
            }
            
        }
        return instance;
    }

}