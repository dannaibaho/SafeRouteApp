package com.example.saferoute.presentation.walkthrough

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.*
import com.example.saferoute.core.theme.DarkBlue
import com.example.saferoute.core.theme.White
import com.example.saferoute.presentation.walkthrough.WalkthroughViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WalkthroughScreen(
    navController: NavController,
    viewModel: WalkthroughViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            count = viewModel.pages.size,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            val item = viewModel.pages[page]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.title,
                    color = White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = item.description,
                    color = White,
                    fontSize = 16.sp
                )
            }
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .padding(16.dp),
            activeColor = White
        )

        val scope = rememberCoroutineScope()

        Button(
            onClick = {
                if (pagerState.currentPage == viewModel.pages.lastIndex) {
                    navController.navigate("login") {
                        popUpTo("walkthrough") { inclusive = true }
                    }
                } else {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = if (pagerState.currentPage == viewModel.pages.lastIndex) "Mulai" else "Lanjut",
                fontSize = 18.sp
            )
        }
    }
}
