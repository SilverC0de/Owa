package com.flux.owa;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class XClass {
    private static final String api = "https://api.owanow.co/alpha/";
    static String tutured = "tutored";
    static String sharedPreferences = "sp";
    static String buildAPI = "alpha"; //alpha
    static String outcast = "";
    static String name = "name";
    static String number = "number";
    static String gender = "gender";
    static String dob = "dob";
    static String okTested = "tess";
    static String induction = "induct";
    static String notification = "notifd";

    static String mail = "mail";
    static String tenant = "tenant";
    static String tenantImage = "tenantimg";

    static String tenantDesc = "tenanttxt";

    static String online = "online";
    static String address = "address";
    static String sendbird = "B762CF8E-FD50-4B31-A51B-96EE0AC48CA2";

    static String cardNumber = "cc";
    static String cardCvv = "v";
    static String cardMM = "md";
    static String cardYY = "yaf";

    static String apiAuthUser = api + "auth.php";
    private static String apiUpdateUser = api + "update.php";
    private static String apiPushChat = api + "chat.php";
    static String apiListings = api + "listings.php";
    static String apiDetails = api + "details.php";
    static String apiApartment = api + "apartment.php";
    static String apiAddToFavourites = api + "save.php";
    static String apiNotification = api + "notification.php";
    static String apiImage = api + "image.php";

    static String apiFavourites = api + "saved.php";
    static String apiPaystack = api + "paystack.php";

    static String paystackKei = "pk_test_219fe40f38e54f389a60160061bdcf153f2415d5";
    static String paystackKey = "pk_live_5f002e17e9fb61784f9db7395d4972c54b511a45";

    static String twilioApi = "https://api.twilio.com/2010-04-01/Accounts/AC670474f89e8a347a983b1acb713a5799/Messages.json";
    static String twilioAuth = "AC670474f89e8a347a983b1acb713a5799";
    static String twilioToken = "c2d5792ba9705edaf2d0fa1f11dff7ed";




    static void updateUser(String key, String value, String mail){

        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder().url(XClass.apiUpdateUser + "?key=" + key + "&value=" + value + "&mail=" + mail).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {

            }
        });

    }
    static void pushMessage(String m, String message, String locale, String to){
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder().url(XClass.apiPushChat + "?mail=" + m + "&message=" + message + "&locale=" + locale + "&to=" + to).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {

            }
        });
    }
}
