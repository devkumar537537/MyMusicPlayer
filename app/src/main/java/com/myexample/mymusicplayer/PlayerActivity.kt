package com.myexample.mymusicplayer

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.myexample.mymusicplayer.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity(),ServiceConnection,MediaPlayer.OnCompletionListener {

    companion object{
       lateinit var musicListPA:ArrayList<MusicModel>
       var songPostion:Int =0

var isPlaying:Boolean = false
        var musicservice:MusicService? = null
        lateinit var binding: ActivityPlayerBinding
        var repeat:Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent= Intent(this,MusicService::class.java)
        startService(intent)
        bindService(intent,this, BIND_AUTO_CREATE)


        intialiseLalyout()
        binding.backbtnPA.setOnClickListener {
            finish()
        }
        binding.playpausebtnPA.setOnClickListener {
            if (isPlaying) {
                pauseMusic()
            } else {
                playMusic()
            }
        }
         binding.previourbtnPA.setOnClickListener {
               preNextSong(increment = false)
           }
                   binding.nextbtnPA.setOnClickListener {
                 preNextSong(increment = true)
          }

            binding.seekBarPA.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                    if(fromUser){
                        musicservice!!.mediaPlayer!!.seekTo(progress)

                    }

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) =Unit

                override fun onStopTrackingTouch(seekBar: SeekBar?) =Unit

            })

binding.repeatBtnPA.setOnClickListener {
    if(!repeat){
        repeat = true
        binding.repeatBtnPA.setColorFilter(ContextCompat.getColor(this,R.color.purple_500))
    }else{
        repeat = false
        binding.repeatBtnPA.setColorFilter(ContextCompat.getColor(this,R.color.cool_pink))
    }
}
    }
   private fun setLayout(){

        Glide.with(this).load(musicListPA.get(songPostion).artUri).
        apply(RequestOptions()
            .placeholder(R.drawable.splash_screen).centerCrop()).into(binding.songImgPA)
        binding.songNamePA.text = musicListPA.get(songPostion).title

       if(repeat){
           binding.repeatBtnPA.setColorFilter(ContextCompat.getColor(this,R.color.purple_500))
       }
    }

    private fun createMedaiaPlayer()
    {
        try {


            if (musicservice!!.mediaPlayer == null) {
                musicservice!!.mediaPlayer = MediaPlayer()
            } else {
                musicservice!!.mediaPlayer!!.reset()
                musicservice!!.mediaPlayer!!.setDataSource(musicListPA.get(songPostion).path)
                musicservice!!.mediaPlayer!!.prepare()
                musicservice!!.mediaPlayer!!.start()
                isPlaying = true
                binding.playpausebtnPA.setIconResource(R.drawable.pause_icon)
                musicservice!!.showNotification(false)
                binding.tvSeekBarstart.text = formatDuration(musicservice!!.mediaPlayer!!.currentPosition.toLong())
                binding.tvseekbarend.text = formatDuration(musicservice!!.mediaPlayer!!.duration.toLong())
                    binding.seekBarPA.progress = 0
                binding.seekBarPA.max = musicservice!!.mediaPlayer!!.duration
                musicservice!!.mediaPlayer!!.setOnCompletionListener(this)
            }
        }catch (e:Exception)
        {
            return
        }
    }
    private fun intialiseLalyout()
    {

        songPostion = intent.getIntExtra("index",0)
        when(intent.getStringExtra("class")){
            "MusicAdapter" ->{
                musicListPA = ArrayList()
                musicListPA.addAll(MainActivity.MusicListMA)
                setLayout()

            }
            "MainActivity"->{
                musicListPA = ArrayList()
                musicListPA.addAll(MainActivity.MusicListMA)
                musicListPA.shuffle()
                setLayout()

            }
        }

    }
    private fun playMusic()
    {
            binding.playpausebtnPA.setIconResource(R.drawable.pause_icon)
        musicservice!!.showNotification(false)
        isPlaying = true
        musicservice!!.mediaPlayer!!.start()
    }
    private fun pauseMusic()
    {
        binding.playpausebtnPA.setIconResource(R.drawable.play_icon)
        musicservice!!.showNotification(true)
        isPlaying=false
        musicservice!!.mediaPlayer!!.pause()
    }
    private fun preNextSong(increment:Boolean)
    {
        if(increment)
        {
           setSongPostion(increment = true)
            setLayout()
            createMedaiaPlayer()
        }else{
            setSongPostion(increment = false)
            setLayout()
            createMedaiaPlayer()
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
      val binder = service as MusicService.MyBinder
        musicservice = binder.currentService()
        createMedaiaPlayer()
      musicservice!!.seekBarSetup()

    }

    override fun onServiceDisconnected(name: ComponentName?) {
      musicservice = null
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(this)
        val intent = Intent(this@PlayerActivity,MusicService::class.java)
        stopService(intent)
    }

    override fun onCompletion(mp: MediaPlayer?) {
        setSongPostion(true)
        createMedaiaPlayer()
        try {
            setLayout()
        }catch (e:java.lang.Exception){
            return
        }

    }
}