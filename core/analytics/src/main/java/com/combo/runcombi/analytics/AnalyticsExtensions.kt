package com.combo.runcombi.analytics

import com.combo.runcombi.analytics.AnalyticsEvent
import com.combo.runcombi.analytics.AnalyticsEvent.Param
import com.combo.runcombi.analytics.AnalyticsHelper

// 기본 화면 및 사용자 이벤트
fun AnalyticsHelper.logScreenView(screenName: String) =
    logEvent(
        AnalyticsEvent(
            type = AnalyticsEvent.Types.SCREEN_VIEW,
            extras = listOf(
                Param(key = AnalyticsEvent.ParamKeys.SCREEN_NAME, value = screenName),
            ),
        ),
    )

fun AnalyticsHelper.logUserLogin(loginMethod: String) =
    logEvent(
        AnalyticsEvent(
            type = AnalyticsEvent.Types.USER_LOGIN,
            extras = listOf(
                Param(key = AnalyticsEvent.ParamKeys.LOGIN_METHOD, value = loginMethod),
            ),
        ),
    )

fun AnalyticsHelper.logUserSignup(signupMethod: String) =
    logEvent(
        AnalyticsEvent(
            type = AnalyticsEvent.Types.USER_SIGNUP,
            extras = listOf(
                Param(key = AnalyticsEvent.ParamKeys.SIGNUP_METHOD, value = signupMethod),
            ),
        ),
    )

// 산책 관련 이벤트
fun AnalyticsHelper.logWalkStarted(walkType: String) =
    logEvent(
        AnalyticsEvent(
            type = AnalyticsEvent.Types.WALK_STARTED,
            extras = listOf(
                Param(key = AnalyticsEvent.ParamKeys.WALK_TYPE, value = walkType),
            ),
        ),
    )

fun AnalyticsHelper.logWalkCompleted(duration: String, distance: String) =
    logEvent(
        AnalyticsEvent(
            type = AnalyticsEvent.Types.WALK_COMPLETED,
            extras = listOf(
                Param(key = AnalyticsEvent.ParamKeys.WALK_DURATION, value = duration),
                Param(key = AnalyticsEvent.ParamKeys.WALK_DISTANCE, value = distance),
            ),
        ),
    )

// 설정 및 기능 이벤트
fun AnalyticsHelper.logSettingChanged(settingName: String, settingValue: String) =
    logEvent(
        AnalyticsEvent(
            type = AnalyticsEvent.Types.SETTING_CHANGED,
            extras = listOf(
                Param(key = AnalyticsEvent.ParamKeys.SETTING_NAME, value = settingName),
                Param(key = AnalyticsEvent.ParamKeys.SETTING_VALUE, value = settingValue),
            ),
        ),
    )

fun AnalyticsHelper.logFeatureUsed(featureName: String) =
    logEvent(
        AnalyticsEvent(
            type = AnalyticsEvent.Types.FEATURE_USED,
            extras = listOf(
                Param(key = AnalyticsEvent.ParamKeys.FEATURE_NAME, value = featureName),
            ),
        ),
    )

// 앱 생명주기 이벤트
fun AnalyticsHelper.logAppStarted(isNewUser: Boolean, userStatus: String) =
    logEvent(
        AnalyticsEvent(
            type = AnalyticsEvent.Types.APP_STARTED,
            extras = listOf(
                Param(key = AnalyticsEvent.ParamKeys.IS_NEW_USER, value = isNewUser.toString()),
                Param(key = AnalyticsEvent.ParamKeys.USER_STATUS, value = userStatus),
            ),
        ),
    )

fun AnalyticsHelper.logAppBackgrounded() =
    logEvent(
        AnalyticsEvent(
            type = AnalyticsEvent.Types.APP_BACKGROUNDED
        )
    )

fun AnalyticsHelper.logAppForegrounded() =
    logEvent(
        AnalyticsEvent(
            type = AnalyticsEvent.Types.APP_FOREGROUNDED
        )
    )

// 사용자 상호작용 이벤트
fun AnalyticsHelper.logButtonClick(buttonName: String, screenName: String? = null) =
    logEvent(
        AnalyticsEvent(
            type = AnalyticsEvent.Types.BUTTON_CLICK,
            extras = listOf(
                Param(key = AnalyticsEvent.ParamKeys.BUTTON_NAME, value = buttonName),
            ).let { params ->
                if (screenName != null) {
                    params + Param(key = AnalyticsEvent.ParamKeys.SCREEN_NAME, value = screenName)
                } else {
                    params
                }
            }
        )
    )

// 오류 및 예외 이벤트
fun AnalyticsHelper.logError(errorType: String, errorMessage: String, screenName: String? = null) =
    logEvent(
        AnalyticsEvent(
            type = AnalyticsEvent.Types.ERROR_OCCURRED,
            extras = listOf(
                Param(key = AnalyticsEvent.ParamKeys.ERROR_TYPE, value = errorType),
                Param(key = AnalyticsEvent.ParamKeys.ERROR_MESSAGE, value = errorMessage),
            ).let { params ->
                if (screenName != null) {
                    params + Param(key = AnalyticsEvent.ParamKeys.SCREEN_NAME, value = screenName)
                } else {
                    params
                }
            }
        )
    )
