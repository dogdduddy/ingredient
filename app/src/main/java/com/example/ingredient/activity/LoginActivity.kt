package com.example.ingredient.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.ingredient.R
import com.example.ingredient.databinding.ActivityLoginBinding
import com.example.ingredient.network.SessionCallback
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kakao.auth.AuthType
import com.kakao.auth.Session
import com.kakao.util.helper.Utility
import org.json.JSONObject
import java.util.*


class
LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    // Kakao
    private lateinit var callbackManager: CallbackManager
    private lateinit var callback: SessionCallback
    // Google
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var oneTapClient: SignInClient
    // Can be any integer unique to the Activity
    private val REQ_ONE_TAP = 2
    private var showOneTapUI = true
    private lateinit var getResultText:ActivityResultLauncher<Intent>

    private val googleSignInClient by lazy {
        GoogleSignIn.getClient(
            this,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.my_web_client_id))
                .requestEmail()
                .requestProfile()
                .build())
    }

    // 구글 로그인 결과 받을 런처
    private val startForResult: ActivityResultLauncher<Intent> =
        registerForActivityResult( ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account)
                } catch (e: ApiException) {
                    Log.d(TAG, "Google Signin Exception : $e")
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        callback = SessionCallback(this)
        callbackManager = CallbackManager.Factory.create()

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

        // 자동 로그인
        val currentUser = auth.currentUser
        remember_me(currentUser)
        initView()
    }

    public override fun onStart() {
        super.onStart()
    }

    fun remember_me(user: FirebaseUser?) {
        if(user != null) {
            // 현재 사용자의 idToken을 확인하여 자동 로그인 시킬지 말지 결정
            user.getIdToken(true).addOnCompleteListener(OnCompleteListener<GetTokenResult> { task ->
                if (task.isSuccessful) {
                    successLogin(user)
                }
            })
        }
    }

    fun initView() {
        binding.facebookLoginBtn.setOnClickListener {
            facebookLogin()
        }
        binding.kakaoLoginBtn.setOnClickListener {
            kakaoLogin()
        }
        binding.googleLoginBtn.setOnClickListener {
            googleLogin()
        }
    }

    private fun kakaoLogin() {
    // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        Log.d(TAG, "LoginActivity - kakaoLoginStart() called")
        val keyHash = Utility.getKeyHash(this) // keyHash 발급
        Session.getCurrentSession().addCallback(callback)
        Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, this)
    }
    private fun facebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this,
                 callbackManager,
                Arrays.asList("public_profile", "email"))
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    //페이스북 로그인 성공
                    firebaseAuthWithGoogle(result?.accessToken)
                    Log.d("FaceBookLogin", "Login Success")
                }
                override fun onCancel() {
                    //페이스북 로그인 취소
                    Log.d("FaceBookLogin", "Login Cancle")
                    successLogin(null)
                }
                override fun onError(error: FacebookException) {
                    //페이스북 로그인 실패
                    Log.d("FaceBookLogin", "Login Fail : $error")
                    successLogin(null)
                }
            })
    }

    private fun firebaseAuthWithGoogle(token: AccessToken?) {
        if (token != null) {
            val credential = FacebookAuthProvider
                .getCredential(token.token)
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        successLogin(task.result.user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        successLogin(null)
                    }
                }
        }
    }


    // 자동 로그인
    private fun successLogin(user: FirebaseUser?) {
        if(user != null) {
            InsertUserData(user.uid)
            startMainActivity(user)
        }
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
        }
        request.setRetryPolicy(
            DefaultRetryPolicy(500000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        )
        queue.add(request)
        return source.task // call validation server and retrieve firebase token
    }

    private fun googleLogin() {
        val signInIntent = googleSignInClient.signInIntent
        Log.d("googleLogin", "googleLogin() called $signInIntent")
        startForResult.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    successLogin(user)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    successLogin(null)
                }
            }
    }
    fun InsertUserData(userid:String) {
        Firebase.firestore.collection("ListData")
            .document(userid)
    }

    fun startMainActivity(user: FirebaseUser) {
        // providerData는 리스트 데이터가 2개 있다. 0번은 email이 null, 1번은 모두 들어있다. 개발일지 22.12.18 참고
        var userData: UserInfo? = null
        if(user.providerData.size < 2) userData = user.providerData.get(0)
        else userData = user.providerData.get(1)

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("user", userData!!.displayName.toString())
        intent.putExtra("email",userData!!.email.toString())
        intent.putExtra("photo",userData!!.photoUrl.toString())
        startActivity(intent)
        finish()
    }
    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback)
    }

    fun toast(sentence:String) {
        Toast.makeText(this.applicationContext,sentence, Toast.LENGTH_SHORT).show()
    }
}