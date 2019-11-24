package me.hacket.rxbus.app

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import me.hacket.rxbus.Action1
import me.hacket.rxbus.RxBus

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "hacket"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RxBus.getDefault<Any>().receive(
            this,
            "tag1",
            object : Action1<Any> {
                override fun onReceive(data: Any) {
                    Log.d(TAG, "receive1: tag=tag1, data=$data (${Thread.currentThread().name})")
                }
            }
        )
        RxBus.getDefault<Any>().receive(
            this,
            "tag1"
        ) {
            Log.d(TAG, "receive2: tag=tag1, data=$it (${Thread.currentThread().name})")
        }

        RxBus.getDefault<String>().receive(
            this,
            "tag2",
            object : Action1<String> {
                override fun onReceive(data: String) {
                    Log.d(TAG, "receive: tag=tag2, data=$data (${Thread.currentThread().name})")
                }
            }
        )
        RxBus.getDefault<String>().receive(
            this,
            "tag2",
            object : Action1<String> {
                override fun onReceive(data: String) {
                    Log.d(TAG, "receive: tag=tag2, data=$data (${Thread.currentThread().name})")
                }
            },
            Schedulers.io()
        )

        btn_post_no_value.setOnClickListener {
            RxBus.getDefault<String>().post("tag1")
        }
        btn_post_value.setOnClickListener {
            RxBus.getDefault<String>().post("tag2", "a test value")
        }

        RxBus.getDefault<Int>().postSticky("tag10", 10086)
        btn_receive_sticky.setOnClickListener {
            RxBus.getDefault<Int>().receiveSticky(this, "tag10") {
                Log.i(
                    TAG,
                    "receiveSticky: tag=tag10, data=$it (${Thread.currentThread().name})"
                )
            }
        }
    }
}
