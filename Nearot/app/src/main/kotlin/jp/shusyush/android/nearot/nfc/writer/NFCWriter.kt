package jp.shusyush.android.nearot.nfc.writer

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef

class NFCWriter(context: Context,
                val nfcAdapter: NfcAdapter = NfcAdapter.getDefaultAdapter(context)) {

    var writeMode = false

    val ndefDetectFilter = arrayOf(IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED).apply {
        try {
            addDataType("text/plain")
        } catch (e: IntentFilter.MalformedMimeTypeException) {}
    })

    val writeTagFilter = arrayOf(IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED))

    fun enableWriteMode(activity: Activity, pendingIntent: PendingIntent) {
        writeMode = true
        nfcAdapter.enableForegroundDispatch(activity, pendingIntent,
                arrayOf(IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)), null)
    }

    fun disableWriteMode(activity: Activity) {
        writeMode = false
        nfcAdapter.disableForegroundDispatch(activity)
    }

    fun writeToTag(message: NdefMessage, tag: Tag) = Ndef.get(tag)?.afterConnected {
        if (!isWritable) {
            return false
        }

        if (maxSize < message.toByteArray().size) {
            return false
        }

        writeNdefMessage(message)

        return true
    } ?: false

}

inline fun <R> Ndef.afterConnected(block: Ndef.() -> R) = try {
    if (!isConnected) {
        connect()
    }
    block()
} finally {
    if (isConnected) {
        close()
    }
}

inline fun <reified T : Activity>
        createIntent(context: Context,
                     configure: Intent.() -> Unit) = Intent(context, T::class.java).apply(configure)
