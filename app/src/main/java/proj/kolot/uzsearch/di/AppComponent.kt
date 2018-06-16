package proj.kolot.uzsearch.di

import dagger.Component
import proj.kolot.uzsearch.list.TasksPresenter
import proj.kolot.uzsearch.main.SearchRepeaterImpl
import proj.kolot.uzsearch.main.TrainsProvider
import proj.kolot.uzsearch.route.TrainListPresenter
import proj.kolot.uzsearch.service.SearchService
import proj.kolot.uzsearch.service.StartupReceiver
import proj.kolot.uzsearch.settings.SettingsPresenter
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(TrainsModule::class, SettingsStorageModule::class, ContextModule::class, RepeaterModule::class))
interface AppComponent {

    fun inject(trainListPresenter: TrainListPresenter)
    fun inject(settingsPresenter: SettingsPresenter)
    fun inject(tasksPresenter: TasksPresenter)
    fun inject(searchManager: SearchRepeaterImpl)
    fun inject(service: SearchService)
    fun inject(trainsProvider:TrainsProvider)
    fun inject(startupReceiver: StartupReceiver)

}

