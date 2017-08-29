package net.blueness.networktest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.find
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var responseText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sendRequest: Button = find(R.id.send_request)
        responseText = find(R.id.response_text)
        sendRequest.setOnClickListener(this)
    }

    override fun onClick(v: View){
        if (v.id == R.id.send_request){
//            sendRequestWithHttpConnection()
            sendRequestWithOkHttp()
        }
    }

    private fun sendRequestWithHttpConnection() {
        object: Thread() {
            override fun run(){
                var connection: HttpURLConnection? = null
                var reader: BufferedReader? = null
                try {
                    val url = URL("https://www.baidu.com")
                    connection = url.openConnection() as HttpURLConnection?
                    connection!!.requestMethod = "GET"
                    connection!!.connectTimeout = 8000
                    connection!!.readTimeout = 8000
                    val inputStream: InputStream? = connection.inputStream
                    reader = BufferedReader(InputStreamReader(inputStream))
                    var response = StringBuilder()
                    var line: String? = null
                    do {
                        line = reader.readLine()
                        response.append(line)
                    } while (line != null)
                    showResponse(response.toString())
                }
                catch (e: Exception){
                    e.printStackTrace()
                }
                finally {
                    if (reader != null) reader.close()
                    if (connection != null) connection.disconnect()
                }
            }
        }.start()
    }

    private fun sendRequestWithOkHttp(){
        object: Thread(){
            override fun run() {
                val client = OkHttpClient()
//                val request = Request.Builder().url("https://baidu.com").build()
                val request = Request.Builder().url("").build()
                val response = client.newCall(request).execute()
                val responseData = response.body()!!.string()
//                showResponse(responseData)

                paresJSONWithJSONObject(responseData)

//                paresJSONWithGSON(responseData)
            }
        }.start()
    }


    private fun showResponse(response: String) {
        runOnUiThread{
            responseText?.text = response
        }
    }

    private fun paresJSONWithJSONObject(jsonData: String?) {
        val jsonArray = JSONArray(jsonData)
        for (i in 0..jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(i)
            val id = jsonObject.getString("id")
            val name = jsonObject.getString("name")
            val version = jsonObject.getString("version")
            Log.i("MainActivity", "id is" + id)
            Log.i("MainActivity", "name is" + name)
            Log.i("MainActivity", "version is" + version)
        }
    }

    private fun paresJSONWithGSON(jsonData: String?) {
        val gson = Gson()
        val appList: MutableList<App> =
                gson.fromJson(jsonData,object: TypeToken<MutableList<App>>(){}.type)
        for (app in appList){
            Log.i("MainActivity", "id is" + app.id)
            Log.i("MainActivity", "name is" + app.name)
            Log.i("MainActivity", "version is" + app.version)
        }
    }
}
