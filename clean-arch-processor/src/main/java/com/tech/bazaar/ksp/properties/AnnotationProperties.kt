package com.tech.bazaar.ksp.properties

import com.tech.bazaar.annotation.ApiServiceAnnotation
import com.tech.bazaar.annotation.GenericAnnotation
import com.tech.bazaar.annotation.LDSAnnotation
import com.tech.bazaar.annotation.RDSAnnotation
import com.tech.bazaar.annotation.RepositoryAnnotation
import com.tech.bazaar.annotation.UseCaseAnnotation

enum class AnnotationProperties(val annotationName: String, val inclusions: List<String>) {
    LDS(
        LDSAnnotation::class.java.canonicalName,
        listOf("Dao")//"androidx.room.Dao"
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
            RDSAnnotation::class.java.simpleName,
            GenericAnnotation::class.java.simpleName
        )
    ),
    USECASE(
        UseCaseAnnotation::class.java.canonicalName,
        listOf(
            RepositoryAnnotation::class.java.simpleName,
            UseCaseAnnotation::class.java.simpleName,
            GenericAnnotation::class.java.simpleName
        )
    ),
    VIEWMODEL(
        "dagger.hilt.android.lifecycle.HiltViewModel",
        listOf(
            UseCaseAnnotation::class.java.simpleName,
            GenericAnnotation::class.java.simpleName,
        )
    )
}
