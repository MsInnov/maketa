package com.mscode.data.cart.datasource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mscode.data.cart.model.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartProduct(cartEntity: CartEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cartsEntities: List<CartEntity>)

    @Delete
    suspend fun deleteCartProduct(cartEntity: CartEntity): Int

    @Query("DELETE FROM cart")
    suspend fun deleteCart()

    @Query("SELECT * FROM cart")
    suspend fun getCart(): List<CartEntity>

    @Query("SELECT * FROM cart")
    fun getCartByFlow(): Flow<List<CartEntity>>

    @Query("SELECT * FROM cart WHERE id = :id")
    suspend fun getCartProductById(id: Int): CartEntity?
}