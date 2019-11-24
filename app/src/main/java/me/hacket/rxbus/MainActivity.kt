package me.hacket.rxbus

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import me.hacket.assistant.rxbus.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//
//        RxBus.getDefault<String>().receive(this, "tag1", {
//
//        })
//
//        btn_post_no_value.setOnClickListener {
//            RxBus.getDefault<String>().post("tag1")
//        }
    }
}
