package proj.kolot.uzsearch.di

import dagger.Module
import dagger.Provides
import proj.kolot.uzsearch.settings.RequestStorage
import proj.kolot.uzsearch.settings.SettingsStorage
import javax.inject.Singleton

@Module
class SettingsStorageModule {


    @Provides
    @Singleton
    fun provideSettings() : SettingsStorage = SettingsStorage()

    @Provides
    @Singleton
    fun provideRequestStorage() : RequestStorage = RequestStorage()
}
