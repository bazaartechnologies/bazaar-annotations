package com.tech.bazaar.ksp.viewmodel

import androidx.lifecycle.ViewModel
import com.tech.bazaar.ksp.usecase.IUseCase
import com.tech.bazaar.ksp.usecase.IUseCase2
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModel3 @Inject constructor(usecase: IUseCase, usecase2: IUseCase2) : ViewModel()
