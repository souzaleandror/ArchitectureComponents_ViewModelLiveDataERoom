package br.com.alura.technews.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Noticia(
    @PrimaryKey(autoGenerate = true) var id: Long,

    val titulo: String = "",
    val texto: String = ""

)
