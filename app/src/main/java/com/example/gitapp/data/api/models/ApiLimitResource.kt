package com.example.gitapp.data.api.models

import com.example.gitapp.entity.LimitResource
import com.squareup.moshi.Json

data class ApiLimitResource(
    @Json(name = "core")
    override val core: ApiLimitCore
): LimitResource
