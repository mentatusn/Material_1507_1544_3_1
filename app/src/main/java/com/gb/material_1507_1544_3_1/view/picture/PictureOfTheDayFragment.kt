package com.gb.material_1507_1544_3_1.view.picture

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.BulletSpan
import android.text.style.ImageSpan
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.gb.material_1507_1544_3_1.view.MainActivity
import com.gb.material_1507_1544_3_1.view.api.ApiActivity
import com.gb.material_1507_1544_3_1.view.api.ApiBottomActivity
import com.gb.material_1507_1544_3_1.view.chips.SettingsFragment
import com.gb.material_1507_1544_3_1.viewmodel.PictureOfTheDayState
import com.gb.material_1507_1544_3_1.viewmodel.PictureOfTheDayViewModel
import com.gb.material_1507_1544_3_1.R
import com.gb.material_1507_1544_3_1.databinding.FragmentMainStartBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.text.SimpleDateFormat
import java.util.*

class PictureOfTheDayFragment : Fragment() {

    private var _binding: FragmentMainStartBinding? = null
    val binding: FragmentMainStartBinding
        get() {
            return _binding!!
        }

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData().observe(viewLifecycleOwner, Observer {
            renderData(it)
        })
        viewModel.sendServerRequest()

        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }

        val behavior = BottomSheetBehavior.from(binding.includeBottomSheet.bottomSheetContainer)
        behavior.state = BottomSheetBehavior.STATE_HIDDEN


        behavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d("mylogs", "$slideOffset slideOffset")
            }
        })

        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.yestrday ->{viewModel.sendServerRequest(takeDate(-1))}
                R.id.today ->{viewModel.sendServerRequest()}
            }
        }
        
        setBottomAppBar()

    }

    private fun takeDate(count: Int): String {
        val currentDate = Calendar.getInstance()
        currentDate.add(Calendar.DAY_OF_MONTH, count)
        val format1 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        format1.timeZone = TimeZone.getTimeZone("EST")
        return format1.format(currentDate.time)
    }


    private fun renderData(state: PictureOfTheDayState) {
        when (state) {
            is PictureOfTheDayState.Error -> { state.error.message
            }
            is PictureOfTheDayState.Loading -> {
                binding.imageView.load(R.drawable.ic_no_photo_vector)
            }
            is PictureOfTheDayState.Success -> {
                val pictureOfTheDayResponseData = state.pictureOfTheDayResponseData
                val url = pictureOfTheDayResponseData.url
                binding.imageView.load(url) {
                    lifecycle(this@PictureOfTheDayFragment)
                    error(R.drawable.ic_load_error_vector)
                    placeholder(R.drawable.ic_no_photo_vector)
                }
                pictureOfTheDayResponseData.explanation?.let{
                    binding.textView.text = it
                    /*binding.textView.typeface = Typeface.createFromAsset(requireContext().assets,"Robus-BWqOd.otf")
                    binding.textView.typeface = Typeface.createFromAsset(requireContext().assets,"font/font/Robus-BWqOd.otf")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        binding.textView.typeface = resources.getFont(R.font.a)
                    }*/

                    val spannableMutable = SpannableStringBuilder("My \n text \n text \nbullet one \nbullet two")
                    val spannableUnMutable = SpannableString("My text \nbullet one \nbullet two")

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        spannableMutable.setSpan(BulletSpan(20,resources.getColor(R.color.colorAccent),20),
                            0,30,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                        spannableMutable.setSpan(BulletSpan(20,resources.getColor(R.color.colorAccent),20),
                        4,21,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                        spannableMutable.setSpan(BulletSpan(20,resources.getColor(R.color.colorAccent),20),
                        11,21,Spannable.SPAN_INCLUSIVE_INCLUSIVE)

                    }

                    spannableMutable.setSpan(BulletSpan(20,resources.getColor(R.color.colorAccent)),
                    21,spannableMutable.length,Spannable.SPAN_INCLUSIVE_INCLUSIVE)

                    for (i in spannableMutable.indices) {
                        if (spannableMutable[i] == 'o') {
                            spannableMutable.setSpan(ImageSpan(requireContext(), R.drawable.ic_earth), i, i + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                        }
                    }


                    binding.textView.text = spannableMutable
                }


            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance(): PictureOfTheDayFragment {
            return PictureOfTheDayFragment()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.api_activity -> {
                startActivity(Intent(requireContext(), ApiActivity::class.java))
            }
            R.id.api_bottom_activity -> {
                startActivity(Intent(requireContext(),ApiBottomActivity::class.java))
            }
            R.id.app_bar_settings -> requireActivity().supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    SettingsFragment.newInstance()
                ).commit()
            android.R.id.home -> BottomNavigationDrawerFragment().show(
                requireActivity().supportFragmentManager,
                ""
            )
        }
        return super.onOptionsItemSelected(item)
    }

    private var isMain = true
    private fun setBottomAppBar() {
        val context = activity as MainActivity
        context.setSupportActionBar(binding.bottomAppBar)
        setHasOptionsMenu(true)


        binding.fab.setOnClickListener {
            if (isMain) {
                isMain = false
                binding.bottomAppBar.navigationIcon = null
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_back_fab
                    )
                )
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
            } else {
                isMain = true
                binding.bottomAppBar.navigationIcon =
                    ContextCompat.getDrawable(context, R.drawable.ic_hamburger_menu_bottom_bar)
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_plus_fab
                    )
                )
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Log.d(TAG, "Fragment back pressed invoked")
                    if (isMain) {
                        System.exit(0)
                    } else {
                        isMain = true
                        binding.bottomAppBar.navigationIcon = ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_hamburger_menu_bottom_bar
                        )
                        binding.bottomAppBar.fabAlignmentMode =
                            BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                        binding.fab.setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.ic_plus_fab
                            )
                        )
                        binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
                    }
                }
            }
            )


    }


}