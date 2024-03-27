package com.example.midexam
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.icons.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class CitySelection : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MidExamTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Navigation()
                }
            }
        }
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "citySelection") {
        composable("citySelection") {
            CitySelectionScreen(navController = navController)
        }
        composable("weatherStatus/{cityName}") { backStackEntry ->
            WeatherStatusScreen(
                cityName = backStackEntry.arguments?.getString("cityName") ?: ""
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySelectionScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(text = "Select City") }
            )
        }
    ) {  it -> Column(
        modifier = Modifier.padding(it)
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Fit
            )
            Image(
                painter = painterResource(id = R.drawable.map),
                contentDescription = null,
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Fit
            )
            CityInputField(navController = navController)
        }
    }
}

@Composable
fun CityInputField(navController: NavHostController) {
    var cityName by remember { mutableStateOf("") }

    TextField(
        value = cityName,
        onValueChange = { cityName = it },
        label = { Text("Enter city name") },
        modifier = Modifier.padding(16.dp)
    )

    Button(
        onClick = {
            navController.navigate("weatherStatus/$cityName")
        },
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "Show Weather")
    }
}

@Composable
fun WeatherStatusScreen(cityName: String) {
    val context = LocalContext.current
    val weatherInfo: List<String> = run {
        val resourceId = context.resources.getIdentifier(
            "weather_info_${cityName.lowercase()}",
            "array",
            context.packageName
        )
        if (resourceId != 0) context.resources.getStringArray(resourceId).toList() else listOf("Info not available")
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Weather Status for $cityName")
        weatherInfo.forEach { info ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Icon(Icons.Filled.Thermostat, contentDescription = null, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = info)
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Icon(Icons.Filled.WaterDrop, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Humidity: 60%")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Icon(Icons.Filled.Cloud, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Condition: Sunny")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CitySelectionPreview() {
    MidExamTheme {
        CitySelectionScreen(navController = rememberNavController())
    }
}
