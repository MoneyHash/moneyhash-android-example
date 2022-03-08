# How to use MoneyHash Android

## About MoneyHash

MoneyHAsh is a Super-API infrastructure for payment orchestration and revenue operations in emerging markets. We provide a single integration to your network of pay-in and pay-out providers, and various other services that you can utilize and combine to build a unique custom payment stack. Our core features include:

1. A single API/SDK integration for Pay-in & Pay-out
2. Unified checkout embed compatible with all integrated providers
3. Orchestration and routing capabilities to allow for optimal transaction route and to increase authorization rates
4. Micro-services to extend your stack capabilities such as subscription management, invoicing, and payment links
5. PCI-compliant card vault to store and tokenize sensitive customer and card information
6. Central dashboard for a unified stack controls and transaction reporting

You can learn more about us by visiting [our website](https://www.moneyhash.io/).

### Requirements

* Android 5.0 (API level 21) and above
* [Android Gradle Plugin](https://developer.android.com/studio/releases/gradle-plugin) 3.5.1
* [Gradle](https://gradle.org/releases/) 5.4.1+
* [AndroidX](https://developer.android.com/jetpack/androidx/) (as of v11.0.0)

### Configuration

Add `moneyhash:android` to your `build.gradle` dependencies.

```
repositories {
    maven{ url "https://jitpack.io" }
}

dependencies {
    implementation 'io.moneyHash:android:0.0.6'
}
```

### Create a Payment Intent
You will need to create a Payment Intent and use it's ID to initiate the SDK, There are two ways to create a Payment Intent:

- **Using The Sandbox**

  Which is helpful to manually and quickly create a Payment Intent without having to running any backend code. For more information about the Sandbox refer to this [section](https://moneyhash.github.io/sandbox)
- **Using The Payment Intent API**

  This will be the way your backend server will eventually use to create a Payment Intents, for more information refer to this [section](https://moneyhash.github.io/api)


### Usage

To start the payment flow use the Payment Intent ID from the step above as a parameter along with a PaymentResultContract instance like below:

1- Add PaymentActivity to AndroidManifest.xml
```xml
<activity android:name="com.moneyhash.sdk.android.PaymentActivity" />
```

2- Setup the activity result contract
```kotlin
    private val paymentResultContract =
        registerForActivityResult(PaymentResultContract()) { result ->
            if(result != null) {
                when(result){
                    is PaymentStatus.Error -> {
                        errorsData = result.errors.joinToString()
                    }
                    is PaymentStatus.Failed -> {
                        paymentResult = result.result
                    }
                    is PaymentStatus.RequireExtraAction -> {
                        paymentResult = result.result
                        requiredActions = result.actions
                    }
                    is PaymentStatus.Success -> {
                        paymentResult = result.result
                    }
                    is PaymentStatus.Unknown -> {
                        status = "Unknown"
                    }
                    is PaymentStatus.Cancelled -> {
                        status = "Cancelled"
                    }
                }
            }
        }
```

3- Start the payment flow
```kotlin
MoneyHash.INSTANCE.start(paymentIntentId, paymentResultContract)
```

### Payment Statuses
Once your customer finishes adding the payment information they will reach one of the following statuses, and  a callback is fired with the payment status which indicate the current status of your payment.

Status | #
--- | ---
Error | There was an error while processing the payment and more details about the errors will be found inside errors data.
Success | The payment is Successful.
RequireExtraAction | That payment flow is done and the customer needs to do some extra actions off the system, a list of the actions required by the customer will be found inside the actions data, and it should be rendered to the customer in your app.
Failed | There was an error while processing the payment.
Unknown | There was an unknown state received and this should be checked from your MoneyHash dashboard.
Cancelled | The customer cancelled the payment flow by clicking back or cancel.

### Proguarding

MoneyHash Android SDK is proguard friendly and the rules is added by default so you don't need to care about adding extra proguard rules

### Questions and Issues

Please provide any feedback via a [GitHub Issue](https://github.com/MoneyHash/moneyhash-android-example/issues/new?template=bug_report.md).