package gr.imdb.movies.di
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import gr.imdb.movies.data.MoviesApi
import gr.imdb.movies.util.Constants.Companion.API_KEY
import gr.imdb.movies.util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()

    //satisfies provideRetrofitInstance
    @Singleton
    @Provides
    fun provideHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor) : OkHttpClient {
        httpLoggingInterceptor.level =  HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor { chain ->
                    val original = chain.request()
                    val originalHttpUrl = original.url

                    //Request customization: add request headers
                    val url = originalHttpUrl.newBuilder()
                            .addQueryParameter("api_key", API_KEY)
                            .build()

                    val requestBuilder = original.newBuilder().url(url)
                    val request = requestBuilder.build()
                    chain.proceed(request)
                }
            .build()
    }

    //returns gsonConverterFactory to provideRetrofit
    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    //the same as every retrofit builder in order to get our data
    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    //this will give the api instance in our RemoteDataSource
    //singleton means we re going to have only one instance of this
    //we're using application scope for this API
    //Provide is if instances must be created with the builder pattern.
    //or if you don't own the class because it comes from external library (Retrofit, Room etc)
    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): MoviesApi {
        return retrofit.create(MoviesApi::class.java)
    }

}