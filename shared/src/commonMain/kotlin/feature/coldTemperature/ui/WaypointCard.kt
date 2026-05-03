package feature.coldTemperature.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import feature.coldTemperature.ui.model.ColdTemperatureWaypoint
import org.jetbrains.compose.resources.painterResource
import services.coldTemperature.WaypointSegment
import sims_checklist.shared.generated.resources.Res
import sims_checklist.shared.generated.resources.close

@Composable
fun WaypointCard(
    idx: Int,
    waypoint: ColdTemperatureWaypoint,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (waypoint.isTemporary) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${idx + 1}.",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.width(8.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = waypoint.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(2.dp))
                Surface(
                    color = when (waypoint.segment) {
                        WaypointSegment.ABOVE_FAF -> MaterialTheme.colorScheme.primaryContainer
                        WaypointSegment.BELOW_FAF -> MaterialTheme.colorScheme.tertiaryContainer
                    },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = waypoint.segment.displayName,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            Spacer(Modifier.width(12.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${waypoint.altitudeFeet} ft",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
                Text(
                    text = "→ ${waypoint.correctedAltitudeFeet} ft",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
            
            if (!waypoint.isTemporary) {
                IconButton(onClick = onDelete) {
                    Icon(
                        painter = painterResource(Res.drawable.close),
                        contentDescription = "Delete waypoint"
                    )
                }
            }
        }
    }
}