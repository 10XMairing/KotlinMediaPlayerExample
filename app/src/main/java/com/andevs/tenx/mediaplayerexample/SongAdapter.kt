package com.andevs.tenx.mediaplayerexample

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.jar.Attributes

class SongAdapter(val mCtx : Context, val songs : ArrayList<HashMap<String, String>>, val listener : CustomItemClickListener) : RecyclerView.Adapter<SongAdapter.SongViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(mCtx).inflate(R.layout.vh_song_listitem, parent, false)
        return SongViewHolder(view)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {

        holder.tvName.text = (songs[position].get("file_name"))
        holder.tvName.setOnClickListener{
            listener.onItemClick(holder.tvName, position)
            }
        }


    class SongViewHolder(view : View): RecyclerView.ViewHolder(view){
            val tvName = view.findViewById<TextView>(R.id.tv_song_name)
    }




}