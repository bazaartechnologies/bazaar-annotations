package com.tech.bazaar.ksp.usecase

import com.bazaar.handshake.sdk.HandshakeGenerator

class UseCase2Impl(
    private val handshakeGenerator: HandshakeGenerator,
) : IUseCase2