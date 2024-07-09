package ru.itis.mygarden.fragments.profile

import android.R.attr.name
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import ru.itis.mygarden.R
import ru.itis.mygarden.databinding.FragmentProfileBinding
import ru.itis.mygarden.presentation.PlantViewModel
import ru.itis.mygarden.presentation.PlantViewModelFactory


@Suppress("DEPRECATION")
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var binding: FragmentProfileBinding? = null
    var SELECT_PICTURE: Int = 1234
    private lateinit var viewModel: PlantViewModel
    var selectedImageUri : Uri? = null

    var uri : String? = null
    var name : String? = null
    val bundle = Bundle()

    var flag = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)


        uri = arguments?.getString("Uri_string")
        name = arguments?.getString("Edit_text")


        val context = requireContext()
        viewModel = ViewModelProvider(this, PlantViewModelFactory(context)).get(PlantViewModel::class.java)

        flag = savedInstanceState?.getBoolean("Uri_boolean") == true


        if (uri != null) binding?.imageView?.setImageURI(Uri.parse(uri))


        if ((savedInstanceState?.getString("Uri_string") != null) && (flag)) {
            uri = savedInstanceState.getString("Uri_string")
        }

        if (flag) {
            selectedImageUri = Uri.parse(savedInstanceState?.getString("Uri_string"))
            binding?.imageView?.setImageURI(selectedImageUri)
        }


        if (name != null) binding?.nameUser?.setText(name)


        binding?.run {
            lifecycleScope.launch{
                countPlants.text = viewModel.getAllPlants().size.toString()
            }

            var counter = 0
            writeName.setOnClickListener {
                counter++
                if (counter % 2 == 0) {
                    nameUser.isEnabled = false
                    Snackbar.make(
                        root,
                        "Имя пользователя подтверждено, чтобы поменять нажмите на кнопку ещё раз ",
                        Snackbar.LENGTH_LONG
                    ).show()
                } else {
                    nameUser.isEnabled = true
                    Snackbar.make(
                        root,
                        "Вы можете изменить имя пользователя ",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }

            makeAvatar.setOnClickListener(View.OnClickListener(){
                imageChooser();
            })


            infoButton.setOnClickListener{
                bundle.putString("Uri_string",uri)
                bundle.putString("Edit_text",binding?.nameUser?.text.toString())
                findNavController().navigate(resId = R.id.action_profileFragment_to_infoFragment,bundle)
            }
        }
    }


    fun imageChooser() {
        val i = Intent()
        i.setType("image/*")
        i.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(i, SELECT_PICTURE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data?.data
                if (null != selectedImageUri) {
                    binding?.imageView?.setImageURI(selectedImageUri)
                    flag = true
                    uri = selectedImageUri.toString()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("Uri_string",selectedImageUri.toString())
        outState.putBoolean("Uri_boolean",flag)
    }
}