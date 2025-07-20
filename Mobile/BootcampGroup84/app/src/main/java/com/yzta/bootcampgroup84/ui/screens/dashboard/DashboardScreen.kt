import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavController
import com.TSNBank.TSNBank.ui.Components.DashboardCarousel
import com.yzta.bootcampgroup84.ui.screens.dashboard.DashboardViewModel
import com.yzta.bootcampgroup84.ui.screens.dashboard.components.ShowJournalSection


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardPage(
    navController: NavController,
    modifier: Modifier = Modifier,
    dashboardViewModel: DashboardViewModel = viewModel()
) {
    val state = rememberPullToRefreshState()
    val isRefreshing by dashboardViewModel.isRefreshing.collectAsState()
    Surface(color = Color.Black, modifier = Modifier.fillMaxSize()) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {

            },
            state = state,
            modifier = modifier,
            indicator = {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = isRefreshing,
                    state = state,
                )
            }
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                item {
                    DashboardCarousel(modifier = Modifier.padding(8.dp))
                }
                item{
                    ShowJournalSection(modifier=Modifier, navController = navController, )
                }
            }
        }
    }
}