package app.com.savelocation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.savelocation.MainActivity
import kotlinx.android.synthetic.main.saved.*

class saved : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.saved)
        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    private fun showMessage(string: String) {
      val container = findViewById<View>(R.id.tvResult)
      if (container != null) {
         Toast.makeText(this@saved, string, Toast.LENGTH_LONG).show()
      }
   }


}