package com.polaris.hospitalitypos.core.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface HospitalityApi {
    @POST("/v1/reservations")
    suspend fun createReservation(@Body payload: ReservationRequestDto): ReservationResponseDto

    @PUT("/v1/reservations/{id}")
    suspend fun updateReservation(@Path("id") id: String, @Body payload: ReservationRequestDto): ReservationResponseDto

    @POST("/v1/payments")
    suspend fun createPayment(@Body payload: PaymentRequestDto): PaymentResponseDto

    @GET("/v1/housekeeping/rooms")
    suspend fun getHousekeepingStatus(): RoomsResponseDto

    @GET("/v1/search")
    suspend fun globalSearch(): SearchResponseDto

    @GET("/v1/reports/{type}")
    suspend fun getReport(@Path("type") type: String): ReportResponseDto

    @GET("/v1/ai/insights")
    suspend fun getAiInsights(): AiInsightResponseDto
}

@JsonClass(generateAdapter = true)
data class ReservationRequestDto(
    @Json(name = "property_id") val propertyId: String,
    @Json(name = "room_id") val roomId: String,
    @Json(name = "guest_id") val guestId: String,
    @Json(name = "check_in") val checkIn: String,
    @Json(name = "check_out") val checkOut: String,
    @Json(name = "nightly_rate") val nightlyRate: Double,
    val status: String,
    val channel: String,
    @Json(name = "balance_due") val balanceDue: Double
)

@JsonClass(generateAdapter = true)
data class ReservationResponseDto(
    val id: String,
    @Json(name = "updated_at") val updatedAt: String
)

@JsonClass(generateAdapter = true)
data class PaymentRequestDto(
    @Json(name = "reservation_id") val reservationId: String,
    val amount: Double,
    val method: String
)

@JsonClass(generateAdapter = true)
data class PaymentResponseDto(
    val id: String,
    val status: String
)

@JsonClass(generateAdapter = true)
data class RoomsResponseDto(
    val rooms: List<HousekeepingRoomDto>
)

@JsonClass(generateAdapter = true)
data class HousekeepingRoomDto(
    val id: String,
    val status: String,
    @Json(name = "updated_at") val updatedAt: String
)

@JsonClass(generateAdapter = true)
data class SearchResponseDto(
    val reservations: List<ReservationSearchDto>,
    val guests: List<GuestSearchDto>
)

@JsonClass(generateAdapter = true)
data class ReservationSearchDto(
    val id: String,
    val guest: String,
    val room: String,
    val status: String
)

@JsonClass(generateAdapter = true)
data class GuestSearchDto(
    val id: String,
    val name: String,
    val phone: String?
)

@JsonClass(generateAdapter = true)
data class ReportResponseDto(
    val type: String,
    val generatedAt: String,
    val payload: Map<String, Any?>
)

@JsonClass(generateAdapter = true)
data class AiInsightResponseDto(
    val summary: String,
    val anomalies: List<String>
)
