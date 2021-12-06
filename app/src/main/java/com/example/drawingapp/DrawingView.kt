package com.example.drawingapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Size
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var mDrawPath: CustomPath? = null
    private var mCanvasBitmap: Bitmap? = null
    private var mDrawPaint: Paint? = null
    private var mCanvasPaint : Paint? = null
    private var mBrushSize : Float = 0.toFloat()

    private var mBrushColor : Int = Color.BLUE

    private var canvas : Canvas? = null

    private var mPath = arrayListOf<CustomPath>()

    init {
        setUpDrawing()
    }

    fun setUpDrawing(){
        mDrawPaint = Paint()
        mDrawPath = CustomPath(mBrushColor, mBrushSize)
        mDrawPaint?.color = mBrushColor
        mDrawPaint?.style = Paint.Style.STROKE
        mDrawPaint?.strokeJoin = Paint.Join.ROUND

        mCanvasPaint = Paint(Paint.DITHER_FLAG)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(mCanvasBitmap!!)

    }

    override fun onDraw(canvas: Canvas){
        super.onDraw(canvas)

        mCanvasBitmap?.let{
            canvas?.drawBitmap(it, 0f, 0f, mCanvasPaint)
        }

        for(p in mPath){
            mDrawPaint?.strokeWidth = p.brushThickness
            mDrawPaint?.color = p.color
            canvas.drawPath(p,mDrawPaint!!)
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x
        val touchY = event?.y

        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                mDrawPath?.color = mBrushColor
                mDrawPath?.brushThickness = mBrushSize

                mDrawPath?. reset()

                if(touchX != null && touchY != null){
                    mDrawPath?.moveTo(touchX, touchY)
                }

            }

            MotionEvent.ACTION_UP -> {
                mPath.add(mDrawPath!!)
            }

            MotionEvent.ACTION_MOVE -> {
                mDrawPath?.lineTo(touchX!!, touchY!!)
            }
            else -> return false

        }

        invalidate()
        return true

    }

    fun setSizeForBrush(newSize: Float){
        mBrushSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, newSize, resources.displayMetrics)

        mDrawPaint?.strokeWidth = mBrushSize

    }

    fun setColor(newColor: String){
        mBrushColor = Color.parseColor(newColor)
        mDrawPaint?.color = mBrushColor
    }

    internal inner class CustomPath(var color: Int, var brushThickness: Float): Path()


}