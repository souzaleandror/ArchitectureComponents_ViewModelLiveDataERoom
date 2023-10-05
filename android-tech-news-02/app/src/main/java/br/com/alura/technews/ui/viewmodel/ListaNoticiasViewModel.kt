package br.com.alura.technews.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import br.com.alura.technews.model.Noticia
import br.com.alura.technews.repository.NoticiaRepository

class ListaNoticiasViewModel(
    private val repository: NoticiaRepository
) : ViewModel() {

    init {
        Log.i("viewmodel", "criando um view model")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("viewmodel", "Destruindo viewmodel")
    }

    fun buscaTodos(quandoSucesso: (noticiasNovas: List<Noticia>) -> Unit, quandoFalha: (erro: String?) -> Unit) {
        repository.buscaTodos(quandoSucesso, quandoFalha)
    }

}