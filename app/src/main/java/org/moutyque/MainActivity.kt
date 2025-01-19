package org.moutyque

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.moutyque.ui.theme.MyCofeeBrewTheme

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

// Define a data class to hold the state of each step
data class StepState(
    val stepNumber: Int,
    var isExpanded: Boolean = false,
    var isChecked: Boolean = false
)

@Composable
fun BrewingGuide(modifier: Modifier = Modifier) {
    val steps = remember { mutableStateOf(listOf<StepState>()) }
    var currentStep by remember { mutableIntStateOf(1) }
    var expandedStepIndex by remember { mutableIntStateOf(-1) }

    // Initialize the steps
    if (steps.value.isEmpty()) {
        steps.value = listOf(
            StepState(1, true, false),
            StepState(2, false, false),
            StepState(3, false, false),
            StepState(4, false, false),
            StepState(5, false, false),
            StepState(6, false, false),
            StepState(7, false, false)
        )
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .verticalScroll(rememberScrollState())
    ) {
        GoodsHeader(modifier = Modifier.background(MaterialTheme.colorScheme.surface))
        Column(modifier = Modifier.fillMaxWidth()) {
            steps.value.forEachIndexed { index, stepState ->
                BrewingStep(
                    stepData = getBrewingStepData(stepState.stepNumber),
                    isExpanded = stepState.isExpanded,
                    onExpand = {
                        // Collapse all other steps
                        val updatedSteps = steps.value.map {
                            if (it.stepNumber != stepState.stepNumber) {
                                it.copy(isExpanded = false)
                            } else {
                                it
                            }
                        }.toMutableList()
                        // Expand the current step
                        updatedSteps[index] = stepState.copy(isExpanded = true)
                        steps.value = updatedSteps.toList()
                        expandedStepIndex = index
                    },
                    onCollapse = {
                        // Collapse the current step
                        val updatedSteps = steps.value.map {
                            if (it.stepNumber != stepState.stepNumber) {
                                it.copy(isExpanded = false)
                            } else {
                                it
                            }
                        }.toMutableList()
                        // Expand the next step
                        val nextStepIndex = (index + 1) % 7
                        if (nextStepIndex != -1) {
                            val nextStep = steps.value.toMutableList()
                            nextStep[nextStepIndex] = steps.value[nextStepIndex].copy(
                                isExpanded = true
                            )
                            steps.value = nextStep.toList()
                        }
                        expandedStepIndex = -1
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
fun GoodsHeader(modifier: Modifier = Modifier) {
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
        Text(
            text = stringResource(id = R.string.goods_coffee),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = stringResource(id = R.string.goods_water),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = stringResource(id = R.string.goods_extra_water),
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
fun getBrewingStepData(stepNumber: Int): BrewingStepData {
    val stepText = when (stepNumber) {
        1 -> stringResource(id = R.string.v60_step_1_action)
        2 -> stringResource(id = R.string.v60_step_2_action)
        3 -> stringResource(id = R.string.v60_step_3_action)
        4 -> stringResource(id = R.string.v60_step_4_action)
        5 -> stringResource(id = R.string.v60_step_5_action)
        6 -> stringResource(id = R.string.v60_step_6_action)
        7 -> stringResource(id = R.string.v60_step_7_action)
        else -> ""
    }
    val commentText = when (stepNumber) {
        1 -> stringResource(id = R.string.v60_step_1_comment)
        2 -> stringResource(id = R.string.v60_step_2_comment)
        3 -> stringResource(id = R.string.v60_step_3_comment)
        4 -> stringResource(id = R.string.v60_step_4_comment)
        5 -> stringResource(id = R.string.v60_step_5_comment)
        6 -> stringResource(id = R.string.v60_step_6_comment)
        7 -> stringResource(id = R.string.v60_step_7_comment)
        else -> ""
    }
    return BrewingStepData(stepNumber = stepNumber, stepText = stepText, commentText = commentText)


}