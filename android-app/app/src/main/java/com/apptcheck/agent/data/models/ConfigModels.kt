package com.apptcheck.agent.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Museum(
    val name: String, 
    val slug: String, 
    val museumid: String
)

@Serializable
data class Site(
    val name: String,
    val baseurl: String,
    val availabilityendpoint: String,
    val digital: Boolean,
    val physical: Boolean,
    val location: String,
    val bookinglinkselector: String,
    val successindicator: String,
    val museums: Map<String, Museum> = emptyMap(),
    val preferredslug: String = ""
)

@Serializable
data class Credential(
    val id: String,
    val name: String,
    val username: String,
    val password: String,
    val email: String,
    val site: String
)

@Serializable
data class ScheduledRun(
    val id: String,
    val sitekey: String,
    val museumslug: String,
    val droptime: String, // ISO-8601 UTC String
    val mode: String,
    val credentialid: String
)

@Serializable
data class AppConfig(
    val sites: Map<String, Site> = emptyMap(),
    val active_site: String = "spl",
    val mode: String = "alert",
    val preferred_days: List<String> = emptyList(),
    val strike_time: String = "09:00",
    val check_window_ms: Long = 60000,
    val check_interval_ms: Long = 2000,
    val pre_warm_offset_ms: Long = 30000,
    val ntfy_topic: String = "myappointments",
    val max_workers: Int = 2,
    val request_jitter_ms: Long = 2000,
    val months_to_check: Int = 2,
    val scheduled_runs: List<ScheduledRun> = emptyList(),
    val rest_cycle_checks: Int = 20,
    val rest_cycle_duration_ms: Long = 3000,
    val credentials: Map<String, Credential> = emptyMap(),
    val selected_credential: String = ""
)
