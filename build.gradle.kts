plugins {
    kotlin("jvm") version "2.4.0"
    id("com.gradleup.shadow") version "9.4.2"
    id("xyz.jpenilla.run-paper") version "3.0.2"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.21"
}

group = "me.honkling"
version = "0.1.0"

repositories {
    mavenCentral()
    maven("https://repo.codemc.io/repository/maven-snapshots/")
    maven("https://repo.codemc.io/repository/maven-releases/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io/")
    mavenLocal()
}

dependencies {
    paperweight.paperDevBundle("26.2.build.+")
    implementation("io.github.honkling.commando:spigot:3.0.5")
    implementation("com.github.honkling:4koma-regex:1.3.0")
    implementation("dev.kord:kord-core:0.15.0")
    implementation(kotlin("reflect"))

    compileOnly("com.github.honkling:ruby:develop")
    compileOnly("com.github.retrooper:packetevents-spigot:2.13.0")
}

tasks {
    runServer {
        minecraftVersion("26.2")
    }

    jar {
        manifest {
            attributes("paperweight-mappings-namespace" to "mojang")
        }
    }

    build {
        dependsOn("shadowJar")
    }

    shadowJar {
        dependencies {
            exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
            exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib-jdk7"))
            exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib-jdk8"))
            exclude(dependency("org.jetbrains.kotlin:kotlin-reflect"))
        }
    }

    processResources {
        val props = mapOf("version" to version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}

kotlin {
    jvmToolchain(25)
}
