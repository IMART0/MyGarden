package ru.itis.mygarden.fragments.plant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import ru.itis.mygarden.R
import ru.itis.mygarden.data.Plant
import ru.itis.mygarden.databinding.FragmentPlantBinding
import ru.itis.mygarden.fragments.add.AddFragment
import ru.itis.mygarden.presentation.PlantViewModel
import ru.itis.mygarden.presentation.PlantViewModelFactory

class PlantFragment : Fragment(R.layout.fragment_plant) {

    private var binding: FragmentPlantBinding? = null
    private var adapter: PlantAdapter? = null
    private lateinit var viewModel: PlantViewModel
    private var plantsList: List<Plant>? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        binding = FragmentPlantBinding.bind(view)
        viewModel = ViewModelProvider(this, PlantViewModelFactory(context)).get(PlantViewModel::class.java)
        viewModel.addPlant(Plant(
            id = 1,
            name = "European Silver Fir",
            description = "Description of the European Silver Fir",
            sunlight = "full_sun",
            nextWateringTime = 1720429200L,
            wateringFrequency = 3,
            imgSource = "https://perenual.com/storage/species_image/1_abies_alba/og/1536px-Abies_alba_SkalitC3A9.jpg"
        ))
        lifecycleScope.launch {
            plantsList = viewModel.getAllPlants()
            println(1)
            initAdapter()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun initAdapter() {
        adapter = plantsList?.let {
            PlantAdapter(
                plants = it,
                glide = Glide.with(this),
                onClick = {
                    findNavController().navigate(
                        resId = R.id.action_plantFragment_to_addFragment,
                        args = AddFragment.bundle(id = it.id)
                    )
                }
            )
        }
        binding?.run {
            userPlantsRv.adapter = adapter
        }
    }
}