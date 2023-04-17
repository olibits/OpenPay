package com.openpay.android.model.people

data class PeopleResult(
    val adult: Boolean? = false,
    val gender: Int? = 0,
    val id: Int? = 0,
    val known_for: List<KnownFor>? = listOf(),
    val known_for_department: String? = "",
    val name: String? = "",
    val popularity: Double? = 0.0,
    val profile_path: String? = ""
)