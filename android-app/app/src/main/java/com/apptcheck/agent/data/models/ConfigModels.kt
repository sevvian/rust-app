package com.apptcheck.agent.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Museum(val name: String, val slug: String, val museumid: String)

@Serializable
data class Site(
    val name: String,
    val baseurl: String,
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
data class AppConfig(
    val sites: Map<String, Site> = emptyMap(),
    val active_site: String = "spl",
    val credentials: Map<String, Credential> = emptyMap(),
    val selected_credential: String = ""
)
