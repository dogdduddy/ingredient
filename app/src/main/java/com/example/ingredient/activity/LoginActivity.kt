package com.example.ingredient.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.ingredient.R
import com.example.ingredient.network.SessionCallback
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.auth.AuthType
import com.kakao.auth.Session
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.util.helper.Utility
import org.json.JSONObject
import java.util.*

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"
    private lateinit var auth: FirebaseAuth
    // Kakao
    private lateinit var callbackManager: CallbackManager
    private lateinit var callback: SessionCallback
    // Google
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var oneTapClient: SignInClient
    private lateinit var googleSignInClient: GoogleSignInClient
    // Can be any integer unique to the Activity
    private val REQ_ONE_TAP = 2
    private var showOneTapUI = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.my_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // setGoogleIdTokenRequestOptions에 서버 Client ID를 전달
        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // 서버 클라이언트 ID (현재는 Firebase Web Client ID)
                    .setServerClientId(getString(R.string.my_web_client_id))
                    // 이전에 로그인하는 데 사용한 계정만 표시합니다.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            .setAutoSelectEnabled(true)
            .build()

        setContentView(R.layout.activity_login)

        // 자동 로그인
        val currentUser = auth.currentUser
        updateUI(currentUser)

        //facebook
        val facebookLoginBtn = findViewById<LoginButton>(R.id.facebookLoginBtn)
        val kakaoLoginBtn = findViewById<Button>(R.id.kakaoLoginBtn)
        val googleBtnbtn = findViewById<Button>(R.id.googleLoginBtn)

        callback = SessionCallback(this)
        callbackManager = CallbackManager.Factory.create()

        facebookLoginBtn.setOnClickListener {
            facebookLogin()
        }
        kakaoLoginBtn.setOnClickListener {
            kakaoLogin()
        }
        googleBtnbtn.setOnClickListener {
            googleLogin()
        }

        val signoutBtn = findViewById<Button>(R.id.signout)
        signoutBtn.setOnClickListener {
            signOut()
        }
    }

    private fun kakaoLogin() {
    // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        Log.d(TAG, "LoginActivity - kakaoLoginStart() called")

        val keyHash = Utility.getKeyHash(this) // keyHash 발급
        Log.d(TAG, "KEY_HASH : $keyHash")

        Session.getCurrentSession().addCallback(callback)
        Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, this)
    }
    private fun facebookLogin() {
        LoginManager.getInstance()
            .logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    //페이스북 로그인 성공
                    handleFacebookAccessToken(result?.accessToken)
                    Log.d("FaceBookLogin", "Login Success")
                    toast("로그인 성공")
                }
                override fun onCancel() {
                    //페이스북 로그인 취소
                    toast("로그인 취소")
                    Log.d("FaceBookLogin", "Login Cancle")
                    updateUI(null)
                }

                override fun onError(error: FacebookException?) {
                    //페이스북 로그인 실패
                    toast("페이스북 로그인 실패")
                    Log.d("FaceBookLogin", "Login Fail : $error")
                    updateUI(null)
                }
            })
    }

    private fun handleFacebookAccessToken(token: AccessToken?) {
        Log.d("MainActivity", "handleFacebookAccessToken:$token")
        if (token != null) {
            val credential = FacebookAuthProvider
                .getCredential(token.token)
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("MainActivity", "signInWithCredential:success")
                        updateUI(task.result.user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("MainActivity", "signInWithCredential:failure", task.exception)
                        toast("Authentication failed.")
                        updateUI(null)
                    }
                }
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    fun signOut() {
        LoginManager.getInstance().logOut()
        UserManagement.getInstance().requestLogout(object : LogoutResponseCallback() {
            override fun onCompleteLogout() {
                FirebaseAuth.getInstance().signOut()
                val handler = Handler(Looper.getMainLooper())
                handler.post(Runnable {
                    //updateUI()
                    Log.d(TAG, "Logout Complete!")
                })
            }
        })
        updateUI(null)
        toast("로그아웃")
    }
    // 자동 로그인
    private fun updateUI(user: FirebaseUser?) {
        if(user == null) {
            toast("로그인에 실패했습니다.")
        }
        else {
            user!!.getIdToken(true)
                .addOnCompleteListener(object : OnCompleteListener<GetTokenResult?> {
                    override fun onComplete(task: com.google.android.gms.tasks.Task<GetTokenResult?>) {
                        if (task.isSuccessful()) {
                            Log.d("userTest", "user : ${user.uid}")
                            val moveMain_intent =
                                Intent(applicationContext, MainActivity::class.java)
                            startActivity(moveMain_intent)
                            finish()
                        }
                    }
                })
        }
    }

    fun toast(sentence:String) {
        Toast.makeText(this.applicationContext,sentence, Toast.LENGTH_SHORT).show()
    }

    open fun getFirebaseJwt(kakaoAccessToken:String): com.google.android.gms.tasks.Task<String> {
        val source = com.google.android.gms.tasks.TaskCompletionSource<String>()
        val queue = Volley.newRequestQueue(this)
        val url = String.format("http://%s:8000/verifyToken",getString(R.string.ipconfig))
        val validationObject: HashMap<String?, String?> = HashMap()
        validationObject["token"] = kakaoAccessToken

        val request: JsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST, url,
            JSONObject(validationObject as Map<*,*>),
            Response.Listener { response ->
                try {
                    val firebaseToken = response.getString("firebase_token")
                    source.setResult(firebaseToken)
                } catch (e: Exception) {
                    source.setException(e)
                    Log.w("getFirebaseJwt", "JWT Response Exception : $e")
                }
            },
            Response.ErrorListener { error ->
                Log.e(TAG, error.toString())
                source.setException(error)
            }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = String.format("Basic %s", Base64.getEncoder().encodeToString(
                    String.format("%s:%s", "token", kakaoAccessToken)
                        .toByteArray())
                )
                return params
            }

        };
        request.setRetryPolicy(
            DefaultRetryPolicy(500000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        queue.add(request)
        return source.task // call validation server and retrieve firebase token
    }

    private fun googleLogin() {
        val signInIntent = googleSignInClient.signInIntent
        // 로그인 팝업에서의 진행 결과를 이어 받을 수 있는 startActivityForResult
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        //onActivityResult에서는 callbackManager에 로그인 결과를 넘겨줍니다
        //여기에 callbackManager?.onAcitivyResult가 있어야 onSuccess를 호출할 수 있습니다.
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)){
            Log.i(TAG, "Session get current session")
            return
        }
        super.onActivityResult(requestCode, resultCode, data)

        Log.d(TAG, "In Result method")
        Log.d(TAG, "$requestCode / $resultCode / $data")

        // GoogleSignInApi.getSignInIntent(...)의 결과를 받음.
        if (requestCode === RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Log.d(TAG, "Google Signin Exception : $e")
            }
        }
    }
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }


    fun startMainActivity(){
        Log.d(TAG, "LoginActivity - startMainActivity() called")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback)
    }
    companion object {
        private const val RC_SIGN_IN = 9001
    }
}