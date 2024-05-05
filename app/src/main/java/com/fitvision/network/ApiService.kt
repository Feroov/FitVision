import com.fitvision.models.Exercise
import com.fitvision.models.FoodList
import retrofit2.http.GET
import retrofit2.Response

interface ApiService {
    @GET("fitvision.json")
    suspend fun getExercises(): Response<ExerciseList>

    @GET("food.json")
    suspend fun getFoods(): Response<FoodList>
}

data class ExerciseList(
    val exercises: List<Exercise>
)
