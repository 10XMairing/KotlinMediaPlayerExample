package com.andevs.tenx.mediaplayerexample

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_home.*
import java.io.File
import java.util.*

class HomeActivity : AppCompatActivity() {



    var adapter : SongAdapter ? = null
    var songs : ArrayList<HashMap<String, String>> = ArrayList()
    var mplayer : MediaPlayer ? = null
    var recentSong = -1
    lateinit var  handler : Handler
    lateinit var  mRunnable : Runnable




    private val REQUEST_CODE = 1122

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            } else {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
            }
        }

        handler = Handler()

        val extStorage = Environment.getExternalStorageDirectory()
        val dir = File(extStorage.absolutePath + "/Music")

        if (dir.exists()) {

            val files = dir.listFiles()
            for (f in files) {
                Log.d("TEST", f.name)
                var data: HashMap<String, String> = HashMap()
                data.put("file_name", f.name)
                data.put("file_path", f.absolutePath)

                songs.add(data)
            }
            Log.d("TEST", "size songs -> " + songs.size)
        }

            var listener: CustomItemClickListener = object : CustomItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                val myUri: Uri = Uri.parse(songs[position]["file_path"])
                stopAndRelease()
                mplayer  = MediaPlayer().apply {
                    setDataSource(applicationContext, myUri)
                    prepare()
                    start()
                    }
                    playCycle()
                    recentSong = position


                 }
            }


            adapter = SongAdapter(this,songs, listener)
            recycler_songs.layoutManager = LinearLayoutManager(this)
            recycler_songs.adapter = adapter


        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
               if(b){
                   Log.d("TEST", "seeking to position : $i")
                   mplayer?.seekTo(i)
               }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something

            }
        })



        

            img_stop.setOnClickListener{
               stopAndRelease()
            }

            img_pause.setOnClickListener{
                pauseSong()


            }
            img_play.setOnClickListener{
                playSong()
                playCycle()


        }


    }

    fun stopAndRelease(){
       if(mplayer != null){
           seekbar.progress = 0
           handler.removeCallbacks(mRunnable)
           mplayer?.stop()
           mplayer?.release()
           mplayer = null
       }
    }

    fun playCycle(){
        seekbar.max = mplayer!!.duration
        if(mplayer!!.isPlaying){
              mRunnable =   Runnable {
                  val currentPos = mplayer!!.currentPosition
                  seekbar.progress = currentPos
                  handler.postDelayed(mRunnable, 1000)
                  Log.d("TEST", ""+currentPos)
                }
            handler.postDelayed(mRunnable, 1000)

        }
    }

    fun playSong(){
        if(mplayer == null){
            if(songs.size != 0){
                var pos = 0
                if(recentSong != -1){
                    pos = recentSong
                }
                mplayer = MediaPlayer().apply {
                    setDataSource(applicationContext, Uri.parse(songs[pos]["file_path"]))
                    prepare()
                    start()
                }

            }
        }else{
            mplayer?.start()
        }
    }

    fun pauseSong(){
        mplayer?.pause()
        handler.removeCallbacks(mRunnable)
    }


}






