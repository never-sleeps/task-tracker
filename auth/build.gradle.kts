plugins {
    kotlin("multiplatform")
}

group = rootProject.group
version = rootProject.version

kotlin {
    jvm {
        withJava()
    }
    linuxX64 {}
    macosX64 {}
    macosArm64 {}

    sourceSets {
        val datetimeVersion: String by project
        val coroutinesVersion: String by project

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))

                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                api("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")

                implementation(project(":common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}
