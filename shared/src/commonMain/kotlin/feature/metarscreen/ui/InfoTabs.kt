package feature.metarscreen.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import feature.metarscreen.model.DatisUi
import feature.metarscreen.model.MetarUi
import org.jetbrains.compose.resources.painterResource
import sims_checklist.shared.generated.resources.Res
import sims_checklist.shared.generated.resources.atis_arrival
import sims_checklist.shared.generated.resources.atis_departure
import services.atisService.model.AtisType
import services.atisService.model.DatisEntry

private enum class InfoTab(val caption: String) {
    METAR("METAR"),
    TAF("TAF"),
    DATIS("D-ATIS");
}

@Composable
fun InfoTabs(
    metarUi: MetarUi,
    datisUi: DatisUi?,
    isLoadingAtis: Boolean,
    selectedTab: Int,
    onSelectTab: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val hasMetar = metarUi.rawMetar.isNotBlank()
    val hasTaf = metarUi.rawTaf.isNotBlank()
    val hasDatis = datisUi != null && datisUi.entries.isNotEmpty()

    val badges = listOf(hasMetar, hasTaf, hasDatis)

    Column(modifier = modifier.fillMaxWidth()) {
        PrimaryTabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier.fillMaxWidth()
        ) {
            InfoTab.entries.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { onSelectTab(index) },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(tab.caption)
                            if (badges[index]) {
                                Spacer(Modifier.width(4.dp))
                                Badge(containerColor = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                )
            }
        }

        when (selectedTab) {
            InfoTab.METAR.ordinal -> MetarTabContent(metarUi.rawMetar)
            InfoTab.TAF.ordinal -> MetarTabContent(metarUi.rawTaf)
            InfoTab.DATIS.ordinal -> DatisTabContent(datisUi, isLoadingAtis)
        }
    }
}

@Composable
private fun MetarTabContent(text: String) {
    SelectionContainer {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = text.ifBlank { "No data available" },
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
private fun DatisTabContent(
    datisUi: DatisUi?,
    isLoading: Boolean
) {
    when {
        isLoading -> {
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(modifier = Modifier.size(32.dp))
            }
        }
        datisUi == null -> {
            SelectionContainer {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "No D-ATIS available for this station (US only)",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
        datisUi.error != null -> {
            SelectionContainer {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = datisUi.error,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Tap to retry",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }
        datisUi.entries.isEmpty() -> {
            SelectionContainer {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "No D-ATIS available for this station (US only)",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
        else -> {
            SelectionContainer {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    datisUi.entries.forEachIndexed { index, entry ->
                        if (index > 0) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                        DatisEntryContent(entry)
                    }
                }
            }
        }
    }
}

@Composable
private fun DatisEntryContent(entry: DatisEntry) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DatisTypeIcon(entry.type)
            DatisTypeBadge(entry.type)
            Text(
                text = entry.code,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${entry.time}Z",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = entry.datis,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 18.sp
        )
    }
}

@Composable
private fun DatisTypeIcon(type: AtisType) {
    if (type == AtisType.COMBINED) return
    val (iconRes, contentDesc) = when (type) {
        AtisType.ARRIVAL -> Res.drawable.atis_arrival to "Arrival ATIS"
        AtisType.DEPARTURE -> Res.drawable.atis_departure to "Departure ATIS"
    }
    val tintColor = MaterialTheme.colorScheme.onSurface
    Icon(
        painter = painterResource(iconRes),
        contentDescription = contentDesc,
        tint = tintColor,
        modifier = Modifier.size(20.dp)
    )
}

@Composable
private fun DatisTypeBadge(type: AtisType) {
    val (label, containerColor, contentColor) = when (type) {
        AtisType.COMBINED -> Triple("COMBINED", MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer)
        AtisType.ARRIVAL -> Triple("ARR", MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.onPrimaryContainer)
        AtisType.DEPARTURE -> Triple("DEP", MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onTertiaryContainer)
    }
    Surface(
        shape = CircleShape,
        color = containerColor,
        modifier = Modifier.height(20.dp)
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            color = contentColor,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            maxLines = 1
        )
    }
}
