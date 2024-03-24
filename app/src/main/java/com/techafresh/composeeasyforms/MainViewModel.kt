package com.techafresh.composeeasyforms

import androidx.lifecycle.ViewModel
import com.github.k0shk0sh.compose.easyforms.EasyForms
import com.github.k0shk0sh.compose.easyforms.EasyFormsResult

class MainViewModel : ViewModel(){

    fun onButtonClicked(easyForms: EasyForms) {
        val formData = easyForms.formData()
        formData.forEach {
            when (val result = it) {
//                is EasyFormsResult.BooleanResult -> handleBooleanResult(result)
//                is EasyFormsResult.GenericStateResult<*> -> handleGeneric(result)
//                is EasyFormsResult.RangeSliderStateResult -> handleRangeSlider(result)
//                is EasyFormsResult.SliderStateResult -> handleSlider(result)
                is EasyFormsResult.StringResult -> handleString(result)
//                is EasyFormsResult.ToggleableStateResult -> handleToggleable(result)
                else -> throw IllegalArgumentException("${result.key} is not handled")
            }
        }
    }



    private fun handleString(result: EasyFormsResult.StringResult) {
        when (result.key as MyFormsKeys) {
            MyFormsKeys.EMAIL -> String
            MyFormsKeys.PASSWORD -> Unit
            MyFormsKeys.NAME -> Unit
            MyFormsKeys.URL -> Unit
            MyFormsKeys.PHONE -> Unit
            MyFormsKeys.CARD -> Unit
            MyFormsKeys.CUSTOM_PASSWORD -> Unit
            else -> throw IllegalArgumentException("${result.key} is not handled")
        }
    }
}