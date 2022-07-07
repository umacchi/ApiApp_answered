package jp.techacademy.tarokirameki.apiapp

import com.google.gson.annotations.SerializedName
data class ApiResponse(
    @SerializedName("results")
    var results: Results
)

data class Results(
    @SerializedName("shop")
    var shop: List<Shop>
)

data class Shop(
    @SerializedName("id")
    var id: String,
    @SerializedName("coupon_urls")
    var couponUrls: CouponUrls,
    @SerializedName("logo_image")
    var logoImage: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("address")
    var address: String
)

data class CouponUrls(
    @SerializedName("pc")
    var pc: String,
    @SerializedName("sp")
    var sp: String
)