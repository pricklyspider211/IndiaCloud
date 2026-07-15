package com.indiacloud.panel

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.KeyEvent
import android.webkit.CookieManager
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var loadingSpinner: ProgressBar
    private var uploadMessage: ValueCallback<Array<Uri>>? = null
    private val PERMISSION_REQUEST_CODE = 100
    val FILE_PICKER_REQUEST_CODE = 1
    val CAMERA_PERMISSION_REQUEST_CODE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        loadingSpinner = findViewById(R.id.loadingSpinner)

        requestRequiredPermissions()
        setupWebView()
    }

    private fun setupWebView() {
        val settings: WebSettings = webView.settings.apply {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            domStorageEnabled = true
            databaseEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
            useWideViewPort = true
            loadWithOverviewMode = true
            builtInZoomControls = false
            displayZoomControls = false
            setSupportZoom(false)
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            allowFileAccess = true
            allowContentAccess = true
            userAgentString = userAgentString + " IndiaCloudApp/1.0"
        }

        CookieManager.getInstance().apply {
            setAcceptCookie(true)
            setAcceptThirdPartyCookies(webView, true)
        }

        webView.webViewClient = IndiaCloudWebViewClient(loadingSpinner)
        webView.webChromeClient = IndiaCloudWebChromeClient(loadingSpinner, this)

        webView.setDownloadListener { url, userAgent, contentDisposition, mimeType, contentLength ->
            downloadFile(url, contentDisposition)
        }

        webView.loadUrl("https://panel.indiacloud.qzz.io/")
    }

    private fun downloadFile(url: String, contentDisposition: String) {
        val fileName = "IndiaCloud_" + System.currentTimeMillis()
        val request = DownloadManager.Request(Uri.parse(url)).apply {
            setMimeType("application/octet-stream")
            addRequestHeader("User-Agent", webView.settings.userAgentString)
            setDescription("Downloading file...")
            setTitle(fileName)
            allowScanningByMediaScanner()
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        }
        val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)
    }

    private fun requestRequiredPermissions() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.POST_NOTIFICATIONS
            )
        } else {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest, PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack()
                return true
            } else {
                finish()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            FILE_PICKER_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val results = if (data?.data != null) {
                        arrayOf(data.data!!)
                    } else {
                        null
                    }
                    uploadMessage?.onReceiveValue(results)
                } else {
                    uploadMessage?.onReceiveValue(null)
                }
                uploadMessage = null
            }
        }
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }
}

class IndiaCloudWebViewClient(private val loadingSpinner: ProgressBar) : WebViewClient() {

    override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
        super.onPageStarted(view, url, favicon)
        loadingSpinner.visibility = android.view.View.VISIBLE
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        loadingSpinner.visibility = android.view.View.GONE
    }

    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {
        val url = request?.url.toString()
        if (!url.startsWith("https://panel.indiacloud.qzz.io")) {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    view?.context?.startActivity(intent)
                    return true
                } catch (e: Exception) {
                    return false
                }
            }
        }
        return false
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        loadingSpinner.visibility = android.view.View.GONE
    }
}

class IndiaCloudWebChromeClient(
    private val loadingSpinner: ProgressBar,
    private val activity: MainActivity
) : WebChromeClient() {

    private var uploadMessage: ValueCallback<Array<Uri>>? = null
    private var permissionRequest: PermissionRequest? = null

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        loadingSpinner.visibility = if (newProgress == 100) {
            android.view.View.GONE
        } else {
            android.view.View.VISIBLE
        }
    }

    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        uploadMessage = filePathCallback
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        activity.startActivityForResult(intent, activity.FILE_PICKER_REQUEST_CODE)
        return true
    }

    override fun onPermissionRequest(request: PermissionRequest?) {
        if (request == null) return

        permissionRequest = request
        val requestedPermissions = request.resources
        val permissions = mutableListOf<String>()

        for (permission in requestedPermissions) {
            when (permission) {
                PermissionRequest.RESOURCE_VIDEO_CAPTURE -> {
                    permissions.add(Manifest.permission.CAMERA)
                }
                PermissionRequest.RESOURCE_AUDIO_CAPTURE -> {
                    permissions.add(Manifest.permission.RECORD_AUDIO)
                }
            }
        }

        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                activity,
                permissions.toTypedArray(),
                activity.CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            request.grant(request.resources)
        }
    }
}
