package services.aircraftService.validation

import feature.checklistDetails.model.CHECKLIST_LINE
import feature.checklists.model.Aircraft
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

data class AircraftValidationResult(val aircraft: Aircraft?, val errors: List<String>) {
    val isValid: Boolean get() = aircraft != null && errors.isEmpty()
}

/**
 * Validates a single aircraft record: strict schema decoding plus semantic rules.
 * Used both at import time (before anything is written) and at load time
 * (to decide whether an app file is usable or must fall back to resources).
 */
object AircraftValidator {

    val strictJson = Json {
        ignoreUnknownKeys = false
        isLenient = false
    }

    fun validate(jsonText: String): AircraftValidationResult =
        try {
            val aircraft = strictJson.decodeFromString<Aircraft>(jsonText)
            val errors = semanticErrors(aircraft)
            AircraftValidationResult(if (errors.isEmpty()) aircraft else null, errors)
        } catch (e: SerializationException) {
            AircraftValidationResult(null, listOf("Invalid aircraft file: ${e.message}"))
        } catch (e: IllegalArgumentException) {
            AircraftValidationResult(null, listOf("Invalid aircraft file: ${e.message}"))
        }

    fun semanticErrors(aircraft: Aircraft): List<String> {
        val errors = mutableListOf<String>()
        if (aircraft.name.isBlank()) errors += "Aircraft name must not be empty"
        with(aircraft.performance) {
            if (fuelCapacity <= 0f) errors += "fuelCapacity must be positive"
            if (averageCruiseSpeed <= 0f) errors += "averageCruiseSpeed must be positive"
            if (averageFuelFlow <= 0f) errors += "averageFuelFlow must be positive"
            if (taxiFuel < 0f) errors += "taxiFuel must not be negative"
            if (contingency < 0) errors += "contingency must not be negative"
            if (reservesMinutes < 0) errors += "reservesMinutes must not be negative"
        }
        val seenIds = mutableSetOf<Int>()
        aircraft.checklists.forEach { checklist ->
            if (!seenIds.add(checklist.id)) errors += "Duplicate checklist id ${checklist.id}"
            if (checklist.caption.isBlank()) errors += "Checklist ${checklist.id} has an empty caption"
            checklist.items.forEachIndexed { index, item ->
                val where = "Checklist ${checklist.id}, item ${index + 1}"
                if (item.caption.isBlank()) errors += "$where: caption must not be empty"
                // LINE separator items are caption-only by catalog convention.
                if (item.caption != CHECKLIST_LINE && item.action.isBlank())
                    errors += "$where (${item.caption}): action must not be empty"
            }
        }
        return errors
    }
}
