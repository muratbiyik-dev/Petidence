package com.pureblacksoft.petidence.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.pureblacksoft.petidence.R
import com.pureblacksoft.petidence.database.PetidenceDB
import com.pureblacksoft.petidence.databinding.ActivityMainBinding
import com.pureblacksoft.petidence.fragment.HomeFragment
import com.pureblacksoft.petidence.fragment.PermissFragment
import com.pureblacksoft.petidence.fragment.PetFragment
import com.pureblacksoft.petidence.function.AppFun
import com.pureblacksoft.petidence.veriable.ConstVeriable
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity: AppCompatActivity()
{
    companion object
    {
        lateinit var petidenceDB: PetidenceDB
        var currentFragmentId = ConstVeriable.HOME_FRAGMENT_ID
    }

    private lateinit var rootMA: View
    var fragmentTransaction = supportFragmentManager.beginTransaction()
    var currentFragment: Fragment = HomeFragment()
    private var backPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        rootMA = binding.root
        setContentView(rootMA)

        //region Load Data
        petidenceDB = PetidenceDB(this)

        downloadData()
        //endregion

        //region Load Fragment
        fragmentTransaction.replace(R.id.containerMA, currentFragment).commit()
        //endregion

        //region Bottom Navigation
        val bottomNavMA = rootMA.bottomNavMA
        bottomNavMA.itemIconTintList = null
        bottomNavMA.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId)
            {
                R.id.itmPet ->
                {
                    currentFragmentId = ConstVeriable.PET_FRAGMENT_ID
                    currentFragment = PetFragment()
                }
                R.id.itmMap ->
                {
                    currentFragmentId = ConstVeriable.PERMISS_FRAGMENT_ID
                    currentFragment = PermissFragment()
                }
                else ->
                {
                    currentFragmentId = ConstVeriable.HOME_FRAGMENT_ID
                    currentFragment = HomeFragment()
                }
            }

            fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.containerMA, currentFragment).commit()

            true
        }
        //endregion
    }

    private fun downloadData()
    {
        //region Food
        AppFun.downloadDataJSON(this, ConstVeriable.FOOD_DOWNLOAD_ID)
        //endregion

        //region Sleep
        AppFun.downloadDataJSON(this, ConstVeriable.SLEEP_DOWNLOAD_ID)
        //endregion

        //region Play
        AppFun.downloadDataJSON(this, ConstVeriable.PLAY_DOWNLOAD_ID)
        //endregion
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed()
    {
        if (currentFragmentId != ConstVeriable.HOME_FRAGMENT_ID)
        {
            val itmHome: View = rootMA.bottomNavMA.findViewById(R.id.itmHome)
            itmHome.performClick()
        }
        else
        {
            if (backPressedOnce)
            {
                finishAffinity()

                return
            }

            this.backPressedOnce = true

            //region Back Toast
            val backToast = Toast.makeText(this, R.string.Back_Toast, Toast.LENGTH_SHORT)
            backToast.setGravity(Gravity.BOTTOM, 0, 160)
            backToast.show()
            //endregion

            Handler().postDelayed(
                {
                backPressedOnce = false
                }, 2000)
        }
    }
}

//PureBlack Software / Murat BIYIK