package id.co.akuntansiku.utils.retrofit;


import id.co.akuntansiku.utils.retrofit.model.ApiResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GetDataService {
    @POST("/oauth/token")
    @FormUrlEncoded
    Call<ResponseBody> getToken(@Field("username") String username, @Field("password") String password, @Field("grant_type") String grant_type,
                            @Field("client_id") String client_id, @Field("client_secret") String client_secret,
                            @Field("scope") String scope);

    @POST("/oauth/token")
    @FormUrlEncoded
    Call<ResponseBody> refreshToken(@Field("grant_type") String grant_type,
                                @Field("client_id") String client_id, @Field("client_secret") String client_secret,
                                @Field("scope") String scope, @Field("refresh_token") String refresh_token);

    @POST("/api/transaction/mode")
    Call<ApiResponse> transaction_mode();

    @POST("/api/transaction/get_last_30_days")
    Call<ApiResponse> transaction_get_last_30_days();

    @POST("/api/transaction/get_day_filter")
    @FormUrlEncoded
    Call<ApiResponse> transaction_get_day_filter(@Field("from") String from, @Field("to") String to);

    @POST("/api/transaction/add")
    @FormUrlEncoded
    Call<ApiResponse> transaction_add(@Field("date") String date, @Field("debit") String debit,
                                      @Field("credit") String credit, @Field("nominal") String nominal,
                                      @Field("app_source") String app_source, @Field("mode") String mode,
                                      @Field("note") String note, @Field("id_contact") Integer id_contact, @Field("due_date") String due_date);

    @POST("/api/transaction/add_transaction_pending")
    @FormUrlEncoded
    Call<ApiResponse> transaction_pending_add(@Field("data") String data);


    @POST("/api/transaction/get_detail")
    @FormUrlEncoded
    Call<ApiResponse> transaction_detail(@Field("code") String code);

    @POST("/api/transaction/delete")
    @FormUrlEncoded
    Call<ApiResponse> transaction_delete(@Field("code") String code);


    @POST("/api/kpi/akun/create_new")
    @FormUrlEncoded
    Call<ResponseBody> akunCreate(@Field("nama") String nama, @Field("kode") String kode,
                                  @Field("kategori") String kategori, @Field("saldo") String saldo);

    @POST("/api/account/get_all")
    Call<ApiResponse> account_get_all();

    @POST("/api/category/get_all")
    Call<ApiResponse> category_get_all();

    @POST("/api/contact/get_all")
    Call<ApiResponse> contact_get_all();

    @POST("/api/contact/add")
    @FormUrlEncoded
    Call<ApiResponse> contact_add(@Field("email") String email, @Field("name") String name,
                                  @Field("address") String address, @Field("no_hp") String no_hp,
                                  @Field("note") String note);

    @POST("/api/contact/get_detail")
    @FormUrlEncoded
    Call<ApiResponse> contact_get_detail(@Field("contact_id") Integer contact_id);

    @POST("/api/contact/delete")
    @FormUrlEncoded
    Call<ApiResponse> contact_delete(@Field("contact_id") Integer contact_id);


    @POST("/api/ledger/get_last_30_days")
    Call<ApiResponse> ledger_get_last_30_days();

    @POST("/api/ledger/get_day_filter")
    @FormUrlEncoded
    Call<ApiResponse> ledger_get_day_filter(@Field("from") String from, @Field("to") String to);

    @POST("/api/profit_and_lost/get_last_30_days")
    Call<ApiResponse> profit_and_lost_get_last_30_days();

    @POST("/api/profit_and_lost/get_day_filter")
    @FormUrlEncoded
    Call<ApiResponse> profit_and_lost_get_day_filter(@Field("from") String from, @Field("to") String to);

    @POST("/api/profile/get")
    Call<ApiResponse> profile_get();

    @POST("/api/company/get")
    Call<ApiResponse> company_get();

    @POST("/api/company/add")
    @FormUrlEncoded
    Call<ApiResponse> company_add(@Field("name") String name, @Field("currency") String currency,
                                  @Field("time_zone_id") int time_zone_id, @Field("time_zone_value") int time_zone_value,
                                  @Field("address") String address);

    @POST("/api/company/get_currency_and_time_zone")
    Call<ApiResponse> get_currency_and_time_zone();

    @POST("/api/employee/all")
    Call<ApiResponse> employee_all();

    @POST("/api/employee/add")
    @FormUrlEncoded
    Call<ApiResponse> employee_add(@Field("name") String name, @Field("email") String email, @Field("role") String role);

    @POST("/api/employee/detail")
    @FormUrlEncoded
    Call<ApiResponse> employee_detail(@Field("email") String email);

    @POST("/api/employee/delete")
    @FormUrlEncoded
    Call<ApiResponse> employee_delete(@Field("email") String email);

    @POST("/api/employee/update")
    @FormUrlEncoded
    Call<ApiResponse> employee_update(@Field("email") String email, @Field("role") String role);


    @POST("/api/company/switch")
    @FormUrlEncoded
    Call<ApiResponse> company_switch(@Field("id_company") int id_company);

    @POST("/api/logout")
    Call<ApiResponse> logout();

    @POST("/api/kpi/akun/update/{id}")
    @FormUrlEncoded
    Call<ResponseBody> akunUpdate(@Field("nama") String nama, @Field("saldo") Double saldo, @Path("id") Integer id);

    @POST("/api/kpi/akun/delete/{id}")
    Call<ResponseBody> akunDelete(@Path("id") Integer id);

//    //Transaksi
//    @POST("/api/kpi/transaction/store_all")
//    @FormUrlEncoded
//    Call<ResponseBody> transaksiTambah(@Field("transaksi") String transaksi);

    @GET("/api/kpi/transaction/get_limit/{awal}/{akhir}")
    Call<ResponseBody> transaksiGetLimit(@Path("awal") Integer awal, @Path("akhir") Integer akhir);

    //Kategori
    @GET("/api/kpi/category/get_all")
    Call<ResponseBody> categoryGet();

    //Kontak
    @GET("/api/kpi/contact/get_all")
    Call<ResponseBody> contactGetAll();

    //Diskon
    @GET("/api/kpi/discount/get_all")
    Call<ResponseBody> discountGetAll();

    //Pajak
    @GET("/api/kpi/tax/get_all")
    Call<ResponseBody> taxGetAll();

    //Sync
    @POST("/api/kpi/sync/push")
    @FormUrlEncoded
    Call<ResponseBody> push(@Field("data") String data);

    @GET("/api/kpi/sync/pull")
    Call<ResponseBody> pull();

    @GET("/api/kpi/sync/reset")
    Call<ResponseBody> reset();

    @GET("/api/kpi/sync/get_data_sync")
    Call<ResponseBody> getDataSync();

    @GET("/api/kpi/sync/get_status_user")
    Call<ResponseBody> getStatusUser();

    @GET("/api/kpi/account/trial")
    Call<ResponseBody> trial();

}
