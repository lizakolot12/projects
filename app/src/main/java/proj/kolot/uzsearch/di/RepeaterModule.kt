package proj.kolot.uzsearch.di

import dagger.Module
import dagger.Provides
import proj.kolot.uzsearch.main.SearchRepeater
import proj.kolot.uzsearch.main.SearchRepeaterImpl
import javax.inject.Singleton

/**
 * Created by Kolot Liza on 6/8/18.
 */
@Module
class RepeaterModule {

        @Provides
        @Singleton
        fun provideSearchRepeater() : SearchRepeater = SearchRepeaterImpl()


}