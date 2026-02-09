package fr.leboncoin.data.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.leboncoin.data.BuildConfig
import fr.leboncoin.data.database.AlbumDatabase
import fr.leboncoin.data.network.api.AlbumApiService
import fr.leboncoin.data.repository.AlbumsRepositoryImpl
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AlbumDataModule {

    @Binds
    @Singleton
    abstract fun bindAlbumRepository(impl: AlbumsRepositoryImpl): AlbumsRepositoryImpl

    companion object {
        @Provides
        @Singleton
        fun provideAlbumDatabase(@ApplicationContext context: Context): AlbumDatabase {
            return Room.databaseBuilder(
                context,
                AlbumDatabase::class.java,
                "albums.db"
            ).build()
        }

        @Provides
        @Singleton
        fun provideAlbumApiService(retrofit: Retrofit): AlbumApiService {
            return retrofit.create(AlbumApiService::class.java)
        }

        @Provides
        @Singleton
        fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {
            val contentType = "application/json".toMediaType()
            return Retrofit.Builder()
                .baseUrl(AlbumApiService.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(json.asConverterFactory(contentType))
                .build()
        }

        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient {
            val builder = OkHttpClient.Builder()
            // Set logs when DEBUG
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                builder.addInterceptor(loggingInterceptor)
            }
            return builder.build()
        }

        @Provides
        @Singleton
        fun provideJson(): Json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }
}