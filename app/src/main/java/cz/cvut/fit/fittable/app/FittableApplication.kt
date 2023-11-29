package cz.cvut.fit.fittable.app

import android.app.Application
import cz.cvut.fit.fittable.app.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class FittableApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@FittableApplication)
            modules(appModule)
        }
    }
}