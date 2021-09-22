package br.senac.devweb.api.product.produto;

import br.senac.devweb.api.product.categoria.Categoria;
import br.senac.devweb.api.product.categoria.CategoriaService;
import br.senac.devweb.api.product.exceptions.NotFoundException;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProdutoService {


    private final ProdutoRepository produtoRepository;
    private final CategoriaService categoriaService;

    public Produto salvar(ProdutoRepresentation.CreateOrUpdateProduto createOrUpdateProduto) {
        Categoria categoria = this.categoriaService.getCategoria(createOrUpdateProduto.getCategoria());
        Produto produto = Produto.builder()
                .nome(createOrUpdateProduto.getNome())
                .descricao(createOrUpdateProduto.getDescricao())
                .complemento(Strings.isEmpty(createOrUpdateProduto.getComplemento()) ? "": createOrUpdateProduto.getComplemento())
                .fabricante(createOrUpdateProduto.getFabricante())
                .fornecedor(Strings.isEmpty(createOrUpdateProduto.getFornecedor()) ? "": createOrUpdateProduto.getFornecedor())
                .qtde(createOrUpdateProduto.getQtde())
                .valor(createOrUpdateProduto.getValor())
                .unidadeMedida(createOrUpdateProduto.getUnidadeMedida())
                .categoria(categoria)
                .status(Produto.Status.ATIVO)
                .build();

        return this.produtoRepository.save(produto);
    }

    public Produto atualizar(Long id, ProdutoRepresentation.CreateOrUpdateProduto createOrUpdateProduto) {
        Produto produtoAntigo = this.buscarUm(id);

        Categoria categoria = this.categoriaService.getCategoria(createOrUpdateProduto.getCategoria());

        Produto produtoAtualizado = produtoAntigo.toBuilder()
                .nome(createOrUpdateProduto.getNome())
                .descricao(createOrUpdateProduto.getDescricao())
                .complemento(Strings.isEmpty(createOrUpdateProduto.getComplemento()) ? "": createOrUpdateProduto.getComplemento())
                .fabricante(createOrUpdateProduto.getFabricante())
                .fornecedor(Strings.isEmpty(createOrUpdateProduto.getFornecedor()) ? "": createOrUpdateProduto.getFornecedor())
                .qtde(createOrUpdateProduto.getQtde())
                .valor(createOrUpdateProduto.getValor())
                .unidadeMedida(createOrUpdateProduto.getUnidadeMedida())
                .categoria(categoria)
                .build();

        return this.produtoRepository.save(produtoAtualizado);
    }

    public List<Produto> buscarTodos(Predicate filter) {
        return this.produtoRepository.findAll(filter);
    }
    public Produto buscarUm(Long id) {
        BooleanExpression filter = QProduto.produto.id.eq(id)
                .and(QProduto.produto.status.eq(Produto.Status.ATIVO));
        return this.produtoRepository.findOne(filter).orElseThrow(() -> new NotFoundException("Produto não encontrado"));
    }

    public void deleteProduto(Long id) {
        Produto produto = this.buscarUm(id);
        produto.setStatus(Produto.Status.INATIVO);
        this.produtoRepository.save(produto);
    }

}

