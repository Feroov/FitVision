import com.fitvision.models.Exercise
import retrofit2.http.GET
import retrofit2.Response

interface ApiService {
    @GET("fitvision.json")
    suspend fun getExercises(): Response<ExerciseList>
}

data class ExerciseList(
    val exercises: List<Exercise>
)
