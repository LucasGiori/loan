package starter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.quarkus.jackson.ObjectMapperCustomizer
import jakarta.inject.Singleton

// Documentation https://www.baeldung.com/kotlin/jackson-kotlin
// https://www.baeldung.com/jackson-annotations

@Singleton
class SerializerBuilder: ObjectMapperCustomizer {
    override fun customize(objectMapper: ObjectMapper?) {
        objectMapper?.registerModules(
            KotlinModule.Builder()
                .withReflectionCacheSize(reflectionCacheSize=512)
                .configure(feature=KotlinFeature.NullToEmptyCollection, enabled=false)
                .configure(feature=KotlinFeature.NullToEmptyMap, enabled=false)
                .configure(feature=KotlinFeature.NullIsSameAsDefault, enabled=false)
                .configure(feature=KotlinFeature.SingletonSupport, enabled=false)
                .configure(feature=KotlinFeature.StrictNullChecks, enabled=false)
                .build()
        )
    }
}