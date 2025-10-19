package com.polaris.hospitalitypos.core.ai

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SecureConfigStore @Inject constructor(
    @ApplicationContext context: Context
) {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val prefs = EncryptedSharedPreferences.create(
        "ai_config",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun getApiKey(): String? = prefs.getString(KEY_CHATGPT_API, null)

    fun setApiKey(value: String) {
        prefs.edit().putString(KEY_CHATGPT_API, value).apply()
    }

    fun clearApiKey() {
        prefs.edit().remove(KEY_CHATGPT_API).apply()
    }

    companion object {
        private const val KEY_CHATGPT_API = "chatgpt_api_key"
    }
}
