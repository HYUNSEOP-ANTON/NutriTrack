package com.example.endproject.ui.tutorial
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.endproject.databinding.FragmentTutorialDietBinding

class TutorialDietFragment : Fragment(){
    private lateinit var binding: FragmentTutorialDietBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTutorialDietBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        binding.btnBack.setOnClickListener {
            //findNavController().navigate(R.id.action_tutorialDietFragment_to_tutorialFragment);
            findNavController().popBackStack()
        }

    }
}