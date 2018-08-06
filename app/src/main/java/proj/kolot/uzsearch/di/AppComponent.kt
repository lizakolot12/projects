package proj.kolot.uzsearch.di

import dagger.Component
import proj.kolot.uzsearch.main.SearchRepeaterImpl
import proj.kolot.uzsearch.main.TrainsProvider
import proj.kolot.uzsearch.route.RoutePresenter
import proj.kolot.uzsearch.service.NotificationFactory
import proj.kolot.uzsearch.service.SearchService
import proj.kolot.uzsearch.service.StartupReceiver
import proj.kolot.uzsearch.task.edit.EditTaskPresenter
import proj.kolot.uzsearch.task.list.TasksPresenter
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(TrainsModule::class, StorageModule::class, ContextModule::class, RepeaterModule::class))
interface AppComponent {

    fun inject(trainListPresenter: RoutePresenter)
    fun inject(settingsPresenter: EditTaskPresenter)
    fun inject(tasksPresenter: TasksPresenter)
    fun inject(searchManager: SearchRepeaterImpl)
    fun inject(service: SearchService)
    fun inject(trainsProvider:TrainsProvider)
    fun inject(startupReceiver: StartupReceiver)
    fun inject(factory: NotificationFactory)
    //fun inject(adapter: TaskAdapter)

}

