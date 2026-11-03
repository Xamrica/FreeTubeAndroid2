package io.freetubeapp.freetube.javascript

import android.content.Context
import android.webkit.WebView
import org.json.JSONObject
import java.nio.charset.StandardCharsets

/**
 * fires and forgets javascript
 * @param js JavaScript string to be evaluated
 */
fun WebView.fafJS(js: String) {
  post {
    // not `loadUrl("javascript: ...")`, that percent-decodes the script, which lets
    // data containing sequences like %22 break out of the string literals it was placed in
    evaluateJavascript(js, null)
  }
}

/**
 * calls `window.dispatchEvent` with just the event name
 */
fun WebView.dispatchEvent(eventName: String) {
  fafJS("window.dispatchEvent(new Event(${JSONObject.quote(eventName)}))")
}

/**
 * calls `window.dispatchEvent` with the given JSON assigned to the event which is dispatched
 */
fun WebView.dispatchEvent(eventName: String, event: JSONObject) {
  var js = "var tempVar = new Event(${JSONObject.quote(eventName)});"
  js += "Object.assign(tempVar, $event);"
  js += "window.dispatchEvent(tempVar);"
  fafJS(js)
}

/**
 * calls `window.dispatchEvent` with an event with a single custom key with a string value
 */
fun WebView.dispatchEvent(eventName: String, keyName: String, data: String) {
  val wrapper = JSONObject()
  wrapper.put(keyName, data)
  dispatchEvent(eventName, wrapper)
}

/**
 * calls `window.dispatchEvent` with an event with a single custom key with a long value
 */
fun WebView.dispatchEvent(eventName: String, keyName: String, data: Long) {
  val wrapper = JSONObject()
  wrapper.put(keyName, data)
  dispatchEvent(eventName, wrapper)
}

/**
 * calls `window.dispatchEvent` with an event with a single custom key with a JSON value
 */
fun WebView.dispatchEvent(eventName: String, keyName: String, data: JSONObject) {
  val wrapper = JSONObject()
  wrapper.put(keyName, data)
  dispatchEvent(eventName, wrapper)
}

/**
 * encodes a string message for transport across the java bridge
 * @param message the message to be encoded
 */
fun btoa(message: String): String {
  return "atob(\"${String(
    // line breaks aren't allowed inside of the js string literal
    android.util.Base64.encode(message.toByteArray(), android.util.Base64.NO_WRAP),
    StandardCharsets.UTF_8
  )}\")"
}

/**
 * @param message the message to log
 * @param level used in js as "console.$level" (ex: log, warn, error)
 */
fun WebView.consoleLog(message: String, level: String = "log") {
  fafJS("console.$level(${btoa(message)})")
}

fun WebView.setScale(scale: Double, context: Context) {
  post {
    if (scale == 0.0) {
      setInitialScale(0)
    } else {

      val feelsLike =
        context.resources.displayMetrics.widthPixels / context.resources.displayMetrics.density

      val percentageOfWidth = feelsLike / context.resources.displayMetrics.widthPixels

      setInitialScale(((1 / percentageOfWidth) * (scale * 100)).toInt())
    }
  }
}
