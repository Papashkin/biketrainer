package com.antsfamily.biketrainer.ui.createworkout.view

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SwipeableCounter(
    width: Dp,
    itemHeight: Dp,
    items: List<Int>,
    initialItem: Int,
    onItemSelected: (item: Int) -> Unit = {}
) {
    val numberOfDisplayedItems = 3

    val scrollState = rememberLazyListState(0)
    var lastSelectedIndex by remember { mutableIntStateOf(0) }
    val itemsState by remember { mutableStateOf(items) }

    LaunchedEffect(initialItem) {
        var targetIndex = items.indexOf(initialItem)
        targetIndex += ((Int.MAX_VALUE / 2) / items.size) * items.size
        lastSelectedIndex = targetIndex
        scrollState.scrollToItem(targetIndex)
        scrollState.scrollToItem(targetIndex - (numberOfDisplayedItems / 2))
    }

    LazyColumn(
        modifier = Modifier
            .width(width)
            .height(itemHeight * numberOfDisplayedItems),
        state = scrollState,
        flingBehavior = rememberSnapFlingBehavior(
            lazyListState = scrollState
        )
    ) {
        items(
            count = Int.MAX_VALUE,
            itemContent = { i ->
                val item = itemsState[i % itemsState.size]
                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.toString(),
                        color = if (lastSelectedIndex == i) {
                            MaterialTheme.colorScheme.inverseSurface
                        } else {
                            MaterialTheme.colorScheme.outlineVariant
                        }
                    )
                }
            }
        )
    }

    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.isScrollInProgress }
            .collect { isScrolling ->
                if (!isScrolling) {
                    val centerIndex = scrollState.firstVisibleItemIndex + (numberOfDisplayedItems / 2)
                    val item = itemsState[centerIndex % itemsState.size]
                    if (lastSelectedIndex != centerIndex) {
                        onItemSelected(item)
                        lastSelectedIndex = centerIndex
                    }
                }
            }
    }
}

@Preview(showBackground = true)
@Composable
private fun TestViewPreview() {
    SwipeableCounter(200.dp, 32.dp, items = (0..59 step 5).toList(), initialItem = 55)
}
