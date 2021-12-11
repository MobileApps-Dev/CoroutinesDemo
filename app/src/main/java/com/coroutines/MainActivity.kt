package com.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.coroutines.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var count = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.btnDownload.setOnClickListener {
//            CoroutineScope(Dispatchers.IO).launch {
//                downloadUserData()
//            }
            CoroutineScope(Dispatchers.Main).launch {
                //UnStructured Concurrency
             //   txt_downloadcount.text = UserDataManager1().getTotalUserCount().toString()

                //Structured Concurrency
                txt_downloadcount.text = UserDataManager2().getTotalUserCount().toString()
            }


        }

        binding.btnCount.setOnClickListener {
            binding.txtCount.text = count++.toString()
        }

        //Background Thread
        CoroutineScope(Dispatchers.IO).launch {
            Log.i("MyTag", "Hello From ${Thread.currentThread().name} ")
        }

        //Foreground Thread
        CoroutineScope(Dispatchers.Main).launch {
            Log.i("MyTag", "Hello From ${Thread.currentThread().name} ")
        }

        // Call Parallel function
        CoroutineScope(Dispatchers.IO).launch {
            Log.i("MyTag", "Start ... ")
            var first = async { getStock1() }
            var second = async { getStock2() }
            var total = first.await() + second.await()
            Log.i("MyTag", "Total is $total")
        }
    }

    // switch Thread IO to Main
    private suspend fun downloadUserData() {
        for (i in 1..20000) {
            Log.i("MyTag", "Downloading user $i in ${Thread.currentThread().name} ")
            withContext(Dispatchers.Main) {
                txt_downloadcount.text = "Downloading user $i in ${Thread.currentThread().name} "
            }

        }


    }
}

private suspend fun getStock1(): Int {
    delay(10000)
    Log.i("MyTag", "Stock 1 Returned")
    return 5500
}

private suspend fun getStock2(): Int {
    delay(8000)
    Log.i("MyTag", "Stock 2 Returned")
    return 3500
}