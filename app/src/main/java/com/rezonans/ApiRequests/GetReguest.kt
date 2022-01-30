package com.rezonans.ApiRequests

import android.os.AsyncTask
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class GetReguest : AsyncTask<String, Int, String>() {
    override fun doInBackground(vararg parts: String): String? {
        val requestURL = parts.first()
        val queryString = parts.last()

        val connection: HttpURLConnection = URL(requestURL).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Authorization", queryString)
        connection.connect()


        var responseCode = connection.responseCode

        var response = StringBuffer()

        if (responseCode== HttpURLConnection.HTTP_OK) {
            // Create an input stream to read the response
            val inputStream = BufferedReader(InputStreamReader(connection.inputStream)).use {
                // Container for input stream data
                response = StringBuffer()
                var inputLine = it.readLine()
                // Add each line to the response container
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }

                it.close()
                // TODO: Add main thread callback to parse response
                //println(">>>> Response: $response")
            }}

        else {
            val inputStr = BufferedReader(InputStreamReader(connection.errorStream)).use {
                // Container for input stream data
                response = StringBuffer()
                var inputLine = it.readLine()
                // Add each line to the response container
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }

                it.close()
                // TODO: Add main thread callback to parse response
                //println(">>>> Response: $response")
            }
        }

        connection.disconnect()

        return response.toString()
    }

    protected fun onProgressUpdate(vararg progress: Int) {
    }

    override fun onPostExecute(result: String?) {
    }
}