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

Add `moneyhash:android` to your `build.gradle` dependencies.

```
repositories {
    maven{ url "https://jitpack.io" }
}

dependencies {
    implementation 'io.moneyHash:android:0.0.3'
}
```

## How to generate payment intent id (paymentIntentId)
Payment intent id is a unique identifier that is associated with a payment intent, you don't have to care whether it is (is_live) payment or not, this is will be handled automatically from your backend api_key
For more information about payment intents and how to generate it you can check the [documentation](https://moneyhash.github.io/api#create-a-payment-intent)

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

3- start the payment flow
```kotlin
MoneyHash.INSTANCE.start(paymentIntentId, paymentResultContract)
```

## Payment statues
When the payment is done, a callback is fired with the payment status which indicate the current status for your payment intent

Status | #
--- | ---
Error | Means there was an error while processing the payment and more details about the errors will be found inside errors data
Success | Means the payment processing is succeeded and completed
RequireExtraAction | Means that payment flow is done and the customer needs to do some extra actions to finish the payment which will be found inside actions data
Failed | Means there was an error while processing the payment
Unknown | Means there was an unknown state received and this should be checked from your payment dashboard
Cancelled | Means the user cancelled the payment flow by clicking back or cancel

## Proguarding

MoneyHash Android SDK is proguard friendly and the rules is added by default so you don't need to care about adding extra proguard rules 

## Questions and Issues

Please provide any feedback via a [GitHub Issue](https://github.com/MoneyHash/moneyhash-android-example/issues/new?template=bug_report.md).
