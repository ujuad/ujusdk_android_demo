plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.ujuad.demo"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.wlyuan.callcenter"
        minSdk = 26
        targetSdk = 36
        versionCode = 310
        versionName = "3.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // 开启多 DEX 支持
        multiDexEnabled = true

        ndk {
            abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a"))
        }

    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packaging {
        resources {
            excludes += setOf(
                "dump_syms/**",
                "**/dump_syms.bin",
                "linux/dump_syms.bin"
            )
        }
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }


    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }

        getByName("debug") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    // 核心基础
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation("androidx.preference:preference-ktx:1.2.1")

    // AndroidX 导航组件
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")

    // AndroidX Activity/Fragment
    implementation("androidx.activity:activity-ktx:1.6.1")
    implementation("androidx.fragment:fragment-ktx:1.5.7")
    implementation("androidx.gridlayout:gridlayout:1.0.0")

    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.retrofit)

    implementation(libs.glide)

    // 华为、荣耀获取OAID，可有效提高Ecpm，建议引入
    implementation("com.huawei.hms:ads-identifier:3.4.62.300")
    implementation("com.hihonor.mcs:ads-identifier:1.0.2.301")


    //-----------------Maven远程方式引入---------------------//
    // 引入UJU-AD核心库（必须）
    implementation("com.ujusdk:uju-ad-core:3.1.30")
    // 引入UJU-ADX库，解锁更多广告预算（可选）
    implementation("com.ujusdk:uju-ad-adx:1.0.18")

    implementation("com.ujusdk:uju-csj-adapter:7.5.1.0")
    implementation("com.pangle_beta.cn:mediation-sdk:7.5.1.0")
    implementation("com.ujusdk:uju-ylh-adapter:4.680.1550")
    implementation("com.qq.e.union:union:4.680.1550")
    implementation("com.ujusdk:uju-bd-adapter:9.45.0")
    implementation("com.baidu:mobads:9.45.0")
    implementation("com.ujusdk:uju-ks-adapter:5.3.20.2")
    implementation(files("libs/kssdklite-ad-5.3.20.2.aar"))

//    //-----------------本地AAR方式引入---------------------//
//    // 引入UJU-AD核心库（必须）
//    implementation(files("libs/ad_core_3.1.30.aar"))
//    // 引入UJU-ADX库，解锁更多广告预算（可选）
//    implementation(files("libs/ad_adx_1.0.18.aar"))
//
//    implementation(files("libs/uju-csj-adapter-7.5.1.0.aar"))
//    implementation("com.pangle_beta.cn:mediation-sdk:7.5.1.0")
//    implementation(files("libs/uju-ylh-adapter-4.680.1550.aar"))
//    implementation("com.qq.e.union:union:4.680.1550")
//    implementation(files("libs/uju-bd-adapter-9.45.0.aar"))
//    implementation("com.baidu:mobads:9.45.0")
//    implementation(files("libs/uju-ks-adapter-5.3.20.2.aar"))
//    implementation(files("libs/kssdklite-ad-5.3.20.2.aar"))


}
