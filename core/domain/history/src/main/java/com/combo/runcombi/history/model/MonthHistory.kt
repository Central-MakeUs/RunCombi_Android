package com.combo.runcombi.history.model

data class MonthHistory(
    val avgCal: Int,
    val avgTime: Int,
    val avgDistance: Double,
    val monthData: List<RunDate>,
    val mostRunStyle: String
)

data class RunDate(
    val date: String,
    val runId: List<Int>
)