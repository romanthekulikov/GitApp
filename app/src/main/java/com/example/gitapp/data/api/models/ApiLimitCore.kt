package com.example.gitapp.data.api.models

import com.example.gitapp.entity.LimitCore
import com.squareup.moshi.Json

data class ApiLimitCore(
    @Json(name = "reset")
    override val resetTime: Long
) : LimitCore
