package com.combo.runcombi.walk

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine

object LocationProvider {
    private var locationCallback: LocationCallback? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(context: Context, onLocation: (LatLng) -> Unit) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let {
                    onLocation(LatLng(it.latitude, it.longitude))
                }
            }
        }
        fusedLocationClient?.requestLocationUpdates(request, locationCallback!!, Looper.getMainLooper())
    }

    fun stopLocationUpdates() {
        locationCallback?.let { fusedLocationClient?.removeLocationUpdates(it) }
        locationCallback = null
    }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(context: Context): LatLng? {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        return suspendCancellableCoroutine { cont ->
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).addOnSuccessListener { location ->
                if (location != null) {
                    cont.resume(LatLng(location.latitude, location.longitude)) { _, _, _ -> }
                } else {
                    cont.resume(null) { _, _, _ -> }
                }
            }.addOnFailureListener {
                cont.resume(null) { _, _, _ -> }
            }
        }
    }
}