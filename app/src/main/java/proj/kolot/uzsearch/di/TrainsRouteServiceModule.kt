package proj.kolot.uzsearch.di

import dagger.Module
import dagger.Provides
import proj.kolot.uzsearch.main.TrainsRouteSearcher
import proj.kolot.uzsearch.main.TrainsRouteSearcherImpl
import javax.inject.Singleton

@Module
class TrainsRouteServiceModule {

    @Provides
    @Singleton
    fun provideTrainsRouteService() : TrainsRouteSearcher = TrainsRouteSearcherImpl()

}