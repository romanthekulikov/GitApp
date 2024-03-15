package com.example.gitapp.data.api.models

import com.example.gitapp.entity.RateLimit
import com.squareup.moshi.Json

data class ApiRateLimit(
    @Json(name = "resources")
    override val limitCore: ApiLimitResource
) : RateLimit