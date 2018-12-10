Paytomat SDK   
==============
Paytomat Wallet SDK allows Android client apps to:

- **Get EOS Account**: Request EOS Authorization for an EOS Account.
- **Transfer EOS**: Send EOS and EOS Tokens.
- **Communicate via Simple Wallet**: DApp can interact with wallet via [Simple Wallet protocol](https://github.com/southex/SimpleWallet/blob/master/README_en.md)
 
## Installation
You can download a jar from GitHub's [releases page](https://github.com/paytomat/Paytomat-Android-SDK/releases).

Or use Gradle:
```groovy
allprojects {
    repositories {
        maven { url 'https://dl.bintray.com/axel95ua/Paytomat-Android-SDK' }
    }
}

dependencies {
    implementation 'com.paytomat:paytomat-android-sdk:0.1.2'
}
```

## How to use sdk

### Check if wallet is installed
```kotlin
PaytomatSdk.isPaytomatInstalled(context)
```

### Ask user to download Paytomat application
```kotlin
PaytomatSdk.requestDownload(this)
```

### Launch EOS Account request
```kotlin
val brandingModel: BrandingModel = BrandingModel.Builder()
            .setIconUrl("https://lh3.googleusercontent.com/4XigLFnoMuxPShHKPUdehtqBdKRe48nbYbFtpIroMUZZqcF-Bt1-UqpEL9ioOSc1-lfC=s360")
            .setTitle("Integration Test")
            .setDescription("Login to PTM integration app")
            .build()
            
val screenLaunched: Boolean = PaytomatSdk.requestLogin(this, brandingModel)
```

### Launch EOS Transfer request
```kotlin
val brandingModel: BrandingModel = BrandingModel.Builder()
            .setIconUrl("https://lh3.googleusercontent.com/4XigLFnoMuxPShHKPUdehtqBdKRe48nbYbFtpIroMUZZqcF-Bt1-UqpEL9ioOSc1-lfC=s360")
            .setTitle("Integration Test")
            .setDescription("Login to PTM integration app")
            .build()
val tokenModel: TokenModel = TokenModel.eos(0.001)
val screenLaunched: Boolean = PaytomatSdk.requestTransaction(
                this,
                brandingModel = brandingModel,
                to = "someaddress1",
                tokenModel = tokenModel,
                memo = "PTM integration test"
            )
```
If you need to transfer eos based token:
```kotlin
val tokenModel: TokenModel = TokenModel(amount = 0.001,
            contract = "ptitokenhome",
            symbol = "PTI",
            precision = 4)
```
### Read wallet response 
Handle wallet response in Activity
```kotlin
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PaytomatSdk.CODE_LOGIN_REQUEST) {
            val accountResult: Result<LoginAccount> = PaytomatSdk.handleLoginResult(requestCode, resultCode, data)
            if (accountResult.isSuccess()){
              val accountName: String? = accountResult.result?.accountName
              //Use account name
            }
            else {
              showError(accountResult.code)
            }
        } else if (requestCode == PaytomatSdk.CODE_TRANSFER_REQUEST) {
            val transactionIdResult: Result<TransferId> =
                PaytomatSdk.handleTransferResult(requestCode, resultCode, data)
            if (transactionIdResult.isSuccess()) {
              val transactionId: String? = transactionIdResult.result?.transactionId
              //Use transaction Id
            } else {
              showError(transactionIdResult.code)
            }
        }
    }
```
### Error handling example
```kotlin
private fun showError(errorCode: Int) {
        val errorMsg: String = when (errorCode) {
            Codes.SUCCESS -> return
            Codes.ERROR_CODE_NO_MNEMONIC -> "Paytomat not initialized"
            Codes.ERROR_CODE_NO_ACCOUNT -> "No account"
            Codes.ERROR_CODE_INVALID_EOS_SYMBOL -> "Invalid eos symbol"
            Codes.ERROR_CODE_INVALID_RECIPIENT_ACCOUNT -> "Invalid recipient address"
            Codes.ERROR_CODE_NOT_ENOUGHT_BALANCE -> "Not enough balance"
            Codes.ERROR_CODE_PARSE -> "Parsing error"
            Codes.ERROR_CODE_CANCELED -> "Canceled action"
            else -> "Something went wrong"
        }
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
    }
```
