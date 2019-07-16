package mindvalley.loader

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.widget.ProgressBar

class CircleProgressBar : ProgressBar {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var timer : CountDownTimer? = null
    fun start(){
        if(timer!=null){
            timer?.cancel()
            timer = null
        }
        timer = object: CountDownTimer(max.toLong(),(max.toLong().div(100))) {
            override fun onTick(millisUntilFinished: Long) {
                progress = max.toLong().minus(millisUntilFinished).toInt()
            }

            override fun onFinish() {
                progress = max
            }
        }
        timer?.start()
    }
}