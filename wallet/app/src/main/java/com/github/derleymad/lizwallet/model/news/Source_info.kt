import com.google.gson.annotations.SerializedName



data class Source_info (

	@SerializedName("name") val name : String,
	@SerializedName("img") val img : String,
	@SerializedName("lang") val lang : String
)