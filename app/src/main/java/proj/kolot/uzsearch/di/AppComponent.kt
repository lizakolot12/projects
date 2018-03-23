package proj.kolot.uzsearch.di

import dagger.Component
import proj.kolot.uzsearch.list.TrainListPresenter
import proj.kolot.uzsearch.main.SearchRepeaterImpl
import proj.kolot.uzsearch.main.TrainsProvider
import proj.kolot.uzsearch.service.SearchService
import proj.kolot.uzsearch.settings.SettingsPresenter
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(TrainsModule::class, SettingsStorageModule::class, ContextModule::class))
interface AppComponent {

    fun inject(trainListPresenter: TrainListPresenter)
    // fun inject(settingsStorage: SettingsStorage)
    fun inject(settingsPresenter: SettingsPresenter)

    //fun inject(searchService: SearchService)
    fun inject(searchManager: SearchRepeaterImpl)

    fun inject(service: SearchService)
    fun inject(trainsProvider:TrainsProvider)

}

