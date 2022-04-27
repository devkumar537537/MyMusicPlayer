package com.myexample.mymusicplayer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.myexample.mymusicplayer.databinding.MusicViewBinding

class MusicAdapter(private val contextt:Context,private val musiclist:ArrayList<MusicModel>) :RecyclerView.Adapter<MusicAdapter.MyViewHolder>() {
    class MyViewHolder(binding: MusicViewBinding): RecyclerView.ViewHolder(binding.root) {

        val title = binding.songNameMV
        val album = binding.songAlbumMV
        val image = binding.imageviewMV
        val duration = binding.songDuration
        val  root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicAdapter.MyViewHolder {
 return MyViewHolder(MusicViewBinding.inflate(LayoutInflater.from(contextt),parent,false))
    }

    override fun onBindViewHolder(holder: MusicAdapter.MyViewHolder, position: Int) {
     val musicModel = musiclist.get(position)
        holder.title.text = musicModel.title
        holder.album.text =musicModel.album
        holder.duration.text = formatDuration(musicModel.duration)
Glide.with(contextt).load(musicModel.artUri).apply(RequestOptions().placeholder(R.drawable.splash_screen).centerCrop()).into(holder.image)
holder.root.setOnClickListener {
    val intent = Intent(contextt,PlayerActivity::class.java)
    intent.putExtra("index",position)
    intent.putExtra("class","MusicAdapter")
    ContextCompat.startActivity(contextt,intent,null)
}
    }

    override fun getItemCount(): Int {
       return musiclist.size
    }
}