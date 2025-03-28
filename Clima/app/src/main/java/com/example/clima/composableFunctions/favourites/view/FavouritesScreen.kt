package com.example.clima.composableFunctions.favourites.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clima.R
import com.example.clima.composableFunctions.favourites.viewmodel.FavouriteViewModel
import com.example.clima.local.AppDataBase
import com.example.clima.local.WeatherLocalDataSource
import com.example.clima.model.FavouritePOJO
import com.example.clima.remote.RetrofitProduct
import com.example.clima.remote.WeatherRemoteDataSource
import com.example.clima.repo.WeatherRepo
import com.example.clima.ui.theme.Gray
import com.example.clima.ui.theme.colorGradient1
import com.example.clima.ui.theme.colorGradient2
import com.example.clima.ui.theme.colorGradient3
import com.example.clima.utilites.Response

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun FavouritesScreen(
    snackbar: SnackbarHostState,
    navigateToMap : () -> Unit,
    navigateToDetails: (FavouritePOJO) -> Unit
) {

    val favFactory = FavouriteViewModel.FavFactory(
        WeatherRepo.getInstance(
            WeatherRemoteDataSource(RetrofitProduct.retrofit),
            WeatherLocalDataSource(AppDataBase.getInstance(LocalContext.current).weatherDao())
        )
    )

    val viewModel: FavouriteViewModel = viewModel(factory = favFactory)
    val uiState by viewModel.favouriteLocations.collectAsStateWithLifecycle()
    viewModel.getFavouriteCities()

    when (uiState) {
        is Response.Failure -> {
            val error = (uiState as Response.Failure).error
            Log.e("TAG", "HomeScreen: $error")
        }

        is Response.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize()
            ) { CircularProgressIndicator() }
        }

        is Response.Success -> {
            val data = (uiState as Response.Success).data
           Scaffold(
               floatingActionButton = {
                       FloatingActionButton(
                           onClick = {
                              navigateToMap()
                           },
                           containerColor = colorGradient1,
                           shape = CircleShape
                       ) {
                           Icon(
                               Icons.Default.Add,
                               contentDescription = "Favorite",
                               tint = Color.White
                           )
                       }

               }
           ) { innerPadding ->
               if (data.isEmpty()) {
                   NoFavourites()
               } else {
                   FavouritesList(data, viewModel, navigateToDetails,snackbar)
               }
           }
        }
    }
}

@Composable
fun NoFavourites() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_location_pin_24),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Favourites",
                fontFamily = FontFamily(Font(R.font.exo2)),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colorGradient1,
                textAlign = TextAlign.Center
            )
        }

        Image(
            painter = painterResource(R.drawable.fav),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 48.dp)
                .size(600.dp)
        )
    }
}

@Composable
fun FavouritesList(
    data: List<FavouritePOJO>,
    viewModel: FavouriteViewModel,
    navigateToDetails: (FavouritePOJO) -> Unit,
    snackbar: SnackbarHostState
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_location_pin_24),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Favourites",
                fontFamily = FontFamily(Font(R.font.exo2)),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colorGradient1,
                textAlign = TextAlign.Center
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp, max = 400.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(data.size) {
                FavouriteCard(data[it], viewModel, navigateToDetails,snackbar)
            }
        }
    }
}

@Composable
fun FavouriteCard(
    data: FavouritePOJO,
    viewModel: FavouriteViewModel,
    navigateToDetails: (FavouritePOJO) -> Unit,
    snackbar: SnackbarHostState
) {
    SwipeToDeleteContainer(
        item = data ,
        onDelete = { item ->
            viewModel.deleteFavouriteCity(item)
        },
        onRestore = {
            viewModel.getFavouriteCities()
        },
        snackbarHostState = snackbar
    ) {
        Card(
            modifier = Modifier
                .clickable {
                    navigateToDetails(data)
                }
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp
                ),
            shape = RoundedCornerShape(32.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)

        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(colorGradient3, colorGradient2, colorGradient1)
                        )
                    )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.country),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column{
                        Text(
                            text = data.city,
                            fontFamily = FontFamily(Font(R.font.exo2)),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Gray
                        )
                        Text(
                            text = data.country,
                            fontFamily = FontFamily(Font(R.font.exo2)),
                            fontSize = 18.sp,
                            color = Gray
                        )
                    }
                }
            }
        }
    }
}

