package proj.kolot.uzsearch.di

import dagger.Module
import dagger.Provides
import proj.kolot.uzsearch.main.TrainsProvider
import proj.kolot.uzsearch.main.TrainsRouteSearcher
import proj.kolot.uzsearch.main.TrainsRouteSearcherImpl
import javax.inject.Singleton

@Module
class TrainsModule {

    @Provides
    @Singleton
    fun provideTrainsRouteService() : TrainsRouteSearcher = TrainsRouteSearcherImpl()


    @Provides
    @Singleton
    fun provideTrainsProvider() : TrainsProvider = TrainsProvider()
}