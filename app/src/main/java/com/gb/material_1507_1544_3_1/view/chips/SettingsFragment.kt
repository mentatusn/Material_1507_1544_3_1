package com.gb.material_1507_1544_3_1.view.chips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gb.material_1507_1544_3_1.R
import com.gb.material_1507_1544_3_1.databinding.FragmentSettingsBinding
import com.google.android.material.chip.Chip


class SettingsFragment : Fragment() {


    private var _binding: FragmentSettingsBinding? = null
    val binding: FragmentSettingsBinding
        get() {
            return _binding!!
        }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            group.findViewById<Chip>(checkedId)?.let {
                Toast.makeText(context, "choose ${it.text}", Toast.LENGTH_SHORT).show()
            }

        }

        binding.chipForDelete.setOnCloseIconClickListener {
            //binding.chipForDelete.visibility = View.GONE
            binding.chipForDelete.isChecked = false
        }

        binding.tabs.getTabAt(0)!!.text = "Работает"
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_one -> {
                    Toast.makeText(context,"1",Toast.LENGTH_SHORT).show()
                }
                R.id.navigation_two -> {
                    Toast.makeText(context,"2",Toast.LENGTH_SHORT).show()
                }
                R.id.navigation_third -> {
                    Toast.makeText(context,"3",Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SettingsFragment()
    }
}