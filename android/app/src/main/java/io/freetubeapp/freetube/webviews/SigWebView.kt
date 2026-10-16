package io.freetubeapp.freetube.webviews

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import io.freetubeapp.freetube.javascript.AsyncJSCommunicator
import io.freetubeapp.freetube.javascript.SigWebViewJavascriptInterface
import org.json.JSONObject

@SuppressLint("ViewConstructor")
class SigWebView(
  context: Context,
  communicator: AsyncJSCommunicator,
  onConsoleMessage: (JSONObject) -> Unit
) : BackgroundPlayWebView(context, null) {
  val jsInterface = SigWebViewJavascriptInterface(this, communicator)

  var onLoad: SigWebView.() -> Unit = {}

  init {
    addJavascriptInterface(jsInterface, "Android")

    @SuppressLint("SetJavaScriptEnabled")
    settings.javaScriptEnabled = true
    settings.allowFileAccess = true

    (context as Activity).runOnUiThread {
      loadUrl("file:///android_asset/decipher.html")
    }

    webViewClient = object : WebViewClient() {
      override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
      ): WebResourceResponse? {
        return null
      }

      override fun onPageFinished(view: WebView?, url: String?) {
        onLoad()
        super.onPageFinished(view, url)
      }

      override fun onRenderProcessGone(view: WebView?, detail: RenderProcessGoneDetail?): Boolean {
        // returning false would make android kill the whole app, the main web view takes care of recovering
        (parent as? ViewGroup)?.removeView(this@SigWebView)
        destroy()
        return true
      }
    }

    webChromeClient = ConsoleLogChromeClient(onConsoleMessage)
  }
}
