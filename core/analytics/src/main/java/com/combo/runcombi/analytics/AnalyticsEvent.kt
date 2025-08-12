package com.combo.runcombi.analytics

data class AnalyticsEvent(
    val type: String,
    val extras: List<Param> = emptyList(),
) {
    class Types {
        companion object {
            const val SCREEN_VIEW = "screen_view"
            const val USER_LOGIN = "user_login"
            const val USER_SIGNUP = "user_signup"
            const val WALK_STARTED = "walk_started"
            const val WALK_COMPLETED = "walk_completed"
            const val SETTING_CHANGED = "setting_changed"
            const val FEATURE_USED = "feature_used"
            const val BUTTON_CLICK = "button_click"
            const val ERROR_OCCURRED = "error_occurred"
            const val APP_STARTED = "app_started"
            const val APP_BACKGROUNDED = "app_backgrounded"
            const val APP_FOREGROUNDED = "app_foregrounded"
        }
    }

    data class Param(val key: String, val value: String)

    class ParamKeys {
        companion object {
            const val SCREEN_NAME = "screen_name"
            const val LOGIN_METHOD = "login_method"
            const val SIGNUP_METHOD = "signup_method"
            const val WALK_TYPE = "walk_type"
            const val WALK_DURATION = "walk_duration"
            const val WALK_DISTANCE = "walk_distance"
            const val SETTING_NAME = "setting_name"
            const val SETTING_VALUE = "setting_value"
            const val FEATURE_NAME = "feature_name"
            const val BUTTON_NAME = "button_name"
            const val ERROR_TYPE = "error_type"
            const val ERROR_MESSAGE = "error_message"
            const val IS_NEW_USER = "is_new_user"
            const val USER_STATUS = "user_status"
        }
    }
}
