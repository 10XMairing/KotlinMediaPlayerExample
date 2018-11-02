package com.andevs.tenx.mediaplayerexample

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_home.*
import java.io.File
import java.util.jar.Manifest

class HomeActivity : AppCompatActivity() {



    var adapter : SongAdapter ? = null
    var songs : ArrayList<HashMap<String, String>> = ArrayList()
    var mplayer : MediaPlayer ? = null
    var recentSong = -1



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
                    recentSong = position
                 }
            }




            adapter = SongAdapter(this,songs, listener)
            recycler_songs.layoutManager = LinearLayoutManager(this)
            recycler_songs.adapter = adapter


            btn_stop.setOnClickListener{
               stopAndRelease()
            }

            btn_pause.setOnClickListener{
                mplayer?.pause()

            }
        btn_play.setOnClickListener{
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


    }

    fun stopAndRelease(){
       if(mplayer != null){
           mplayer?.stop()
           mplayer?.release()
           mplayer = null
       }
    }


}





