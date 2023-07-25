package com.tech.bazaar.ksp.properties

import com.tech.bazaar.annotation.ApiService as ApiServiceAnnotation
import com.tech.bazaar.annotation.LDS as LDSAnnotation
import com.tech.bazaar.annotation.RDS as RDSAnnotation
import com.tech.bazaar.annotation.Repository as RepositoryAnnotation
import com.tech.bazaar.annotation.UseCase as UseCaseAnnotation

enum class AnnotationProperties(val annotationName: String, val inclusions: List<String>) {
    LDS(
        LDSAnnotation::class.java.canonicalName,
        listOf("Dao")
    ),
    RDS(
        RDSAnnotation::class.java.canonicalName,
        listOf(
            ApiServiceAnnotation::class.java.simpleName
        )
    ),
    REPOSITORY(
        RepositoryAnnotation::class.java.canonicalName,
        listOf(
            LDSAnnotation::class.java.simpleName,
            RDSAnnotation::class.java.simpleName
        )
    ),
    USECASE(
        UseCaseAnnotation::class.java.canonicalName,
        listOf(
            RepositoryAnnotation::class.java.simpleName,
            UseCaseAnnotation::class.java.simpleName
        )
    ),
    VIEWMODEL(
        "dagger.hilt.android.lifecycle.HiltViewModel",
        listOf(
            UseCaseAnnotation::class.java.simpleName
        )
    ),
    ACTIVITY_FRAGMENT(
        "dagger.hilt.android.AndroidEntryPoint",
        listOf(
            "HiltViewModel"
        )
    )
}
