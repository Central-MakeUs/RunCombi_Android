import androidx.lifecycle.ViewModel
import com.combo.runcombi.signup.model.TermsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class TermsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TermsUiState())
    val uiState: StateFlow<TermsUiState> = _uiState

    fun setAllChecked(value: Boolean) {
        _uiState.update {
            it.copy(
                termsChecked = value,
                privacyChecked = value,
                locationChecked = value
            )
        }
    }

    fun updateTermsChecked(value: Boolean) {
        _uiState.update { it.copy(termsChecked = value) }
    }

    fun updatePrivacyChecked(value: Boolean) {
        _uiState.update { it.copy(privacyChecked = value) }
    }

    fun updateLocationChecked(value: Boolean) {
        _uiState.update { it.copy(locationChecked = value) }
    }
}
