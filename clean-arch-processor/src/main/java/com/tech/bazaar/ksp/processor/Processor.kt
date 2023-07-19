package com.tech.bazaar.ksp.processor

import com.tech.bazaar.ksp.extensions.getAnnotatedClasses
import com.tech.bazaar.ksp.extensions.getClassFromParameter
import com.tech.bazaar.ksp.extensions.getConstructorParameters
import com.tech.bazaar.ksp.properties.AnnotationProperties
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.tech.bazaar.ksp.extensions.getAnnotatedClassVariables
import com.tech.bazaar.ksp.extensions.sliceOrNull

internal class Processor(
    private val environment: SymbolProcessorEnvironment,
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        AnnotationProperties.values().forEach { annotation ->
            findAndValidateAnnotations(
                resolver, annotation = annotation.annotationName, annotation.inclusions
            )
        }
        return emptyList()
    }

    private fun findAndValidateAnnotations(
        resolver: Resolver, annotation: String, inclusions: List<String>
    ) {
        //step1 - Find interfaces annotated with @CustomAnnotation
        val annotatedClasses = resolver.getAnnotatedClasses(
            annotation
        )
        val rootPackage = getRootPackage(annotatedClasses.firstOrNull()?.packageName?.asString())

        // check if it was a class or interface
        if (annotatedClasses.any { it.classKind != ClassKind.INTERFACE }) {
            // for Class(Fragment/Activity/VM)
            validate(annotatedClasses, inclusions, rootPackage)
        } else {
            // for Interface
            //step 2 - For each interface, find Implementation classes
            val files = resolver.getAllFiles().toList()

            val declarations = mutableListOf<KSClassDeclaration>()

            files.map {
                it.declarations
            }.map {
                it.toList().filterIsInstance<KSClassDeclaration>()
            }.forEach {
                declarations.addAll(it)
            }

            val implementationClasses = declarations.filter {
                it.superTypes.map {
                    it.toString()
                }.toList().intersect(annotatedClasses.map {
                    it.toString()
                }.toSet()).isNotEmpty()
            }

            validate(implementationClasses, inclusions, rootPackage)
        }
    }

    private fun validate(
        implementationClasses: List<KSClassDeclaration>,
        inclusions: List<String>,
        appRootPackage: String?
    ) {
        // step 3 - For each Implementation class, access it’s constructor parameters
        implementationClasses.forEach { implClass ->
            // get & validate each constructor parameter
            implClass.getConstructorParameters().forEach {

                // step 4 - For each class, get it’s annotation
                val parameterClass = it.getClassFromParameter()

                val classRootPackage = getRootPackage(parameterClass?.packageName?.asString())
                if (classRootPackage != appRootPackage) {
                    return@forEach
                }
                // step 5 - Validate according to respective layer
                val annotationsList =
                    parameterClass?.annotations?.toMutableList() ?: mutableListOf()

                annotationsList.addAll(it.annotations)

                val annotations = annotationsList.map { it.shortName.asString() }

                if (annotations.isEmpty() || annotations.intersect(inclusions).isEmpty()) {
                    throw java.lang.Exception(
                        "$implClass params are annotated with $annotations but should have annotated with $inclusions"
                    )
                }
            }

            // get & validate each class variable which is @Inject annotated
            implClass.getAnnotatedClassVariables().filter {
                it.annotations.toList().isNotEmpty()
                        && (!it.annotations.toList()
                    .map { it.shortName.toString() }.toList().contains("Inject"))
            }.map {
                it.type.resolve().declaration as KSClassDeclaration
            }.forEach {

                val classRootPackage = getRootPackage(it.packageName.asString())
                if (classRootPackage != appRootPackage) {
                    return@forEach
                }
                // step 4 - For each class, get it’s annotation
                // step 5 - Validate according to respective layer
                val annotations =
                    it.annotations.toList().map { it.shortName.asString() }

                if (annotations.isEmpty() || annotations.intersect(inclusions).isEmpty()) {
                    throw java.lang.Exception(
                        "$implClass variables are annotated with $annotations but should have annotated with $inclusions"
                    )
                }
            }
        }
    }

    private fun getRootPackage(filePath: String?) = filePath?.split(".")
        ?.sliceOrNull(IntRange(0, 3))?.joinToString(separator = ".")

}