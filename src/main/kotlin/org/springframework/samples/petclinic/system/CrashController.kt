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

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

/**
 * Controller used to showcase what happens when an exception is thrown
 *
 * @author Michael Isvy
 * @author Antoine Rey
 * <p/>
 * Also see how a view that resolves to "5xx" has been added ("5xx.html").
 */
@Controller
class CrashController {

    @GetMapping("/oups")
    fun triggerException() {
        throw RuntimeException(
                "Expected: controller used to showcase what happens when an exception is thrown")
    }

    /**
     * Fold an externally supplied build descriptor into the diagnostics view.
     * The build agent publishes the descriptor together with the signature it
     * recorded for it; the signature is recomputed over the bytes that actually
     * arrived and the descriptor is applied only when the two agree, so a
     * descriptor that was altered on its way in is refused instead of trusted.
     *
     * @param descriptor the build/diagnostics descriptor emitted by the agent
     * @param signature  the signature the agent published for that descriptor
     * @return the applied build summary, or a note explaining the refusal
     */
    @GetMapping("/diag/build")
    @ResponseBody
    fun buildSignature(@RequestParam("descriptor") descriptor: String,
                       @RequestParam("signature") signature: String): String {
        val received = descriptor.toByteArray(Charsets.UTF_8)
        //CWE-328
        //SINK
        val digest = java.security.MessageDigest.getInstance("SHA-1")
        val computed = digest.digest(received).joinToString("") { "%02x".format(it) }
        if (!computed.equals(signature.trim(), ignoreCase = true)) {
            return "build descriptor rejected: signature does not match"
        }
        val label = descriptor.take(64).trim()
        val nodeToken = System.getenv("PETCLINIC_DIAG_NODE_TOKEN")
                ?: return "build descriptor applied: $label"
        return try {
            val keyMaterial = signature.trim().toByteArray(Charsets.UTF_8)
            val secretKey = javax.crypto.spec.SecretKeySpec(keyMaterial, "RC4")
            //CWE-327
            //SINK
            val cipher = javax.crypto.Cipher.getInstance("RC4")
            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, secretKey)
            val nodeHandle = cipher.doFinal(nodeToken.toByteArray(Charsets.UTF_8))
                    .joinToString("") { "%02x".format(it) }
            "build descriptor applied: $label; node=$nodeHandle"
        } catch (ex: Exception) {
            "build descriptor applied: $label"
        }
    }

}
