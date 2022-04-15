package edu.project.trippo.di

import dagger.Component
import dagger.Module
import dagger.Provides
import edu.project.trippo.api.Api
import edu.project.trippo.ui.main.MainViewModel
import retrofit2.Retrofit

@Component(modules = [MainModule::class])
interface DaggerComponent {
    fun inject(activity: MainViewModel)
}

@Module(includes = [IOModule::class])
interface MainModule

@Module
interface IOModule {
    @Module
    companion object {
        @JvmStatic
        @Provides
        fun provideApiWeather(): Api =
            Retrofit
                .Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/weather")
                .build()
                .create(Api::class.java)

        @JvmStatic
        @Provides
        fun provideApiCorona(): Api =
            Retrofit
                .Builder()
                .baseUrl("https://api.smartable.ai/coronavirus/stats")
                .build()
                .create(Api::class.java)

        @JvmStatic
        @Provides
        fun provideApiHotels(): Api =
            Retrofit
                .Builder()
                .baseUrl("hotels-com-provider.p.rapidapi.com")
                .build()
                .create(Api::class.java)

        @JvmStatic
        @Provides
        fun provideApiCities(): Api =
            Retrofit
                .Builder()
                .baseUrl("wft-geo-db.p.rapidapi.com")
                .build()
                .create(Api::class.java)
    }
}