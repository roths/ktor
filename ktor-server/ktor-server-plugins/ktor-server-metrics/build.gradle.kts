description = ""
kotlin {
    sourceSets {
        jvmMain {
            dependencies {
                api("io.dropwizard.metrics:metrics-core:4.2.8")
                api("io.dropwizard.metrics:metrics-jvm:4.2.9")
            }
        }
    }
}
