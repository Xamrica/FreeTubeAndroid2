package io.freetubeapp.freetube.webviews

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import io.freetubeapp.freetube.R
import io.freetubeapp.freetube.activities.FreeTubeActivity
import io.freetubeapp.freetube.helpers.WindowInsetsControllerWrapper
import io.freetubeapp.freetube.helpers.spoofDesktopUserAgent
import io.freetubeapp.freetube.javascript.FreeTubeJavaScriptInterface
import io.freetubeapp.freetube.javascript.dispatchEvent
import org.json.JSONObject

@SuppressLint("ViewConstructor")
class FreeTubeWebView (
  context: FreeTubeActivity
) : BackgroundPlayWebView(context, null) {
  private val activity = context
  private var fullscreenView: View? = null
  private var fullscreenViewCallback: WebChromeClient.CustomViewCallback? = null
  val isFullscreen: Boolean
    get() {
      return fullscreenView != null
    }
  private val windowInsetsControllerWrapper = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && windowInsetsController != null) {
    WindowInsetsControllerWrapper(windowInsetsController)
  } else {
    WindowInsetsControllerWrapper(context.windowInsetsController)
  }
  val jsInterface = FreeTubeJavaScriptInterface(context, this)

  val onConsoleMessage: (JSONObject) -> Unit = { messageData: JSONObject ->
    context.state.consoleMessages.add(messageData)
    dispatchEvent("console-message", "data", messageData)
  }

  init {
    spoofDesktopUserAgent()
    layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
    setBackgroundColor(Color.TRANSPARENT)

    @SuppressLint("SetJavaScriptEnabled")
    settings.javaScriptEnabled = true
    // add the JavaScript interface
    addJavascriptInterface(jsInterface, "Android")

    // this is the 🥃 special sauce that makes local api streaming a possibility
    @Suppress("DEPRECATION")
    settings.allowUniversalAccessFromFileURLs = true
    @Suppress("DEPRECATION")
    settings.allowFileAccessFromFileURLs = true
    // allow playlist ▶auto-play in background
    settings.mediaPlaybackRequiresUserGesture = false

    webViewClient = object: WebViewClient() {
      override fun onPageFinished(view: WebView?, url: String?) {
        context.state.currentPage = url
        super.onPageFinished(view, url)
      }
      override fun onRenderProcessGone(view: WebView?, detail: RenderProcessGoneDetail?): Boolean {
        // the renderer crashed or was killed by the system (e.g. to reclaim memory while the app was in the background),
        // returning false would make android kill the whole app, so start over with a fresh web view instead
        if (!activity.isFinishing && !activity.isDestroyed) {
          activity.recreate()
        }
        return true
      }
      override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        if (request?.url?.scheme == "file") {
          // don't send file url requests to a web browser (it will crash the app)
          return true
        }

        val regex = context.getString(R.string.youtube_regex)

        val urlString = request?.url?.toString()
        if (urlString != null && Regex(regex).containsMatchIn(urlString)) {
          dispatchEvent("youtube-link", "link", urlString)
          return true
        }
        // send all requests to a real web browser
        context.startActivity(
          Intent(Intent.ACTION_VIEW, request?.url)
        )
        return true
      }
    }

    webChromeClient = object: ConsoleLogChromeClient({ message ->
      onConsoleMessage(message)
    }) {
      override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
        if (view != null) {
          val viewGroup = (parent as ViewGroup)

          // hide system ui
          viewGroup.fitsSystemWindows = false

          windowInsetsControllerWrapper.hide(WindowInsetsCompat.Type.systemBars())
          windowInsetsControllerWrapper.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

          viewGroup.addView(view)
          fullscreenView = view
          fullscreenViewCallback = callback
          dispatchEvent("start-fullscreen")
        }
      }

      override fun onHideCustomView() {
        hideFullscreenView()
      }
    }
  }

  /**
   * exits fullscreen immediately on the native side,
   * instead of waiting for the page to call `document.exitFullscreen()`
   */
  fun exitFullscreen() {
    if (fullscreenView == null) return
    // ask chromium to leave fullscreen, this usually calls `onHideCustomView` right away
    fullscreenViewCallback?.onCustomViewHidden()
    // clean up in case chromium didn't
    hideFullscreenView()
  }

  private fun hideFullscreenView() {
    val view = fullscreenView ?: return
    fullscreenView = null
    fullscreenViewCallback = null

    val viewGroup = (parent as ViewGroup)

    // show system ui
    viewGroup.fitsSystemWindows = true
    windowInsetsControllerWrapper.show(WindowInsetsCompat.Type.systemBars())

    viewGroup.removeView(view)
    activity.restoreOrientation()
    dispatchEvent("end-fullscreen")

    // rendering is handed back to this view, make sure chromium doesn't consider the page hidden
    post {
      forceWindowVisible()
      invalidate()
    }
  }

  fun generateBgWebview(): BotGuardWebView {
    return BotGuardWebView(context, onConsoleMessage)
  }

  fun generateSigWebview(): SigWebView {
    val view = SigWebView(context, jsInterface.jsCommunicator, onConsoleMessage)
    val viewGroup = (parent as ViewGroup)
    viewGroup.addView(view)
    view.visibility = GONE
    return view
  }
}
