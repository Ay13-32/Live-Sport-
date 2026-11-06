MainActivity.ktpackage com.ayman.screenstream

import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var projectionManager: MediaProjectionManager
    private val REQUEST_CODE_SCREEN_CAPTURE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        projectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

        val btnStart = findViewById<Button>(R.id.btnStartStream)
        btnStart.setOnClickListener {
            // طلب إذن التقاط الشاشة رسمياً من نظام أندرويد
            startActivityForResult(
                projectionManager.createScreenCaptureIntent(),
                REQUEST_CODE_SCREEN_CAPTURE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SCREEN_CAPTURE && resultCode == RESULT_OK && data != null) {
            // هنا تم منح الإذن بنجاح! نبدأ خدمة البث في الخلفية
            val serviceIntent = Intent(this, ScreenCaptureService::class.java).apply {
                putExtra("RESULT_CODE", resultCode)
                putExtra("DATA", data)
            }
            startService(serviceIntent)
        }
    }
}
