package br.com.alura.orgs.ui.recyclerview.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import br.com.alura.orgs.R
import br.com.alura.orgs.databinding.ProdutoItemBinding
import br.com.alura.orgs.extensions.formataParaMoedaBrasileira
import br.com.alura.orgs.extensions.tentaCarregarImagem
import br.com.alura.orgs.model.Produto

class ListaProdutosAdapter(
    private val context: Context,
    produtos: List<Produto> = emptyList(),
    var quandoClicaNoItem: (produto: Produto) -> Unit = {},
    var deteletar: (produto: Produto) -> Unit = {},
    ) : RecyclerView.Adapter<ListaProdutosAdapter.ViewHolder>() {

    private val produtos = produtos.toMutableList()

    inner class ViewHolder(private val binding: ProdutoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var produto: Produto
        init {
            itemView.setOnClickListener {
                if (::produto.isInitialized) {
                    quandoClicaNoItem(produto)
                }
            }
//            itemView.setOnLongClickListener {
//                vaiParaFormularioProduto(it)
//            }
        }
        private fun vaiParaFormularioProduto(v: View): Boolean {
            showPopup(v, popMenu)
            return true
        }

        private fun showPopup(v: View, popupMenu: PopupMenu.OnMenuItemClickListener) {
            PopupMenu(context, v).apply {
                setOnMenuItemClickListener(popupMenu)
                inflate(R.menu.menu_detalhes_produto)
                show()
            }
        }

        private val popMenu = PopupMenu.OnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.menu_detalhes_produto_editar -> {
                    Toast.makeText(context, "editando", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_detalhes_produto_deletar -> {
                    Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show()
                    deteletar(produto)
                    true
                }
                else -> false
            }
        }

        fun vincula(produto: Produto) {
            this.produto = produto
            val nome = binding.produtoItemNome
            nome.text = produto.nome
            val descricao = binding.produtoItemDescricao
            descricao.text = produto.descricao
            val valor = binding.produtoItemValor
            val valorEmMoeda: String = produto.valor.formataParaMoedaBrasileira()
            valor.text = valorEmMoeda
            val visibilidade = if (produto.imagem != null) {
                View.VISIBLE
            } else {
                View.GONE
            }
            binding.imageView.visibility = visibilidade
            binding.imageView.tentaCarregarImagem(produto.imagem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ProdutoItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produto = produtos[position]
        holder.vincula(produto)
    }

    override fun getItemCount(): Int = produtos.size

    fun atualiza(produtos: List<Produto>) {
        this.produtos.clear()
        this.produtos.addAll(produtos)
        notifyDataSetChanged()
    }

}
