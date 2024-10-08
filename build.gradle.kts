plugins {
    kotlin("jvm") version "2.0.20"
}

group = "com.huang"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    compileOnly(files("D:\\Projects\\idea\\PluginDemoCollection\\libs\\coolrequest-tool.jar"))

    compileOnly(files("D:\\Environment\\jdk-8u381\\lib\\tools.jar"))

}

//此配置用于将第三方依赖同项目打包成jar
tasks.withType<Jar> {
//    from({
//        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
//    })
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}