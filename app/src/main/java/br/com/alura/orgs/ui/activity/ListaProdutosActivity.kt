package br.com.alura.orgs.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import br.com.alura.orgs.R
import br.com.alura.orgs.database.AppDatabase
import br.com.alura.orgs.databinding.ActivityListaProdutosActivityBinding
import br.com.alura.orgs.ui.recyclerview.adapter.ListaProdutosAdapter
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class ListaProdutosActivity : AppCompatActivity() {

    private val adapter = ListaProdutosAdapter(context = this)
    private val binding by lazy {
        ActivityListaProdutosActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraRecyclerView()
        configuraFab()
    }

    override fun onResume() {
        super.onResume()
        val db = AppDatabase.instancia(this)
        val pprodutoDao = db.produtoDao()
        val scope = MainScope()
        scope.launch {
            val produtos = withContext(IO) {
                pprodutoDao.buscaTodos()
            }
            adapter.atualiza(produtos)
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_ordenacao, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.nomeDesc -> {
//                adapter.atualiza(produtoDao.buscarPorNomeDesc())
//            }
//            R.id.nomeAsc -> {
//                adapter.atualiza(produtoDao.buscarPorNomeAsc())
//            }
//            R.id.descricaoDesc -> {
//                adapter.atualiza(produtoDao.buscarPorDescricaoDesc())
//            }
//            R.id.descricaoAsc -> {
//                adapter.atualiza(produtoDao.buscarPorDescricaoAsc())
//            }
//            R.id.valorDesc -> {
//                adapter.atualiza(produtoDao.buscarPorValorDesc())
//            }
//            R.id.valorAsc -> {
//                adapter.atualiza(produtoDao.buscarPorValorAsc())
//            }
//            R.id.semOrdenacao -> {
//                adapter.atualiza(produtoDao.buscaTodos())
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

    private fun configuraFab() {
        val fab = binding.activityListaProdutosFab
        fab.setOnClickListener {
            vaiParaFormularioProduto()
        }
    }

    private fun vaiParaFormularioProduto() {
        val intent = Intent(this, FormularioProdutoActivity::class.java)
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun configuraRecyclerView() {
        val recyclerView = binding.activityListaProdutosRecyclerView
        recyclerView.adapter = adapter
        adapter.quandoClicaNoItem = {
            val intent = Intent(
                this, DetalhesProdutoActivity::class.java
            ).apply {
                putExtra(CHAVE_PRODUTO_ID, it.id)
            }
            startActivity(intent)
        }
//        adapter.deteletar = {
//            produtoDao.remove(it)
//        }
    }

}