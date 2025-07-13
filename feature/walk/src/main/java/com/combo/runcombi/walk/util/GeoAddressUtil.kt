package com.combo.runcombi.walk.util

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale

object GeoAddressUtil {
    suspend fun getAddress(context: Context, latLng: LatLng?): String {
        if (latLng == null) return "위치 정보 없음"
        return if (Build.VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
            getAddressTiramisu(context, latLng)
        } else {
            getAddressLegacy(context, latLng)
        }
    }

    @RequiresApi(VERSION_CODES.TIRAMISU)
    private suspend fun getAddressTiramisu(context: Context, latLng: LatLng): String =
        withContext(Dispatchers.IO) {
            val geocoder = Geocoder(context, Locale.getDefault())
            return@withContext suspendCancellableCoroutine<String> { cont ->
                geocoder.getFromLocation(
                    latLng.latitude, latLng.longitude, 1, object : Geocoder.GeocodeListener {
                        override fun onGeocode(addresses: MutableList<Address>) {
                            cont.resume(formatAddress(addresses)) { _, _, _ -> }
                        }

                        override fun onError(errorMessage: String?) {
                            cont.resume("주소 정보 없음") { _, _, _ -> }
                        }
                    })
            }
        }

    private suspend fun getAddressLegacy(context: Context, latLng: LatLng): String =
        withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                formatAddress(addresses)
            } catch (e: Exception) {
                "주소 불러오기 실패"
            }
        }

    private fun formatAddress(addresses: List<Address>?): String {
        if (addresses.isNullOrEmpty()) return "주소 정보 없음"
        return listOfNotNull(
            addresses[0].adminArea?.takeIf { it.isNotBlank() },
            addresses[0].locality?.takeIf { it.isNotBlank() },
            addresses[0].subLocality?.takeIf { it.isNotBlank() }).joinToString(" ")
    }
} 