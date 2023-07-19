package com.tech.bazaar.ksp.extensions

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter

fun KSClassDeclaration.getConstructorParameters(): List<KSValueParameter> {
    val constructor = primaryConstructor ?: return emptyList()
    return constructor.parameters
}

fun KSClassDeclaration.getAnnotatedClassVariables(): List<KSPropertyDeclaration> {
    return this.getAllProperties().toList().filter {
        it.annotations.toList().isNotEmpty()
    }

}

fun KSValueParameter.getClassFromParameter(): KSClassDeclaration? {
    val parameterType = type.resolve()
    return parameterType.declaration as? KSClassDeclaration
}

fun Resolver.getAnnotatedClasses(
    annotationName: String
): MutableList<KSClassDeclaration> {
    val annotatedClasses = mutableListOf<KSClassDeclaration>()

    val annotatedSymbols = getSymbolsWithAnnotation(annotationName)
    for (symbol in annotatedSymbols) {
        if (symbol is KSClassDeclaration) {
            annotatedClasses.add(symbol)
        }
    }

    return annotatedClasses
}

fun <T> List<T>.sliceOrNull(indices: IntRange): List<T> {
    return try {
        this.slice(indices)
    } catch (exp: Exception) {
        emptyList()
    }
}