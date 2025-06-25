package com.github.xserxses.nqueensproblem.welcome.home

import android.content.Context
import androidx.fragment.app.Fragment
import com.github.xserxses.nqueensproblem.main.MainActivity

class WelcomeHomeFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity)
            .component
            .welcomeComponent()
            .create()
            .inject(this)
    }
}
