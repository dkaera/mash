package com.dkaera.mash.ui.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dkaera.mash.R
import com.dkaera.mash.sumOf
import com.dkaera.mash.ui.onboarding.states.ConfirmPasswordState
import com.dkaera.mash.ui.onboarding.states.EmailState
import com.dkaera.mash.ui.onboarding.states.PasswordState
import com.dkaera.mash.ui.onboarding.states.TextState
import com.dkaera.mash.ui.onboarding.widgets.Email
import com.dkaera.mash.ui.onboarding.widgets.Password
import com.dkaera.mash.ui.onboarding.widgets.TextInput
import com.dkaera.mash.ui.util.supportWideScreen

@Composable
fun SignUp(
    modifier: Modifier,
    onUp: () -> Unit,
    signUpComplete: (email: String, password: String) -> Unit
) {
    Scaffold(
        modifier = modifier.statusBarsPadding(),
        topBar = {
            SignUpTopAppBar(
                topAppBarText = stringResource(id = R.string.sign_up),
                onBackPressed = onUp
            )
        },
        content = { contentPadding ->
            SignInSignUpScreen(
                modifier = Modifier.supportWideScreen(),
                contentPadding = contentPadding,
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SignUpContent(onSignUpSubmitted = signUpComplete)
                }
            }
        }
    )
}

@Composable
fun SignUpContent(
    onSignUpSubmitted: (email: String, password: String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val focusRequester = remember { FocusRequester() }
        val confirmationPasswordFocusRequest = remember { FocusRequester() }

        val emailState = remember { EmailState() }
        val passwordState = remember { PasswordState() }
        val nickNameState = remember { TextState() }
        val confirmPasswordState = remember { ConfirmPasswordState(passwordState = passwordState) }

        TextInput(
            label = stringResource(id = R.string.nick_name),
            textState = nickNameState,
            modifier = Modifier.padding(top = 16.dp),
        )
        Email(emailState = emailState,
            modifier = Modifier.padding(vertical = 16.dp),
            onImeAction = { focusRequester.requestFocus() })
        Password(
            label = stringResource(id = R.string.password),
            passwordState = passwordState,
            modifier = Modifier
                .focusRequester(focusRequester)
                .padding(bottom = 16.dp),
            onImeAction = { confirmationPasswordFocusRequest.requestFocus() }
        )
        Password(
            label = stringResource(id = R.string.confirm_password),
            passwordState = confirmPasswordState,
            onImeAction = {
                if (isDataFilled(emailState, passwordState, confirmPasswordState, nickNameState)) {
                    onSignUpSubmitted(emailState.text, passwordState.text)
                }
            },
            modifier = Modifier.focusRequester(confirmationPasswordFocusRequest)
        )
        Button(
            onClick = { onSignUpSubmitted(emailState.text, passwordState.text) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            enabled = isDataFilled(emailState, passwordState, confirmPasswordState, nickNameState)
        ) {
            Text(
                text = stringResource(id = R.string.sign_up),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

private fun isDataFilled(
    emailState: EmailState,
    passwordState: PasswordState,
    confirmPasswordState: ConfirmPasswordState,
    nickNameState: TextState
) = arrayOf(
    emailState,
    passwordState,
    confirmPasswordState,
    nickNameState
).map { it.isValid }.sumOf { it }

@Composable
fun SignUpTopAppBar(topAppBarText: String, onBackPressed: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = topAppBarText,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Filled.ChevronLeft,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        // We need to balance the navigation icon, so we add a spacer.
        actions = {
            Spacer(modifier = Modifier.width(68.dp))
        },
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 0.dp
    )
}

@Preview(name = "SignUp")
@Composable
private fun SignUpPreview() = SignUp(
    Modifier,
    {},
    { _, _ -> },
)