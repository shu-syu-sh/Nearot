package jp.shusyush.android.nearot

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import jp.shusyush.android.nearot.nfc.writer.NFCWriter
import jp.shusyush.android.nearot.nfc.writer.createIntent

class WriteLotActivity : AppCompatActivity() {

    lateinit var nfcPendingIntent: PendingIntent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_lot)

        val writer = NFCWriter(this)
        val nfcIntent = createIntent<WriteLotActivity>(this) { addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) }
        nfcPendingIntent = createPendingIntent(nfcIntent)
    }

    fun createPendingIntent(intent: Intent) = PendingIntent.getActivity(this, 0, intent, 0)

}
