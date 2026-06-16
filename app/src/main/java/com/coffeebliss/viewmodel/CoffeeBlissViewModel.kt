package com.coffeebliss.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.coffeebliss.data.local.CoffeeBlissDatabase
import com.coffeebliss.data.local.entity.Member
import com.coffeebliss.data.repository.CoffeeBlissRepository
import com.coffeebliss.utils.SessionManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class UiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: String? = null
)

class CoffeeBlissViewModel(application: Application) : AndroidViewModel(application) {

    private val db = CoffeeBlissDatabase.getDatabase(application)
    private val repository = CoffeeBlissRepository(
        db.memberDao(), db.transactionDao(), db.redeemDao()
    )
    private val sessionManager = SessionManager(application)

    // Session
    val currentMemberId: StateFlow<Int> = sessionManager.memberId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), -1)

    // Current member data
    val currentMember: StateFlow<Member?> = currentMemberId.flatMapLatest { id ->
        if (id != -1) repository.getMemberById(id) else flowOf(null)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Transactions for current member
    val transactions = currentMemberId.flatMapLatest { id ->
        if (id != -1) repository.getTransactionsByMember(id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Redeems for current member
    val redeems = currentMemberId.flatMapLatest { id ->
        if (id != -1) repository.getRedeemsByMember(id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // ======================== AUTH ========================

    fun register(name: String, email: String, phone: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val existing = repository.getMemberByEmail(email)
                if (existing != null) {
                    onResult(false, "Email sudah terdaftar!")
                    return@launch
                }
                val count = repository.getMemberCount()
                val memberNumber = repository.generateMemberNumber(count)
                val member = Member(
                    name = name,
                    email = email,
                    phone = phone,
                    memberNumber = memberNumber
                )
                val id = repository.registerMember(member)
                sessionManager.saveMemberId(id.toInt())
                onResult(true, "Registrasi berhasil! Selamat datang, $name!")
            } catch (e: Exception) {
                onResult(false, "Terjadi kesalahan: ${e.message}")
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun login(email: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val member = repository.getMemberByEmail(email)
                if (member != null) {
                    sessionManager.saveMemberId(member.id)
                    onResult(true, "Login berhasil! Selamat datang, ${member.name}!")
                } else {
                    onResult(false, "Email tidak ditemukan. Silakan daftar terlebih dahulu.")
                }
            } catch (e: Exception) {
                onResult(false, "Terjadi kesalahan: ${e.message}")
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
        }
    }

    // ======================== TRANSACTION ========================

    fun addTransaction(amount: Long, onResult: (Int) -> Unit) {
        viewModelScope.launch {
            val memberId = currentMemberId.value
            if (memberId == -1) return@launch
            val points = repository.recordTransaction(memberId, amount)
            // Update status
            val member = currentMember.value
            if (member != null) {
                repository.updateMemberStatus(memberId, member.totalPoints + points)
            }
            onResult(points)
        }
    }

    // ======================== REDEEM ========================

    fun redeemReward(rewardName: String, pointsRequired: Int, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val member = currentMember.value ?: run {
                onResult(false, "Member tidak ditemukan")
                return@launch
            }
            if (member.totalPoints < pointsRequired) {
                onResult(false, "Poin tidak cukup! Kamu butuh $pointsRequired poin, saat ini ${member.totalPoints} poin.")
                return@launch
            }
            repository.redeemReward(member.id, rewardName, pointsRequired)
            repository.updateMemberStatus(member.id, member.totalPoints - pointsRequired)
            onResult(true, "Berhasil menukar poin dengan $rewardName! Nikmati minumanmu ☕")
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null, success = null) }
    }
}
