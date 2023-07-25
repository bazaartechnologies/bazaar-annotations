package com.tech.bazaar.ksp.viewmodel

import androidx.lifecycle.ViewModel
import com.tech.bazaar.ksp.usecase.IUseCase2
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModel2 @Inject constructor(usecase: IUseCase2) : ViewModel()
