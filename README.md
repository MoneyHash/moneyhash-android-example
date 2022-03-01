# How to use MoneyHash Android

## About MoneyHash

We are MEA’s first payment orchestration and revenue operations platform. We bring you all integrations and microservices you need to grow, under one-roof.
You can learn more about us by visiting [our website](https://www.moneyhash.io/).

## Requirements

* Android 5.0 (API level 21) and above
* [Android Gradle Plugin](https://developer.android.com/studio/releases/gradle-plugin) 3.5.1
* [Gradle](https://gradle.org/releases/) 5.4.1+
* [AndroidX](https://developer.android.com/jetpack/androidx/) (as of v11.0.0)

## Configuration

[![](https://jitpack.io/v/io.moneyHash/Android.svg)](https://jitpack.io/#io.moneyHash/Android)

Add `moneyhash:android` to your `build.gradle` dependencies.

```
repositories {
    maven{ url "https://jitpack.io" }
}

dependencies {
    implementation 'io.moneyHash:android:0.0.2'
}
```

## Usage

To start the payment flow you need to get the payment id from your backend and send it as parameter with the environment (staging or production) and then start the flow

1- Add PaymentActivity to AndroidManifest.xml
```xml
<activity android:name="com.moneyhash.sdk.android.PaymentActivity" />
```

2- setup the activity result contract
```kotlin
    private val paymentResultContract =
        registerForActivityResult(PaymentResultContract()) { result ->
            if(result != null) {
                when(result){
                    is PaymentStatus.Error -> {
                        tv.text = result.errors.joinToString()
                    }
                    is PaymentStatus.Failed -> {
                        tv.text = "Failed"
                    }
                    is PaymentStatus.RequireExtraAction -> {
                        tv.text = result.actions.joinToString()
                    }
                    is PaymentStatus.Success -> {
                        tv.text = "Success"
                    }
                    is PaymentStatus.Unknown -> {
                        tv.text = "Unknown"
                    }
                    is PaymentStatus.Cancelled -> {
                        tv.text = "Cancelled"
                    }
                }
            }
        }
```

3- setup the target environment
```kotlin
MoneyHash.INSTANCE.setEnvironment(MoneyHashEnvironment.STAGING)
```

4- start the payment flow
```kotlin
MoneyHash.INSTANCE.start(paymentId, paymentResultContract)
```

## Proguarding

MoneyHash Android SDK is proguard friendly and the rules is added by default so you don't need to care about adding extra proguard rules 

## Questions and Issues

Please provide any feedback via a [GitHub Issue](https://github.com/MoneyHash/moneyhash-android-example/issues/new?template=bug_report.md).
