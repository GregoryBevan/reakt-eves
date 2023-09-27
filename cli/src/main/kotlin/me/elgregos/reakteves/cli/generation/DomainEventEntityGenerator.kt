package me.elgregos.reakteves.cli.generation

//import com.squareup.kotlinpoet.*
//import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
//import me.elgregos.reakteves.infrastructure.event.EventEntity
//import org.springframework.data.relational.core.mapping.Table
//import java.io.File
//import java.util.*

//fun generateDomainEventEntity(kotlinSourceDir: File, domain: String, domainPackage: String) {
//    val file = FileSpec.builder("$domainPackage.infrastructure.event", "${domain}EventEntity")
//        .addType(
//            TypeSpec.classBuilder("${domain}EventEntity")
//                .addAnnotation(
//                    AnnotationSpec.builder(Table::class)
//                        .addMember("\"${domain.lowercase()}_event\"")
//                        .build()
//                )
//                .addModifiers(KModifier.DATA)
//                .primaryConstructor(
//                    FunSpec.constructorBuilder()
//                        .addParameter(
//                            ParameterSpec.builder("id", UUID::class)
//                                .addAnnotation(
//                                    AnnotationSpec.builder(JvmName::class)
//                                        .useSiteTarget(AnnotationSpec.UseSiteTarget.GET)
//                                        .addMember("\"id\"")
//                                        .build()
//                                )
//                                .build()
//                        )
//                        .addParameter(
//                            ParameterSpec.builder(
//                                "sequenceNum",
//                                Long::class.asTypeName().copy(nullable = true)
//                            ).defaultValue("null").build()
//                        )
//                        .addParameter(ParameterSpec.builder("version", Int::class).defaultValue("1").build())
//                        .build()
//                )
//                .addProperty(PropertySpec.builder("id", UUID::class).initializer("id").build())
//                .addProperty(
//                    PropertySpec.builder("sequenceNum", Long::class.asTypeName().copy(nullable = true))
//                        .initializer("sequenceNum").addModifiers(KModifier.OVERRIDE).build()
//                )
//                .addProperty(
//                    PropertySpec.builder("version", Int::class).initializer("version").addModifiers(KModifier.OVERRIDE)
//                        .build()
//                )
//                .superclass(
//                    EventEntity::class.parameterizedBy(UUID::class, UUID::class).plusParameter(
//                        ClassName("${domainPackage}.domain.event", "GameEvent")
//                    )
//                )
//                .addSuperclassConstructorParameter("id")
//                .addSuperclassConstructorParameter("sequenceNum")
//                .addSuperclassConstructorParameter("version")
//                .build()
//
//
//        )
//        .build()
//
//
//    file.writeTo(kotlinSourceDir)
//}