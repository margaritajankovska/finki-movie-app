package com.example.movieapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentProfileBinding
import com.example.movieapp.domain.profile.model.Profile
import java.io.File
import java.util.*

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var saved = false

    private var imageFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val profileViewModelFactory = ProfileViewModelFactory(requireContext())
        profileViewModel =
            ViewModelProvider(this, profileViewModelFactory).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initViews()

        profileViewModel.getProfileLiveData().observe(viewLifecycleOwner) {
            initProfileData(it)
        }

        profileViewModel.getSaveStateLiveData().observe(viewLifecycleOwner) { isSuccessful ->
            if (isSuccessful) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.profile_save_succes_mesage),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.profile_save_error_message),
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
        return root
    }

    private fun initProfileData(it: Profile) {
        Glide.with(requireContext())
            .load(File(requireActivity().filesDir, it.imageName ?: ""))
            .centerCrop()
            .placeholder(R.drawable.ic_profile)
            .into(binding.imageName)

        if (it.fullName != null) {
            binding.fullName.setText(it.fullName)
        }
        for (genre in it.genres) {
            val editText = EditText(requireContext())
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            editText.layoutParams = layoutParams
            editText.setText(genre)
            binding.genresHolder.addView(editText, binding.genresHolder.childCount - 1)
        }
    }

    private fun initViews() {
        binding.apply {
            takePicture.setOnClickListener { v: View? ->
                deleteCachedImage()
                startActivityForResult(
                    Intent(requireContext(), CameraActivity::class.java),
                    CameraActivity.IMAGE_REQUEST_CODE
                )
            }
            addGenre.setOnClickListener { v: View? ->
                val editText = EditText(requireContext())
                val layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                editText.layoutParams = layoutParams
                genresHolder.addView(editText, genresHolder.childCount - 1)
                editText.requestFocus()
            }
            save.setOnClickListener { v: View? ->
                saved = true
                val genres = ArrayList<String>()
                for (i in 0 until binding.genresHolder.childCount - 1) {
                    val childAt = binding.genresHolder.getChildAt(i) as EditText
                    genres.add(childAt.text.toString())
                }
                profileViewModel.saveProfile(
                    fullName.text.toString(),
                    imageFile?.name ?: "",
                    genres
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CameraActivity.IMAGE_REQUEST_CODE && resultCode == CameraActivity.IMAGE_RESULT_CODE_OK) {
            var fileName = data!!.getStringExtra(CameraActivity.IMAGE_EXTRA).toString()
            if (fileName.isNotEmpty()) {
                imageFile = File(requireActivity().filesDir, fileName)
                Glide.with(requireContext())
                    .load(imageFile)
                    .centerCrop()
                    .placeholder(R.drawable.ic_profile)
                    .into(binding.imageName)
            }
        }
    }

    private fun deleteCachedImage() {
        imageFile?.let { requireActivity().deleteFile(imageFile!!.name) }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!saved) {
            deleteCachedImage()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}