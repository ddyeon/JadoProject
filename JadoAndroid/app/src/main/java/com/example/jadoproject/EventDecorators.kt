/*
package com.example.jadoproject

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

@SuppressLint("UseCompatLoadingForDrawables")
class EventDecorators(color: Int, dates: Collection<CalendarDay?>?, context: Context) :
    DayViewDecorator {
    private val drawable: Drawable = context.resources.getDrawable(R.drawable.alert_dark_frame)
    private val color: Int = color
    private val dates: HashSet<CalendarDay?> = HashSet(dates)
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        view.setSelectionDrawable(drawable)
        //view.addSpan(new DotSpan(5, color)); // 날자밑에 점
    }

}*/
