package com.example.endproject.ui.tutorial


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.endproject.R
import com.example.endproject.databinding.FragmentTutorialBinding

class TutorialFragment : Fragment(){

    private lateinit var binding: FragmentTutorialBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTutorialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //DB에 추가하는 법
        binding.btnDBTip.setOnClickListener {
            findNavController().navigate(R.id.action_tutorialFragment_to_tutorialDBFragment)
        }

        //식단 팁 요령
        binding.btnDietTip.setOnClickListener {
            findNavController().navigate(R.id.action_tutorialFragment_to_tutorialDietFragment)
        }

    }
}