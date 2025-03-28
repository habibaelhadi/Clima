package com.example.clima.composableFunctions.map.view

import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clima.R
import com.example.clima.composableFunctions.map.viewmodel.MapViewModel
import com.example.clima.local.AppDataBase
import com.example.clima.local.WeatherLocalDataSource
import com.example.clima.remote.RetrofitProduct
import com.example.clima.remote.WeatherRemoteDataSource
import com.example.clima.repo.WeatherRepo
import com.example.clima.utilites.GeocoderHelper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.compose.autocomplete.components.PlacesAutocompleteTextField
import com.google.android.libraries.places.compose.autocomplete.models.AutocompletePlace
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import java.util.Locale

@Composable
fun MapScreen() {
        val context = LocalContext.current

        // Initialize Places API (outside ViewModel)
        Places.initializeWithNewPlacesApiEnabled(context, "AIzaSyCaj10hgcwGaosoYRyv79ppLviFJ9eMNmM")
        val placesClient: PlacesClient = Places.createClient(context)

        // Create ViewModel with custom factory
        val mapScreenFactory = MapViewModel.MapViewModelFactory(
            placesClient = placesClient,
            repository = WeatherRepo.getInstance(
                WeatherRemoteDataSource(RetrofitProduct.retrofit),
                WeatherLocalDataSource(AppDataBase.getInstance(context).weatherDao())
            )
        )
        val viewModel: MapViewModel = viewModel(factory = mapScreenFactory)

        val searchText by viewModel.searchText.collectAsStateWithLifecycle()
        val predictions by viewModel.predictions.collectAsStateWithLifecycle()
        val selectedLocation by viewModel.selectedLocation.collectAsStateWithLifecycle()

        val markerState = rememberMarkerState(
            position = LatLng(
                selectedLocation?.latitude ?: 31.2001,
                selectedLocation?.longitude ?: 29.9187,
            )
        )
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(markerState.position, 10f)
        }

        LaunchedEffect(Unit) {
            viewModel.message.collect {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        LaunchedEffect(selectedLocation) {
            selectedLocation?.let {
                val newLatLng = LatLng(it.latitude, it.longitude)

                markerState.position = newLatLng

                cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(newLatLng, 12f))
            }
        }


        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(mapType = MapType.HYBRID),
                onMapClick = { latLng ->
                    Log.i("TAG", "onMapClick: lat -> {${latLng.latitude}}, lng -> {${latLng.longitude}}")
                    markerState.position = latLng
                    viewModel.updateSelectedLocation(latLng)
                }
            ) {
                Marker(
                    state = markerState,
                    title = stringResource(R.string.selected_location),
                    snippet = "Marker at selected location"
                )
            }

            PlacesAutocompleteTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                searchText = searchText,
                textFieldMaxLines = 1,
                predictions = predictions.map {
                    AutocompletePlace(
                        placeId = it.placeId,
                        primaryText = it.getPrimaryText(null),
                        secondaryText = it.getSecondaryText(null)
                    )
                },
                onQueryChanged = { viewModel.onSearchQueryChanged(it) },
                onSelected = { autocompletePlace ->
                    viewModel.onPlaceSelected(autocompletePlace.placeId)
                },
                selectedPlace = null
            )


            selectedLocation?.let { location ->
                val lat = location.latitude
                val lng = location.longitude
                val address = getAddressFromLocation(lat,lng)
                val context = LocalContext.current
                val country = GeocoderHelper(context).getLocationInfo(
                    LatLng(lat, lng)
                )
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(colorResource(R.color.white))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = address,
                            style = MaterialTheme.typography.titleMedium,
                            color = colorResource(R.color.black),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = stringResource(R.string.latitude, location.latitude))
                        Text(text = stringResource(R.string.longitude, location.longitude))
                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                viewModel.insertFavouriteLocation(
                                    location.latitude,
                                    location.longitude,
                                    country.country!!,
                                    country.city!!
                                )
                            },
                            colors = ButtonDefaults.buttonColors(colorResource(R.color.purple)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(R.string.add_to_favourites),
                                style = MaterialTheme.typography.titleMedium,
                                color = colorResource(R.color.white),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

}

@Composable
fun getAddressFromLocation(lat : Double,lng : Double): String {
    val geocoder = Geocoder(LocalContext.current, Locale.getDefault())
    return try {
        val addresses = geocoder.getFromLocation(lat, lng, 1)
        if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]
            listOfNotNull(
                address.featureName,
                address.locality,
                address.adminArea,
                address.countryName
            ).joinToString(", ")
        } else {
            "Address Not Found !"
        }
    } catch (e: Exception) {
        e.printStackTrace()
        "Error Fetching Address"
    }
}