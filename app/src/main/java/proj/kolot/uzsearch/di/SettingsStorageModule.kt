package proj.kolot.uzsearch.di

import dagger.Module
import dagger.Provides
import proj.kolot.uzsearch.storage.Storage
import javax.inject.Singleton

@Module
class StorageModule {



    @Provides
    @Singleton
    fun provideStorage() : Storage = Storage()
}
