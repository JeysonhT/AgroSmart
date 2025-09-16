package com.example.agrosmart.data.local.realmsetup.realm;

import android.content.Context;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;

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
                .schemaVersion(2)
                .migration(new RealmMigration() {
                    @Override
                    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
                        if(oldVersion == 1 && newVersion > oldVersion){
                            realm.getSchema().get("DiagnosisHistory").
                                    addField("recommendation", String.class)
                                    .removeField("observations");
                        }
                    }
                })
                .compactOnLaunch()
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);
    }

}
