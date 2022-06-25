package com.dkaera.mash.ui.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dkaera.mash.R
import com.dkaera.mash.ui.onboarding.states.EmailState
import com.dkaera.mash.ui.onboarding.states.PasswordState
import com.dkaera.mash.ui.onboarding.widgets.Email
import com.dkaera.mash.ui.onboarding.widgets.Password
import com.dkaera.mash.ui.theme.PurpleTheme
import com.dkaera.mash.ui.util.supportWideScreen

@Composable
fun SignIn(
    modifier: Modifier,
    navigateToSignUp: () -> Unit,
    signInComplete: (email: String, password: String) -> Unit,
) {
    PurpleTheme {
        Scaffold(
            modifier = modifier,
            topBar = {
                SignInTopAppBar(
                    topAppBarText = stringResource(id = R.string.sign_in),
                )
            },
            content = { contentPadding ->
                SignInSignUpScreen(
                    modifier = Modifier.supportWideScreen(),
                    contentPadding = contentPadding,
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        SignInContent(navigateToSignUp, signInComplete)
                    }
                }
            }
        )
    }
}

@Composable
fun SignInContent(
    navigateToSignUp: () -> Unit,
    onSignInSubmitted: (email: String, password: String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val focusRequester = remember { FocusRequester() }
        val emailState = remember { EmailState() }
        val passwordState = remember { PasswordState() }
        Email(
            emailState = emailState,
            modifier = Modifier
                .fillMaxWidth(),
            onImeAction = { focusRequester.requestFocus() }
        )
        Password(
            label = stringResource(id = R.string.password),
            passwordState = passwordState,
            modifier = Modifier
                .focusRequester(focusRequester)
                .padding(vertical = 16.dp),
            onImeAction = { onSignInSubmitted(emailState.text, passwordState.text) }
        )
        Button(
            onClick = { onSignInSubmitted(emailState.text, passwordState.text) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            enabled = emailState.isValid && passwordState.isValid
        ) {
            Text(
                text = stringResource(id = R.string.sign_in),
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
        }
        Text(
            text = stringResource(id = R.string.or),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
        Button(
            onClick = navigateToSignUp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {
            Text(
                text = stringResource(id = R.string.sign_up),
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun SignInTopAppBar(topAppBarText: String) {
    TopAppBar(
        modifier = Modifier.statusBarsPadding(),
        title = {
            Text(
                text = topAppBarText,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        },
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 0.dp
    )
}

@Composable
private fun SignInPreview() = SignIn(Modifier, {}) { _, _ -> }