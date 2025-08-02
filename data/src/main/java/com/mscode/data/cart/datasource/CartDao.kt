package com.mscode.data.cart.datasource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mscode.data.cart.model.CartProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartProduct(cartProductEntity: CartProductEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cartsLocal: List<CartProductEntity>)

    @Delete
    suspend fun deleteCartProduct(cartLocal: CartProductEntity): Int

    @Query("DELETE FROM cartProduct")
    suspend fun deleteCart()

    @Query("SELECT * FROM cartProduct")
    suspend fun getCart(): List<CartProductEntity>

    @Query("SELECT * FROM cartProduct")
    fun getCartByFlow(): Flow<List<CartProductEntity>>

    @Query("SELECT * FROM cartProduct WHERE id = :id")
    suspend fun getCartProductById(id: Int): CartProductEntity?
}