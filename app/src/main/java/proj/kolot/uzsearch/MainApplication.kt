package proj.kolot.uzsearch

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import org.slf4j.LoggerFactory
import proj.kolot.uzsearch.di.*
import proj.kolot.uzsearch.storage.db.HelperFactory


//import android.support.multidex.MultiDex


class MainApplication : Application() {
    private val LOG = LoggerFactory.getLogger("MainApplication")


    companion object {
        lateinit var graph: AppComponent
        lateinit var instance: MainApplication
            private set

    }

    override fun attachBaseContext(base: Context) {
        MultiDex.install(base)
        super.attachBaseContext(base)

    }


    override fun onTerminate() {
        HelperFactory.releaseHelper();
        super.onTerminate()

    }

    override fun onCreate() {
        super.onCreate()
        HelperFactory.setHelper(getApplicationContext());
        instance = this
        graph = DaggerAppComponent.builder()
                .contextModule(ContextModule(this))
                .trainsModule(TrainsModule())
                .storageModule(StorageModule())
                .repeaterModule(RepeaterModule())
                .build()


    }

}

