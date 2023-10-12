package br.com.alura.technews.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.alura.technews.model.Noticia
import br.com.alura.technews.repository.NoticiaRepository
import br.com.alura.technews.repository.Resource

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

//    private val liveData = MutableLiveData<List<Noticia>>();

    fun buscaTodos() : LiveData<Resource<List<Noticia>?>> {

        //repository.buscaTodos(quandoSucesso, quandoFalha)
//        repository.buscaTodos(quandoSucesso = {
//            noticasNovas -> liveData.value = noticasNovas;
//        }, quandoFalha = {});
//        return liveData;
        return repository.buscaTodos();
    }

}