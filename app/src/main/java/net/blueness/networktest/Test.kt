package net.blueness.networktest

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by Blueness on 2017/8/29.
 */

class Test {

    private fun paresJSONWithGSON(jsonData: String) {
        val gson = Gson()
        val appList = gson.fromJson<List<App>>(jsonData, object : TypeToken<List<App>>() {

        }.type)
    }
}
