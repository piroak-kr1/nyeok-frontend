package com.piroak.nyeok.network

import com.google.maps.routing.v2.ComputeRoutesResponse
import com.google.maps.routing.v2.Route
import com.google.protobuf.util.JsonFormat
import com.piroak.nyeok.common.Coordinate
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.lang.reflect.Type

interface AppApiService {
    @GET("/")
    suspend fun hello(): String

    @GET("/echo")
    suspend fun echo(@Query("message") message: String): String

    @GET("/compute_routes_sample")
    suspend fun computeRoutesSample(): ComputeRoutesResponse

    @POST("/compute_routes")
    suspend fun computeRoutes(
        @Body request: RouteRequest
    ): ComputeRoutesResponse
}

data class RouteRequest(
    val origin: Coordinate,
    val destination: Coordinate,
)

class ComputeRoutesResponseConverterFactory : Converter.Factory() {
    companion object {
        fun create(): ComputeRoutesResponseConverterFactory {
            return ComputeRoutesResponseConverterFactory()
        }
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return if (type == ComputeRoutesResponse::class.java) {
            ComputeRoutesResponseConverter()
        } else {
            null
        }
    }

    class ComputeRoutesResponseConverter : Converter<ResponseBody, ComputeRoutesResponse> {
        override fun convert(value: ResponseBody): ComputeRoutesResponse? {
            val jsonString = value.string()
            val builder: ComputeRoutesResponse.Builder = ComputeRoutesResponse.newBuilder()
            JsonFormat.parser().merge(jsonString, builder)
            return builder.build()
        }
    }
}