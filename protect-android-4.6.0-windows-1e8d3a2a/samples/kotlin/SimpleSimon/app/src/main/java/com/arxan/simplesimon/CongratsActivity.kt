package com.arxan.simplesimon

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.arxan.simplesimon.databinding.ActivityCongratsBinding
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

class CongratsActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityCongratsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCongratsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        showKonfetti()
        Log.i("SimpleSimon", "Congratulations!")
    }

    private fun showKonfetti() {
        mBinding.contentCongrats.viewKonfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.Square, Shape.Circle)
                .addSizes(Size(12))
                .setPosition(-50f, mBinding.contentCongrats.viewKonfetti.width + 50f, -50f, -50f)
                .streamFor(300, 5000L)

        mBinding.contentCongrats.textView.text = stringFromJNI()
    }

    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
