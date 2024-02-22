# How to use MoneyHash Android

### Requirements

* Android 5.0 (API level 21) and above
* [Android Gradle Plugin](https://developer.android.com/studio/releases/gradle-plugin) 3.5.1
* [Gradle](https://gradle.org/releases/) 5.4.1+
* [AndroidX](https://developer.android.com/jetpack/androidx/) (as of v11.0.0)

### Configuration

Add `moneyhash:android` to your `build.gradle` dependencies.

```groovy
repositories {
    maven{ url "https://jitpack.io" }
}

dependencies {
    implementation 'io.moneyHash:android:1.0.4'
}
```

Enable `viewBinding` in your project.

```groovy
buildFeatures {
  viewBinding true
}
```

## How to use?

- Create moneyHash instance using `MoneyHashSDKBuilder`

```Kotlin
import com.moneyhash.sdk.android.core.MoneyHashSDKBuilder
val moneyHash = MoneyHashSDKBuilder.build()
```

> MoneyHash SDK guides to for the actions required to be done, to have seamless integration through intent details `state`

| state                             | Action                                                                                                                                                                                          |
| :-------------------------------- |:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `METHOD_SELECTION`                | Use `moneyHash.getIntentMethod` to get different intent methods and render them natively with your own styles & use `moneyHash.proceedWithMethod` to proceed with one of them on user selection |
| `INTENT_FORM`                     | Use `moneyHash.renderForm` to start the SDK flow to let MoneyHash handle the flow for you & listen for result by using IntentContract() for Activity result                                     |
| `INTENT_PROCESSED`                | Render your successful confirmation UI with the intent details                                                                                                                                  |
| `TRANSACTION_FAILED`              | Render your failure UI with the intent details                                                                                                                                                  |
| `TRANSACTION_WAITING_USER_ACTION` | Render your pending actions confirmation UI with the intent details & `externalActionMessage` if exists on `Transaction`                                                                        |
| `EXPIRED`                         | Render your intent expired UI                                                                                                                                                                   |
| `CLOSED`                          | Render your intent closed UI                                                                                                                                                                    |

- Get intent details based on the intent id and type (Payment/Payout)

```kotlin
moneyHash.getIntentDetails(intentId, IntentType.Payment, onSuccess = { intentDetails ->
   // handle the intent details
}, onFail = { throwable ->
  // handle the error
})
```

- Get intent available payment/payout methods, saved cards and customer balances

```kotlin
moneyHash.getIntentMethods(intentId, IntentType.Payment, onSuccess = { intentMethods ->
   // handle the intent methods native UI
}, onFail = { throwable ->
  // handle the error
})
```

- Proceed with a payment/payout method, card or wallet

```kotlin
moneyHash
  .proceedWithMethod(
    intentId = "intentId",
    intentType = IntentType.Payment,
    selectedMethodId = "methodId",
    methodType = MethodType.EXPRESS_METHOD, // method type that returned from the intent methods
    methodMetaData = MethodMetaData(
      // optional and can be null
      cvv = "cvv", // required for customer saved cards that requires cvv
    ),
    onSuccess = { intentMethods, intentDetails ->
      // handle the intent methods native UI and updated intent details
    }, onFail = { throwable ->
      // handle the error
    })
```

- Reset the selected method on and intent to null

> Can be used for `back` button after method selection
> or `retry` button on failed transaction UI to try a different
> method by the user.

```kotlin
moneyHash
  .resetSelectedMethod(
    intentId = "intentId",
    intentType = IntentType.Payment,
    onSuccess = { intentMethods, intentDetails ->
      // handle the intent methods native UI and updated intent details
    }, onFail = { throwable ->
      // handle the error
    })
```

- Delete a customer saved card

```kotlin
moneyHash
  .deleteSavedCard(
    cardTokenId = "cardTokenId", // card token id that returned in savedCards list in IntentMethods
    intentSecret = "intentSecret", // intent secret that returned in intent details
    onSuccess = {
      // card deleted successfully
    }, onFail = { throwable ->
      // handle the error
    })
```

- Render SDK embed forms and payment/payout integrations

> Must be called if `state` of an intent is `INTENT_FORM` to let MoneyHash handle the payment/payout. you can listen for completion or failure of an intent by providing using the IntentContract().
> you can also use it directly to render the embed form for payment/payout without handling the methods selection native UI.

Add PaymentActivity / PayoutActivity to AndroidManifest.xml

```xml
<activity android:name="com.moneyhash.sdk.android.payment.PaymentActivity" />
```

```xml
<activity android:name="com.moneyhash.sdk.android.payout.PayoutActivity" />
```

```kotlin
 private val resultContract =
  registerForActivityResult(IntentContract()) { result ->

  }

moneyHash
  .renderForm(
    intentId = "intentId",
    intentType = IntentType.Payment,
    launcher = resultContract,
    resultType = ResultType.RESULT_SCREEN_WITH_CALLBACK // Result type can be RESULT_SCREEN_WITH_CALLBACK or CALLBACK (to not render moneyhash success screen)
  )
```

## Responses

### Methods Error Response

```kotlin
enum class IntentType {
    Payment,
    Payout;
}

// Intent methods 

data class IntentMethods(
  val customerBalances: List<CustomerBalance>? = null,
  val paymentMethods: List<PaymentMethod>? = null,
  val expressMethods: List<ExpressMethod>? = null,
  val savedCards: List<SavedCard>? = null,
  val payoutMethods: List<PayoutMethod>? = null
)

data class CustomerBalance(
  val balance: Double? = null,
  val id: String? = null,
  val icon: String? = null,
  val isSelected: Boolean? = null,
  val type: MethodType = MethodType.CUSTOMER_BALANCE
)

data class PaymentMethod(
  val id: String? = null,
  val title: String? = null,
  val isSelected: Boolean? = null,
  val confirmationRequired: Boolean? = null,
  val icons: List<String>? = null,
  val type: MethodType? = MethodType.PAYMENT_METHOD
)

data class PayoutMethod(
  val id: String? = null,
  val title: String? = null,
  val isSelected: Boolean? = null,
  val confirmationRequired: Boolean? = null,
  val icons: List<String>? = null,
  val type: MethodType? = MethodType.PAYOUT_METHOD
)

data class ExpressMethod(
  val id: String? = null,
  val title: String? = null,
  val isSelected: Boolean? = null,
  val confirmationRequired: Boolean? = null,
  val icons: List<String>? = null,
  val type: MethodType? = MethodType.EXPRESS_METHOD
)

data class SavedCard(
  val id: String? = null,
  val brand: String? = null,
  val last4: String? = null,
  val expiryMonth: String? = null,
  val expiryYear: String? = null,
  val country: String? = null,
  val logo: String? = null,
  val requireCvv: Boolean? = null,
  val cvvConfig: CvvConfig? = null,
  val type: MethodType? = MethodType.SAVE_CARD
)

data class CvvConfig(
  val digitsCount: Int? = 0
)

// Intent Details

data class IntentDetails(
  val selectedMethod: String? = null,
  val intent: IntentData? = null,
  val walletBalance: Double? = null,
  val transaction: TransactionData? = null,
  val redirect: RedirectData? = null,
  val state: State? = null
)

data class TransactionData(
  val billingData: String? = null,
  val amount: Double? = null,
  val externalActionMessage: List<String>? = null,
  val amountCurrency: String? = null,
  val id: String? = null,
  val methodName: String? = null,
  val method: String? = null,
  val createdDate: String? = null,
  val status: String? = null,
  val customFields: String? = null,
  val providerTransactionFields: String? = null,
  val customFormAnswers: String? = null
)

data class IntentData(
  val amount: AmountData? = null,
  val secret: String? = null,
  val expirationDate: String? = null,
  val isLive: Boolean? = null,
  val id: String? = null,
  val status: IntentStatus? = null
)

data class AmountData(
  val value: String? = null,
  val formatted: Double? = null,
  val currency: String? = null,
  val maxPayout: Double? = null,
)

data class RedirectData(
  val redirectUrl: String? = null
)

// Method Meta Data
data class MethodMetaData(
  val cvv: String? = null
)
```


### Proguarding
MoneyHash Android SDK is proguard friendly and the rules is added by default so you don't need to
care about adding extra proguard rules

### Questions and Issues
Please provide any feedback via a [GitHub Issue](https://github.com/MoneyHash/moneyhash-android-example/issues/new?template=bug_report.md).