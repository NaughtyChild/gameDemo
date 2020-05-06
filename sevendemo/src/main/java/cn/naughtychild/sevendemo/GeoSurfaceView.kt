package cn.naughtychild.sevendemo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import uk.co.geolib.geolib.C2DArc
import uk.co.geolib.geolib.C2DLine
import uk.co.geolib.geolib.C2DPoint
import uk.co.geolib.geopolygons.C2DHoledPolygon
import uk.co.geolib.geopolygons.C2DPolygon

class GeoSurfaceView : SurfaceView, SurfaceHolder.Callback {
    val outerRectData = arrayOf(300, 300, 600, 300, 600, 600, 300, 600)
    val innerRectData = arrayOf(450, 300, 600, 450, 450, 600, 300, 450)
//    val innerRectData = arrayOf(300, 300, 600, 300, 600, 600, 300, 600)

    lateinit var geoThread: GeoThread

    init {
        holder.addCallback(this)
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        geoThread = GeoThread(holder!!)
        geoThread.start()
    }

    fun drawView(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)
        val outPolygon = createPolygon(outerRectData)
        val innerPolygon = createPolygon(innerRectData)
        val holePolygon = C2DHoledPolygon(outPolygon)
        holePolygon.AddHole(innerPolygon)
        drawFiled(holePolygon, Color.BLUE, canvas)
//        drawPolygon(innerPolygon, Color.BLUE, canvas)
    }

    private fun drawFiled(c2DHoledPolygon: C2DHoledPolygon, color: Int, canvas: Canvas) {
        val paint = Paint()
        paint.color = color
        if (c2DHoledPolygon.getRim().lines.size == 0) return
        val path = createPath(c2DHoledPolygon.rim)
        c2DHoledPolygon.holeCount
        for (i in 0 until c2DHoledPolygon.holeCount) {
            if (c2DHoledPolygon.GetHole(i).lines.size > 2) {
                path.addPath(createPath(c2DHoledPolygon.GetHole(i)))
            }
        }
        path.fillType = Path.FillType.EVEN_ODD
        canvas.drawPath(path, paint)
    }

    private fun drawPolygon(c2DHoledPolygon: C2DPolygon, color: Int, canvas: Canvas) {
        val paint = Paint()
        paint.color = color
        if (c2DHoledPolygon.lines.size == 0) return
        val path = createPath(c2DHoledPolygon)
        path.fillType = Path.FillType.EVEN_ODD
        canvas.drawPath(path, paint)
    }

    private fun createPath(poly: C2DPolygon): Path {
        val path: Path = Path()
        if (poly.lines.size == 0) {
            return path//返回空路线
        }
        val firstPoint = poly.lines.get(0).GetPointFrom()
        path.moveTo(firstPoint.x.toFloat(), firstPoint.y.toFloat())
        poly.lines.forEachIndexed { index, line ->
            when (line) {
                is C2DLine -> {
                    val ptTo = line.GetPointTo()
                    path.lineTo(ptTo.x.toFloat(), ptTo.y.toFloat())
                }
                is C2DArc -> {
                    val arc = line as C2DArc
                    val mid = arc.GetMidPoint()
                    val pyto = arc.GetPointTo()
                    path.quadTo(
                        mid.x.toFloat(),
                        mid.y.toFloat(),
                        pyto.x.toFloat(),
                        pyto.y.toFloat()
                    )
                }
            }
        }
        path.close()
        return path
    }

    private fun createPolygon(array: Array<Int>): C2DPolygon {
        val c2DPointList1 = ArrayList<C2DPoint>()
        for (i in array.indices step 2) {
            val c2DPoint = C2DPoint(array[i].toDouble(), array[i + 1].toDouble())
            c2DPointList1.add(c2DPoint)
        }
//        val c2DPointList = array.run {
//            val pointList = ArrayList<C2DPoint>()
//            this.forEachIndexed { index, value ->
//                if (index >= this.size - 1) return@forEachIndexed
//                val c2dPoint = C2DPoint(value.toDouble(), array.get(index + 1).toDouble())
//                pointList.add(c2dPoint)
//            }
//            return@run pointList
//        }
        val c2DPolygon = C2DPolygon()
        c2DPolygon.Create(c2DPointList1, true)
        return c2DPolygon
    }

    inner class GeoThread(val holder: SurfaceHolder) : Thread() {
        var flag = true
        override fun run() {
            super.run()
            while (flag) {
                val canvas = holder.lockCanvas()
                drawView(canvas)
                holder.unlockCanvasAndPost(canvas)
                sleep(500)
            }
        }
    }
}


