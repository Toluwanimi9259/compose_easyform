package com.techafresh.composeeasyforms

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.k0shk0sh.compose.easyforms.BuildEasyForms
import com.github.k0shk0sh.compose.easyforms.CardValidationType
import com.github.k0shk0sh.compose.easyforms.EasyForms
import com.github.k0shk0sh.compose.easyforms.EasyFormsErrorState
import com.github.k0shk0sh.compose.easyforms.EasyFormsResult
import com.github.k0shk0sh.compose.easyforms.EmailValidationType
import com.github.k0shk0sh.compose.easyforms.NameValidationType
import com.github.k0shk0sh.compose.easyforms.PasswordValidationType
import com.techafresh.composeeasyforms.ui.theme.ComposeEasyFormsTheme
import kotlin.math.log

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeEasyFormsTheme {
                // A surface container using the 'background' color from the theme
                BuildEasyForm{
                    viewModel.onButtonClicked(it)
                }
            }
        }
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BuildEasyForm(
    onClick : (EasyForms) -> Unit
){
    Scaffold {
        BuildEasyForms { easyForm ->
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ){
                    EmailTextField(easyForms = easyForm)
                    NameTextField(easyForm = easyForm)
//                    Spacer(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
                    Text(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp), text = "Password must contain at least one digit [0-9]. \n Password must contain at least one lowercase Latin character [a-z] \n Password must contain at least one uppercase Latin character [A-Z] \n Password must contain at least one special character like ! @ # & ( ). \n Password must contain a length of at least 8 characters and a maximum of 20 characters.")
                    PasswordTextField(easyForm = easyForm)


                    Text(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp), text = "Custom Password Validator \n Password must contain at least one special character like ! @ # & ( ). \n Password must contain a length of at least 8 characters and a maximum of 20 characters.")
                    CustomPasswordTextField(easyForm = easyForm)
                    CardTextField(easyForms = easyForm)
                    LoginButton(easyForm = easyForm) {
                        onClick.invoke(easyForm)
                    }
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeEasyFormsTheme {
    }
}


@Composable
fun EmailTextField(easyForms: EasyForms) {
    val textFieldState = easyForms.getTextFieldState(
        key = MyFormsKeys.EMAIL,
        easyFormsValidationType = EmailValidationType,
        defaultValue = "",
    )
    val state = textFieldState.state
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(1f),
        label = { Text(text = "Email")},
        value = state.value,
        onValueChange = textFieldState.onValueChangedCallback,
        isError = textFieldState.errorState.value == EasyFormsErrorState.INVALID,
    )
}

@Composable
fun LoginButton(easyForm: EasyForms, onClick : ()  ->  Unit){
    val errorStates = easyForm.observeFormStates()
    val formDataState = rememberSaveable() { mutableStateOf<String?>(null) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
//        modifier = Modifier.padding(16.dp)
    ){
        Button(onClick = {
            formDataState.value = "${easyForm.formData()}"


            Log.d("TAG", "LoginButton: Field : ${easyForm.formData()[0].key} \n Value :  ${findValue(easyForm.formData()[0].toString())}")
            Log.d("TAG", "LoginButton: Field : ${easyForm.formData()[1].key} \n Value :  ${findValue(easyForm.formData()[1].toString())}")
            Log.d("TAG", "LoginButton: Field : ${easyForm.formData()[2].key} \n Value :  ${findValue(easyForm.formData()[2].toString())}")
            Log.d("TAG", "LoginButton: Field : ${easyForm.formData()[3].key} \n Value :  ${findValue(easyForm.formData()[3].toString())}")
            onClick.invoke()
        },
            modifier = Modifier.fillMaxWidth(1f),
            enabled = errorStates.value.all { it.value == EasyFormsErrorState.VALID }
        ) {
            Text(text = "Login")
        }
        
        Spacer(modifier = Modifier.padding(16.dp))

        if (!formDataState.value.isNullOrEmpty()) {
            val text = buildAnnotatedString {
                append("Email : ${findValue(easyForm.formData()[0].toString())}")
                append("\n")
                append("Username : ${findValue(easyForm.formData()[1].toString())}")
                append("\n")
                append("Password : ${findValue(easyForm.formData()[2].toString())}")
                append("\n")
                append("Custom Password: ${findValue(easyForm.formData()[3].toString())}")
                append("\n")
                append("Card : ${findValue(easyForm.formData()[4].toString())}")
            }
            Text(text, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun NameTextField(easyForm: EasyForms) {
    val nameTextFieldState = easyForm.getTextFieldState(MyFormsKeys.NAME, NameValidationType)
    val nameState = nameTextFieldState.state
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(1f),
        value = nameState.value,
        onValueChange = nameTextFieldState.onValueChangedCallback,
        isError = nameTextFieldState.errorState.value == EasyFormsErrorState.INVALID,
        label = { Text("Name") },
        placeholder = { Text("Joe") },
        leadingIcon = {
            Icon(
                Icons.Outlined.Person,
                contentDescription = "Name",
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    )
}

@Composable
fun PasswordTextField(easyForm: EasyForms) {
    val textFieldState = easyForm.getTextFieldState(MyFormsKeys.PASSWORD, PasswordValidationType)
    val textState = textFieldState.state
    val passwordVisibility = remember { mutableStateOf(false) }
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(1f),
        value = textState.value,
        onValueChange = textFieldState.onValueChangedCallback,
        isError = textFieldState.errorState.value == EasyFormsErrorState.INVALID,
        label = { Text("Password") },
        leadingIcon = {
            Icon(
                Icons.Outlined.Lock,
                "Password",
            )
        },
        trailingIcon = {
            val image = if (passwordVisibility.value) {
                painterResource(id = R.drawable.baseline_visibility_24)
            } else {
                painterResource(id = R.drawable.baseline_visibility_off_24)
            }

            IconButton(onClick = {
                passwordVisibility.value = !passwordVisibility.value
            }) {
                Icon(painter = image, "")
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (passwordVisibility.value) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
    )
}

@Composable
fun CustomPasswordTextField(easyForm: EasyForms){
    val textFieldState = easyForm.getTextFieldState(MyFormsKeys.CUSTOM_PASSWORD , CustomPasswordValidationType)
    val passwordState = textFieldState.state
    val passwordVisibility = remember { mutableStateOf(false) }
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(1f),
        value = passwordState.value,
        onValueChange = textFieldState.onValueChangedCallback,
        isError = textFieldState.errorState.value == EasyFormsErrorState.INVALID,
        label = { Text("Password") },
        leadingIcon = {
            Icon(
                Icons.Outlined.Lock,
                "Password",
            )
        },
        trailingIcon = {
            val image = if (passwordVisibility.value) {
                painterResource(id = R.drawable.baseline_visibility_24)
            } else {
                painterResource(id = R.drawable.baseline_visibility_off_24)
            }

            IconButton(onClick = {
                passwordVisibility.value = !passwordVisibility.value
            }) {
                Icon(painter = image, "")
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (passwordVisibility.value) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
    )
}

@Composable
fun CardTextField(easyForms: EasyForms) {
    val textFieldState = easyForms.getTextFieldState(
        key = MyFormsKeys.CARD,
        easyFormsValidationType = CardValidationType,
        defaultValue = "",
    )
    val state = textFieldState.state
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(1f),
        label = {Text(text = "Card Number")},
        value = state.value,
        onValueChange = textFieldState.onValueChangedCallback,
        isError = textFieldState.errorState.value == EasyFormsErrorState.INVALID,
    )
}