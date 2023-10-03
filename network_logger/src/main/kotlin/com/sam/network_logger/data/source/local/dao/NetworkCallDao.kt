package com.sam.network_logger.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sam.network_logger.data.source.local.entity.NetworkCall
import kotlinx.coroutines.flow.Flow

@Dao
interface NetworkCallDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNetworkCall(networkCall: NetworkCall)

    @Query("SELECT * FROM networkcall")
    fun getAllNetworkCallFlow(): Flow<List<NetworkCall>>

    @Query("SELECT * FROM networkcall")
    fun getAllNetworkCall(): List<NetworkCall>?

    @Query("DELETE FROM networkcall")
    fun nukeNetworkCalls()
}