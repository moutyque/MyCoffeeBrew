package org.moutyque

import Minus
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.moutyque.ui.theme.MyCofeeBrewTheme
import androidx.compose.material.icons.filled.Add

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyCofeeBrewTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TabScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}


@Composable
fun BrewingGuide(modifier: Modifier = Modifier) {
    val steps = remember { mutableStateOf(listOf<BrewingStepData>()) }
    var coffeeQuantity by remember { mutableIntStateOf(15) }

    // Initialize the steps

    steps.value = (1..7).map {
        getBrewingStepData(it, coffeeQuantity)
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .verticalScroll(rememberScrollState())
    ) {
        GoodsHeader(
            coffeeQuantity = coffeeQuantity,
            onCoffeeQuantityChanged = { newQuantity ->
                coffeeQuantity = newQuantity
            },
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            steps.value.forEachIndexed { index, stepState ->
                BrewingStep(
                    stepData = stepState,
                    onExpand = {
                        steps.value.forEach { it.isExpanded.value = false }
                        stepState.isExpanded.value = true
                    },
                    onCollapse = {
                        steps.value.forEach { it.isExpanded.value = false }
                        // Expand the next step
                        val nextStepIndex = (index + 1) % 7
                        if (nextStepIndex != -1) {
                            steps.value[nextStepIndex].isExpanded.value = true
                        }
                    },
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                if (stepState.stepNumber < 7) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }
            Spacer(modifier = Modifier.padding(bottom = 16.dp))
        }
    }
}

@Composable
fun QuantitySelector(
    quantity: Int,
    onQuantityChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    minQuantity: Int = 1,
    maxQuantity: Int = 100
) {
    var currentQuantity by remember { mutableIntStateOf(quantity) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IconButton(
            onClick = {
                if (currentQuantity > minQuantity) {
                    currentQuantity--
                    onQuantityChanged(currentQuantity)
                }
            },
            enabled = currentQuantity > minQuantity
        ) {
            Icon(Minus, contentDescription = "Decrease quantity")
        }

        Text(
            text = currentQuantity.toString(),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.width(32.dp),
        )

        IconButton(
            onClick = {
                if (currentQuantity < maxQuantity) {
                    currentQuantity++
                    onQuantityChanged(currentQuantity)
                }
            },
            enabled = currentQuantity < maxQuantity
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Increase quantity")
        }
    }
}

@Composable
fun GoodsHeader(
    coffeeQuantity: Int,
    onCoffeeQuantityChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val waterQuantity = coffeeQuantity * 16
    val extraWaterQuantity = coffeeQuantity * 2

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.goods_title),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Use QuantitySelector instead of the dropdown
        QuantitySelector(
            quantity = coffeeQuantity,
            onQuantityChanged = onCoffeeQuantityChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Text(
            text = stringResource(id = R.string.goods_coffee, coffeeQuantity),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = stringResource(id = R.string.goods_water, waterQuantity),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = stringResource(id = R.string.goods_grinder),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = stringResource(id = R.string.goods_extra_water, extraWaterQuantity),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 4.dp)
        )
    }
}

@Composable
fun TabScreen(modifier: Modifier = Modifier) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("V60", "About", "Settings")
    val tabContent = listOf<@Composable (Modifier) -> Unit>(
        { BrewingGuide(modifier = Modifier.padding(16.dp)) },
        { Text(text = "About", modifier = Modifier.padding(16.dp)) },
        { Text(text = "Settings", modifier = Modifier.padding(16.dp)) }
    )
    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index }
                )
            }
        }
        when (tabIndex) {
            0 -> tabContent[0](modifier)
            1 -> tabContent[1](modifier)
            2 -> tabContent[2](modifier)
        }
    }
}

@Composable
fun getBrewingStepData(stepNumber: Int, coffeeQuantity: Int = 15): BrewingStepData =
    when (stepNumber) {
        1 -> BrewingStepData(
            stepNumber = stepNumber,
            stepText = stringResource(id = R.string.v60_step_1_action),
            isExpanded = remember { mutableStateOf(false) },
            commentText = stringResource(id = R.string.v60_step_1_comment)
        )

        2 -> BrewingStepData(
            stepNumber = stepNumber,
            stepText = stringResource(id = R.string.v60_step_2_action, coffeeQuantity),
            isExpanded = remember { mutableStateOf(false) },
            commentText = stringResource(id = R.string.v60_step_2_comment)
        )

        3 -> BrewingStepData(
            stepNumber = stepNumber,
            stepText = stringResource(
                id = R.string.v60_step_3_action,
                coffeeQuantity * 50 / 15
            ),
            isExpanded = remember { mutableStateOf(false) },
            commentText = stringResource(id = R.string.v60_step_3_comment)
        )

        4 -> BrewingStepData(
            stepNumber = stepNumber,
            stepText = stringResource(id = R.string.v60_step_4_action, coffeeQuantity),
            isExpanded = remember { mutableStateOf(false) },
            commentText = stringResource(id = R.string.v60_step_4_comment)
        )

        5 -> BrewingStepData(
            stepNumber = stepNumber,
            stepText = stringResource(
                id = R.string.v60_step_5_action,
                coffeeQuantity * 250 / 15
            ),
            isExpanded = remember { mutableStateOf(false) },
            commentText = stringResource(id = R.string.v60_step_5_comment)
        )

        6 -> BrewingStepData(
            stepNumber = stepNumber,
            stepText = stringResource(id = R.string.v60_step_6_action, coffeeQuantity),
            isExpanded = remember { mutableStateOf(false) },
            commentText = stringResource(id = R.string.v60_step_6_comment)
        )

        7 -> BrewingStepData(
            stepNumber = stepNumber,
            stepText = stringResource(id = R.string.v60_step_7_action, coffeeQuantity),
            isExpanded = remember { mutableStateOf(false) },
            commentText = stringResource(id = R.string.v60_step_7_comment)
        )

        else -> throw IllegalStateException("Invalid step number")
    }



