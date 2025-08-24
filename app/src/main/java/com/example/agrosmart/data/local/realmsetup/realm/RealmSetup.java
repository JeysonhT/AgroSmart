package com.example.agrosmart.data.local.realmsetup.realm;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmSetup {
    private RealmSetup(){
    }

    //inicializa realm
    public static void setRealConfig(Context context){
        Realm.init(context);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("Agrosmart.reaml")
                .allowQueriesOnUiThread(false)
                .allowWritesOnUiThread(false)
                .schemaVersion(1)
                .compactOnLaunch()
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);
    }

}
