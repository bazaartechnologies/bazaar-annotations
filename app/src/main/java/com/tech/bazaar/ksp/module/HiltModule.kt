package com.tech.bazaar.ksp.module

import com.bazaar.handshake.sdk.HandshakeGenerator
import com.tech.bazaar.ksp.lds.ILDS
import com.tech.bazaar.ksp.lds.LDSImpl
import com.tech.bazaar.ksp.lds.MyDao
import com.tech.bazaar.ksp.rds.ApiService
import com.tech.bazaar.ksp.rds.IRDS
import com.tech.bazaar.ksp.rds.RDSImpl
import com.tech.bazaar.ksp.repository.IRepository
import com.tech.bazaar.ksp.repository.RepositoryImpl
import com.tech.bazaar.ksp.usecase.IUseCase
import com.tech.bazaar.ksp.usecase.IUseCase2
import com.tech.bazaar.ksp.usecase.UseCase2Impl
import com.tech.bazaar.ksp.usecase.UseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class HiltModule {

    @Provides
    internal fun repositoryProvider(
        rds: IRDS, lds: ILDS
    ): IRepository = RepositoryImpl(rds, lds)

    @Provides
    internal fun useCase2Provider(): IUseCase2 = UseCase2Impl(HandshakeGenerator())

    @Provides
    internal fun useCaseProvider(useCase2: IUseCase2, repository: IRepository): IUseCase =
        UseCaseImpl(useCase2, repository)

    @Provides
    internal fun rdsProvider(): IRDS = RDSImpl(object : ApiService {

    })

    @Provides
    internal fun ldsProvider(): ILDS = LDSImpl(object : MyDao {

    })

}
