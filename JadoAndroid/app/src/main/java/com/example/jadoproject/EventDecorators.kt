package com.example.jadoproject

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class EventDecorators(private val context: Context,
                    private val stringProductColor : Array<String>, private val dates : CalendarDay
) : DayViewDecorator {

    private lateinit var colors : IntArray

    override fun shouldDecorate(day: CalendarDay?): Boolean = dates == day

    override fun decorate(view: DayViewFacade?) {
        colors = IntArray(stringProductColor.size)
        for (i in stringProductColor.indices)
            colors[i] = Color.parseColor(stringProductColor[i])

        view?.addSpan(CustomMultipleSpan(5f, colors))

    }


}
