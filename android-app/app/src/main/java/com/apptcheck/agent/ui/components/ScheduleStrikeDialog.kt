package com.apptcheck.agent.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.*
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleStrikeDialog(
    onDismiss: () -> Unit,
    onConfirm: (LocalDateTime, ZoneId, String) -> Unit
) {
    var date by remember { mutableStateOf(LocalDate.now()) }
    var time by remember { mutableStateOf(LocalTime.of(9, 0)) }
    var selectedZone by remember { mutableStateOf(ZoneId.systemDefault()) }
    var mode by remember { mutableStateOf("alert") }
    
    val zones = listOf(
        ZoneId.of("America/Los_Angeles"),
        ZoneId.of("America/Denver"),
        ZoneId.of("America/Chicago"),
        ZoneId.of("America/New_York"),
        ZoneId.of("UTC")
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Schedule Future Strike") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Target Drop Time", style = MaterialTheme.typography.labelMedium)
                
                // Simplified Date/Time Selection for logic demonstration
                // In production, use DatePicker/TimePicker components
                OutlinedTextField(
                    value = date.toString(),
                    onValueChange = { try { date = LocalDate.parse(it) } catch(e: Exception) {} },
                    label = { Text("Date (YYYY-MM-DD)") }
                )
                OutlinedTextField(
                    value = time.format(DateTimeFormatter.ofPattern("HH:mm")),
                    onValueChange = { try { time = LocalTime.parse(it) } catch(e: Exception) {} },
                    label = { Text("Time (HH:mm)") }
                )

                Text("Timezone", style = MaterialTheme.typography.labelMedium)
                zones.forEach { zone ->
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        RadioButton(selected = selectedZone == zone, onClick = { selectedZone = zone })
                        Text(zone.id, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                Text("Mode", style = MaterialTheme.typography.labelMedium)
                Row {
                    FilterChip(selected = mode == "alert", onClick = { mode = "alert" }, label = { Text("Alert Only") })
                    Spacer(modifier = Modifier.width(8.dp))
                    FilterChip(selected = mode == "booking", onClick = { mode = "booking" }, label = { Text("Auto-Book") })
                }
            }
        },
        confirmButton = {
            Button(onClick = { 
                onConfirm(LocalDateTime.of(date, time), selectedZone, mode)
            }) { Text("Confirm Schedule") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
