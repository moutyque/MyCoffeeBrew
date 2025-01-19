package org.moutyque

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class BrewingStepData(
    val stepNumber: Int,
    val stepText: String,
    val commentText: String
)

@Composable
fun BrewingStep(
    stepData: BrewingStepData,
    isExpanded: Boolean,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpandedState by remember { mutableStateOf(isExpanded) }
    val customFontFamily = FontFamily.Serif

    Column(modifier = modifier.animateContentSize(tween(durationMillis = 300))) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(id = R.string.step_title, stepData.stepNumber),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = customFontFamily,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            IconButton(onClick = {
                isExpandedState = !isExpandedState
                if (isExpandedState) {
                    onExpand()
                } else {
                    onCollapse()
                }
            }) {
                Icon(
                    imageVector = if (isExpandedState) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Expand/Collapse"
                )
            }
        }
        AnimatedVisibility(visible = isExpandedState) {
            Column {
                Text(
                    text = stepData.stepText,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = customFontFamily,
                    ),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = stepData.commentText,
                    style = MaterialTheme.typography.bodyMedium.copy(fontFamily = customFontFamily),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }
}