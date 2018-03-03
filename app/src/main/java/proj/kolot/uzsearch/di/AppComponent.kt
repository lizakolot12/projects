package proj.kolot.uzsearch.di

import dagger.Component
import proj.kolot.uzsearch.list.TrainListPresenter
import proj.kolot.uzsearch.service.SearchService
import proj.kolot.uzsearch.main.SearchRepeaterImpl
import proj.kolot.uzsearch.settings.SettingsPresenter
import proj.kolot.uzsearch.settings.SettingsStorage
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(TrainsRouteServiceModule::class, SettingsStorageModule::class, ContextModule::class))
interface AppComponent {

   fun inject(trainListPresenter: TrainListPresenter)
   // fun inject(settingsStorage: SettingsStorage)
     fun inject(settingsPresenter: SettingsPresenter)
    //fun inject(searchService: SearchService)
    fun inject(searchManager: SearchRepeaterImpl)

}

