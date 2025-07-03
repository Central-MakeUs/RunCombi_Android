import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TermsViewModel : ViewModel() {
    var termsChecked by mutableStateOf(false)
        private set
    var privacyChecked by mutableStateOf(false)
        private set
    var locationChecked by mutableStateOf(false)
        private set

    val allChecked: Boolean
        get() = termsChecked && privacyChecked && locationChecked

    fun setAllChecked(value: Boolean) {
        termsChecked = value
        privacyChecked = value
        locationChecked = value
    }

    fun updateTermsChecked(value: Boolean) {
        termsChecked = value
    }
    fun updatePrivacyChecked(value: Boolean) {
        privacyChecked = value
    }
    fun updateLocationChecked(value: Boolean) {
        locationChecked = value
    }
}