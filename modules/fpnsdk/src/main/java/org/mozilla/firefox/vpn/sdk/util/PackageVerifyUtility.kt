package org.mozilla.firefox.vpn.sdk.util

import android.annotation.TargetApi
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import java.io.InputStream
import java.security.PublicKey
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory

class PackageVerifyUtility {
    companion object {

        fun verifyPackageSignature(
            packageManager: PackageManager,
            packageName: String,
            certificateInputStream: InputStream
        ): Boolean {
            val publicKey = try {
                parseCertificatePublicKey(certificateInputStream)
            } catch(e: CertificateException) {
                return false
            }
            val signatures = getPackageSignature(packageManager, packageName)

            var verified = false
            for (signature in signatures) {
                verified = try {
                    val cert = parseCertificate(signature.toByteArray().inputStream())
                    cert.verify(publicKey)
                    true
                } catch (e: Exception) {
                    false
                }
                if (verified) {
                    break
                }
            }

            return verified
        }

        @Throws(CertificateException::class)
        private fun parseCertificatePublicKey(inputStream: InputStream): PublicKey {
            val cert = parseCertificate(inputStream)
            return cert.publicKey
        }

        @Throws(CertificateException::class)
        private fun parseCertificate(inputStream: InputStream): Certificate {
            return CertificateFactory.getInstance("X.509").generateCertificate(inputStream)
        }

        private fun getPackageSignature(
            packageManager: PackageManager,
            packageName: String
        ): Array<Signature> {
            val packageInfo = getPackageInfo(packageManager, packageName)
            return getSigners(packageInfo)
        }

        private fun getPackageInfo(
            packageManager: PackageManager,
            packageName: String
        ): PackageInfo {
            val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                PackageManager.GET_SIGNING_CERTIFICATES
            } else {
                PackageManager.GET_SIGNATURES
            }
            return packageManager.getPackageInfo(packageName, flags)
        }

        @TargetApi(Build.VERSION_CODES.P)
        private fun getSigners(packageInfo: PackageInfo): Array<Signature> {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val signingInfo = packageInfo.signingInfo
                return try {
                    signingInfo.signingCertificateHistory
                } catch (e: Exception) {
                    signingInfo.apkContentsSigners
                }
            } else {
                packageInfo.signatures
            }
        }

    }
}
