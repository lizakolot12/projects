package proj.kolot.uzsearch

import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import proj.kolot.uzsearch.settings.SettingsActivity

open class MainActivity : AppCompatActivity() {


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_settings -> {
                val intent = android.content.Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
}
