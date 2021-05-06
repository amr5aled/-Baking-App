package com.example.amr5aled.baking;

import android.content.Intent;
import android.widget.RemoteViewsService;



public class mywidgetservice extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new mywidgetfactory(getApplicationContext());
    }
}
