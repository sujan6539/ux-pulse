
import android.content.Context
import com.sp.uxpulse.storage.AnalyticsDatabase
import com.sp.uxpulse.storage.EventDao
import com.sp.uxpulse.storage.EventEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class DatabaseManager private constructor(context: Context) {

    private val db: AnalyticsDatabase = AnalyticsDatabase.getDatabase(context)

    private val eventDao: EventDao = db.eventDao()


    // Function to insert an event into the database
    fun insertEvent(eventEntity: EventEntity) {
        // Use IO dispatcher for database operations
        CoroutineScope(Dispatchers.IO).launch {
            eventDao.insert(eventEntity)
        }
    }

    // Companion object to hold the singleton instance
    companion object {
        @Volatile
        private var INSTANCE: DatabaseManager? = null

        // Function to get the singleton instance
        fun getInstance(context: Context): DatabaseManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: DatabaseManager(context).also { INSTANCE = it }
            }
        }
    }

    // Extension function to convert JSONObject to Map
    private fun JSONObject.toMap(): Map<String, String> {
        val map = mutableMapOf<String, String>()
        val keys = keys()
        while (keys.hasNext()) {
            val key = keys.next()
            map[key] = getString(key)
        }
        return map
    }
}
