package com.example.endproject.ui.tutorial
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.endproject.databinding.FragmentTutorialDbBinding

class TutorialDBFragment : Fragment(){
    private lateinit var binding: FragmentTutorialDbBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTutorialDbBinding.inflate(layoutInflater)
        return binding.root
    }

    //todo db에 음식추가하는 법 설명하는 xml만들기
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        binding.btnBack.setOnClickListener {
            //findNavController().navigate(R.id.action_tutorialDBFragment_to_tutorialFragment);
            findNavController().popBackStack()
        }
    }

}