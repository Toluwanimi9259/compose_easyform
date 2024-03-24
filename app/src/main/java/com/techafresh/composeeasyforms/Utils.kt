package com.techafresh.composeeasyforms

import com.github.k0shk0sh.compose.easyforms.EasyFormsValidationType

object CustomPasswordValidationType : EasyFormsValidationType(
    minLength = 8,
    maxLength = 20,
    /* Password must contain a special character
    and 8 >= password <= 20 */
    regex = "^(?=.*[!@#&()–[{}]:;',?/*~\$^+=<>]).{8,20}\$"
)


fun findValue(string : String) : String{
    return string.split("value=")[1].split(")")[0]
}