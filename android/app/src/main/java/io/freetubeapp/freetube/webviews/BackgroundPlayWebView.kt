package io.freetubeapp.freetube.webviews

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView

open class BackgroundPlayWebView @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null
) : WebView(context, attrs) {
  private var once: Boolean = false
  override fun onWindowVisibilityChanged(visibility: Int) {
    if (once) return
    if (visibility != GONE) super.onWindowVisibilityChanged(VISIBLE)
    once = true
  }

  /**
   * tells chromium that the window is visible, bypassing the background play override
   *
   * while a fullscreen custom view is shown, chromium listens to the window visibility of that view instead,
   * so if the window gets hidden during fullscreen (e.g. the phone is locked) the page stays hidden
   * (and blank) after rendering is handed back to this web view, since the override above swallows the resume
   */
  fun forceWindowVisible() {
    super.onWindowVisibilityChanged(VISIBLE)
  }
}
