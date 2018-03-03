package proj.kolot.uzsearch.di

import android.content.Context
import dagger.Module
import javax.inject.Singleton
import dagger.Provides


@Module
class ContextModule(private var mContext: Context) {

    @Provides
    @Singleton
    internal fun provideContext(): Context {
        return mContext
    }
}