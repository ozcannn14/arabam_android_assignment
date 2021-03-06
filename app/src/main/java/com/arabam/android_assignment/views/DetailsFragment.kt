package com.arabam.android_assignment.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.arabam.android_assignment.adapters.SliderAdapter
import com.arabam.android_assignment.databinding.FragmentDetailsBinding
import com.arabam.android_assignment.viewmodels.DetailsViewModel


class DetailsFragment : Fragment() {
    private lateinit var viewModel: DetailsViewModel
    private var id: Int? = null

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            id = DetailsFragmentArgs.fromBundle(it).id
        }

        viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        viewModel.getDetails(id)


        _binding?.button?.setOnClickListener {
            val action =
                DetailsFragmentDirections.actionDetailsFragmentToFullscreenFragment(binding.selectedAdvert?.photos?.toTypedArray())
            Navigation.findNavController(view).navigate(action)

        }

        _binding?.phoneView?.setOnClickListener {
            try {
                var number: String? = _binding?.phoneView?.text.toString()
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
                startActivity(intent)
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }




        listenData()
    }

    private fun listenData() {
        viewModel.advert.observe(viewLifecycleOwner, Observer { item ->
            item.let {
                _binding?.selectedAdvert = it
                _binding?.viewPager?.adapter = SliderAdapter(_binding?.root?.context, it.photos)

            }
        })

    }
}