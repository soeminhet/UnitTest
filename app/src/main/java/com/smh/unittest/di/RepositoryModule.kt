package com.smh.unittest.di

import com.smh.unittest.data.repository.RepositoryImpl
import com.smh.unittest.domain.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindRepository(
        repositoryImpl: RepositoryImpl
    ): Repository
}