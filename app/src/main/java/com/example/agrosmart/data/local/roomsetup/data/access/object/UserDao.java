package com.example.agrosmart.data.local.roomsetup.data.access.object;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.agrosmart.domain.models.User;

@Dao // clase dao de room para hacer las operaciones en la base de datos
public interface UserDao {

    @Insert
    void insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);
}
