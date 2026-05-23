package com.unifytv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.unifytv.ui.UnifyTvRoot
import com.unifytv.ui.theme.UnifyTvTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = (application as UnifyTvApp).repository
        setContent {
            UnifyTvTheme {
                UnifyTvRoot(repository = repository)
            }
        }
    }
}
