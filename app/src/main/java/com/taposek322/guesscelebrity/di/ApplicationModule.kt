package com.taposek322.guesscelebrity.di

import com.taposek322.guesscelebrity.data.DownloadImage
import com.taposek322.guesscelebrity.data.DownloadImageImpl
import com.taposek322.guesscelebrity.domain.ApplicationInternalInteractor
import com.taposek322.guesscelebrity.domain.ApplicationInternalInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ApplicationModule {
    @Binds
    @Singleton
    fun bindDownloadImage(downloadImageImpl: DownloadImageImpl): DownloadImage

    @Binds
    @Singleton
    fun bindInternalInteractor(applicationInternalInteractorImpl: ApplicationInternalInteractorImpl): ApplicationInternalInteractor

    companion object {
        @Provides
        @IoDispatcher
        @Singleton
        fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
    }
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher