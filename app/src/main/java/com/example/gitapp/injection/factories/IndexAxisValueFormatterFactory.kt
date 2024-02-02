package com.example.gitapp.injection.factories

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class IndexAxisValueFormatterFactory @AssistedInject constructor(
    @Assisted("barValueArrayString") private val barValueArrayString: Array<String>
) {
    fun createIndexAxisValueFormatter(): IndexAxisValueFormatter {
        return IndexAxisValueFormatter(barValueArrayString)
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("barValueArrayString") barValueArrayString: Array<String>): IndexAxisValueFormatterFactory
    }
}