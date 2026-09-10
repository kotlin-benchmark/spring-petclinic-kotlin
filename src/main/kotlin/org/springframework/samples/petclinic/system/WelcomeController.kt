/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.system

import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import javax.net.ssl.HostnameVerifier

/**
 * @author Antoine Rey
 */
@Controller
class WelcomeController {

    @GetMapping("/")
    fun welcome(): String = "welcome"

    @GetMapping("/status/sync")
    @ResponseBody
    fun syncClinicStatus(): String {
        val statusEndpoint = "https://status.internal.clinic/feed"
        val feedClient = OkHttpClient.Builder()
                //CWE-295
                //SINK
                .hostnameVerifier(HostnameVerifier { _, _ -> true })
                .build()
        return try {
            //CWE-798
            //SOURCE
            val statusPassword = "St@tusFeed_2024#"
            val request = Request.Builder()
                    .url(statusEndpoint)
                    //CWE-798
                    //SINK
                    .header("Authorization", okhttp3.Credentials.basic("status-agent", statusPassword))
                    .build()
            feedClient.newCall(request).execute().use { response ->
                if (response.isSuccessful) "status-synced" else "status-unavailable"
            }
        } catch (ex: Exception) {
            "status-unavailable"
        }
    }
}
