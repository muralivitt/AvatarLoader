package mindvalley.demo.view

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import mindvalley.demo.viewmodel.MainActivityViewModel
import mindvalley.loader.R
import mindvalley.loader.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        setImageUpdateListener()
    }

    private fun setupViewModel() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = MainActivityViewModel()
        binding.viewModel?.updateImageUrl()
    }

    private fun setImageUpdateListener(){
        binding?.viewModel?.imageUrl?.observe(this, Observer {
            avatarImageView.loadHttpImage(it,R.drawable.notfound)
            progressBar.start()
        })
    }
}
