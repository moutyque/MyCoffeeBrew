package org.moutyque

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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

@Composable
fun BrewingGuide(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        for (step in 1..7) {
            BrewingStep(step)
            if (step < 7) {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}

@Composable
fun BrewingStep(stepNumber: Int) {
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

    Column {
        Text(
            text = stringResource(id = R.string.step_title, stepNumber),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = stepText,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = commentText,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
    }
}
@Composable
fun TabScreen(modifier: Modifier = Modifier) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("V60", "About", "Settings")
    val tabContent =  listOf<@Composable (Modifier) -> Unit>(
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
            0 -> tabContent[0](Modifier)
            1 -> tabContent[1](Modifier)
            2 -> tabContent[2](Modifier)
        }
    }
}