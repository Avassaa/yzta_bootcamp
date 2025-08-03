import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("journal/create")
    suspend fun createJournalEntry(
        @Header("Authorization") token: String,
        @Body request: JournalEntryRequest
    ): Response<CreateEntryResponse> // Or a more specific success response
}
data class JournalEntryRequest(
    val feelingText: String,
    val moodRating: Float,
    val entryPhotoUrl: String,
    val isPublic: Boolean = true
)