package com.example.panverification

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.panverification.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.isEnabled = false

        viewModel.isNextButtonEnabled.observe(this, Observer {
            binding.btnNext.isEnabled = it
        })

        viewModel.panError.observe(this, Observer {
            binding.etPanNumber.error = it
        })

        viewModel.dayError.observe(this, Observer {
            binding.etDate.error = it
        })

        viewModel.monthError.observe(this, Observer {
            binding.etMonth.error = it
        })

        viewModel.yearError.observe(this, Observer {
            binding.etYear.error = it
        })

        val inputWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onInputChanged(
                    binding.etPanNumber.text.toString(),
                    binding.etDate.text.toString(),
                    binding.etMonth.text.toString(),
                    binding.etYear.text.toString()
                )
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        binding.etPanNumber.addTextChangedListener(inputWatcher)
        binding.etDate.addTextChangedListener(inputWatcher)
        binding.etMonth.addTextChangedListener(inputWatcher)
        binding.etYear.addTextChangedListener(inputWatcher)

        binding.btnNext.setOnClickListener {
            Toast.makeText(this,"Details Submitted Succesfully",Toast.LENGTH_SHORT).show()
            finish()
        }
        binding.tvNoPanLink.setOnClickListener {
            Toast.makeText(this,"Sorry you can't register without PAN",Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}