package com.example.dailysync.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(
    // ★ Improved: Context ではなく DataStore を直接注入する形に変更
    // これにより @ApplicationContext の警告問題が根本から解消され、
    // クラスが Android Framework (Context) に依存しなくなります。
    private val dataStore: DataStore<Preferences>,
) {
    // Kotlinの命名規則に従い、キャメルケースを使用
    private val issueTrackerUrlBaseKey = stringPreferencesKey("issue_tracker_url_base")

    val issueTrackerUrlBase: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[issueTrackerUrlBaseKey] ?: ""
        }

    suspend fun setIssueTrackerUrlBase(url: String) {
        dataStore.edit { preferences ->
            preferences[issueTrackerUrlBaseKey] = url
        }
    }
}
