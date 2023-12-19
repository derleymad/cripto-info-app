package com.github.derleymad.lizwallet.utils.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.github.derleymad.lizwallet.R
import com.github.derleymad.lizwallet.model.market.MarketData

class LineChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val dataPoints = mutableListOf<MarketData>()

    private val VERTICAL_SCALE = 0.9f // Fator de escala vertical
    private val MIN_HEIGHT = 200f // Altura mínima do gráfico

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawLineChart(canvas)
    }

    private fun drawLineChart(canvas: Canvas) {
        if (dataPoints.size < 2) {
            return  // Pelo menos dois pontos são necessários para desenhar uma linha
        }

        val paint = Paint()
        paint.color = resources.getColor(R.color.green)
        paint.strokeWidth = 4f

        val maxX = dataPoints.size - 1
        val maxY = dataPoints.map { it.market_cap.toFloat() }.maxOrNull() ?: 0f

        val width = width.toFloat()
        val height = height.toFloat()

        val pointWidth = width / maxX
        val scaleY = (height - MIN_HEIGHT) / maxY * VERTICAL_SCALE // Fator de escala vertical

        // Calcula o deslocamento vertical para centralizar o gráfico
        val verticalOffset = (height - (maxY * scaleY)) / 2

        for (index in 0 until maxX) {
            val currentPoint = dataPoints[index]
            val nextPoint = dataPoints[index + 1]

            val x1 = index * pointWidth
            val y1 = height - (currentPoint.market_cap.toFloat() * scaleY) - verticalOffset

            val x2 = (index + 1) * pointWidth
            val y2 = height - (nextPoint.market_cap.toFloat() * scaleY) - verticalOffset

            canvas.drawLine(x1, y1, x2, y2, paint)
        }
    }

    fun setDataPoints(dataPoints: List<MarketData>) {
        this.dataPoints.clear()
        this.dataPoints.addAll(dataPoints)
        invalidate()
    }
}
