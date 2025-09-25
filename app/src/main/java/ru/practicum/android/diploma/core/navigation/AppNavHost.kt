package ru.practicum.android.diploma.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.command.CommandScreen
import ru.practicum.android.diploma.favorites.ui.FavoritesScreen
import ru.practicum.android.diploma.favorites.ui.viewmodel.FavoritesViewModel
import ru.practicum.android.diploma.filtration.ui.screens.CountrySelectionScreen
import ru.practicum.android.diploma.filtration.ui.screens.IndustryFilterScreen
import ru.practicum.android.diploma.filtration.ui.screens.MainFilterScreen
import ru.practicum.android.diploma.filtration.ui.screens.RegionSelectionScreen
import ru.practicum.android.diploma.filtration.ui.screens.WorkPlaceScreen
import ru.practicum.android.diploma.filtration.ui.viewmodel.CountrySelectionViewModel
import ru.practicum.android.diploma.filtration.ui.viewmodel.RegionSelectionViewModel
import ru.practicum.android.diploma.filtration.ui.viewmodel.WorkPlaceViewModel
import ru.practicum.android.diploma.main.ui.SearchScreen
import ru.practicum.android.diploma.main.ui.viewmodel.SearchVacancyViewModel
import ru.practicum.android.diploma.vacancy.ui.components.VacancyScreen
import ru.practicum.android.diploma.vacancy.ui.viewmodel.VacancyViewModel

/**
 * The application's navigation host, managing navigation between screens.
 *
 * @param navController The navigation controller used to handle screen transitions.
 */
@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    val searchVm = koinViewModel<SearchVacancyViewModel>()
    NavHost(navController = navController, startDestination = Screen.Main.route, modifier = modifier) {
        composable(Screen.Main.route) {
            SearchScreen(
                modifier = Modifier,
                viewModel = searchVm,
                navController = navController
            ) { vacancyId ->
                if (searchVm.clickDebounce()) {
                    navController.navigate(Screen.VacancyDetails.route + "/$vacancyId")
                }
            }
        }
        composable(Screen.Command.route) {
            CommandScreen(modifier = Modifier)
        }
        composable(Screen.Favorites.route) {
            val vm = koinViewModel<FavoritesViewModel>()
            FavoritesScreen(
                viewModel = vm
            ) { vacancyId ->
                if (vm.clickDebounce()) {
                    navController.navigate(Screen.VacancyDetails.route + "/$vacancyId")
                }
            }
        }
        composable(
            route = Screen.VacancyDetails.route + "/{vacancyId}",
            arguments = listOf(navArgument("vacancyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val viewModel: VacancyViewModel = koinViewModel()
            VacancyScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable(Screen.Filtration.route) {
            MainFilterScreen(navController = navController, searchVm = searchVm)
        }
        composable(Screen.WorkPlace.route) {
            val viewModel: WorkPlaceViewModel = koinViewModel()
            WorkPlaceScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable(Screen.CountrySelection.route) {
            val viewModel: CountrySelectionViewModel = koinViewModel()
            CountrySelectionScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable(
            route = "region_selection?countryId={countryId}",
            arguments = listOf(
                navArgument("countryId") {
                    type = NavType.StringType
                    defaultValue = "null"
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val countryIdString = backStackEntry.arguments?.getString("countryId")
            val countryId = countryIdString?.toIntOrNull()
            val viewModel: RegionSelectionViewModel = koinViewModel()
            RegionSelectionScreen(
                viewModel = viewModel,
                navController = navController,
                currentCountryId = countryId
            )
        }

        composable(
            route = Screen.IndustrySelection.route,
            arguments = listOf(
                navArgument("industryId") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val industryId = backStackEntry.arguments?.getString("industryId") ?: ""
            IndustryFilterScreen(
                industryId = industryId,
                navController = navController
            )
        }
    }
}
