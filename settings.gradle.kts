pluginManagement {
    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://maven.aliyun.com/repository/central") }
        maven { url = uri("https://artifact.bytedance.com/repository/pangle") }
        // 华为官方广告标识服务SDK
        maven { url = uri("https://developer.huawei.com/repo") }
        // 荣耀官方广告标识服务SDK
        maven { url = uri("https://developer.hihonor.com/repo") }
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}


dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url = uri("https://artifact.bytedance.com/repository/pangle") }
        // 华为官方广告标识服务SDK
        maven { url = uri("https://developer.huawei.com/repo") }
        // 荣耀官方广告标识服务SDK
        maven { url = uri("https://developer.hihonor.com/repo") }
        google()
        mavenCentral()
        flatDir {
            dir(file("${rootDir}/libs"))
        }
    }
}


rootProject.name = "UJU-SDK"
include(":demo")
