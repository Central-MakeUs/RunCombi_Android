package com.combo.runcombi.feature.login

import android.content.Context
import com.combo.runcombi.feature.login.model.LoginData
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class LoginManager(
    private val context: Context,
) {
    suspend fun request(): LoginData = withContext(Dispatchers.IO) {
        suspendCancellableCoroutine { continuation ->
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                    if (error != null) {
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            continuation.resume(LoginData.Failed(error))
                        } else {
                            UserApiClient.instance.loginWithKakaoAccount(context) { token2, error2 ->
                                if (error2 != null) {
                                    continuation.resume(LoginData.Failed(Exception(error2.message)))
                                } else if (token2 != null) {
                                    continuation.resume(LoginData.Success(token2.accessToken))
                                }
                            }
                        }
                    } else if (token != null) {
                        continuation.resume(LoginData.Success(token.accessToken))
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                    if (error != null) {
                        continuation.resume(LoginData.Failed(Exception(error.message)))
                    } else if (token != null) {
                        continuation.resume(LoginData.Success(token.accessToken))
                    }
                }
            }
        }
    }
}
