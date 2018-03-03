package proj.kolot.uzsearch.di

import android.content.Context
import dagger.Module
import dagger.Provides
import proj.kolot.uzsearch.settings.SettingsStorage
import javax.inject.Singleton

@Module
class SettingsStorageModule {


    @Provides
    @Singleton
    fun provideSettings() : SettingsStorage = SettingsStorage()
}
