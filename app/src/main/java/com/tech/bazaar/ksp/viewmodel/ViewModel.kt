package com.tech.bazaar.ksp.viewmodel

import androidx.lifecycle.ViewModel
import com.tech.bazaar.ksp.usecase.IUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(usecase: IUseCase) : ViewModel()