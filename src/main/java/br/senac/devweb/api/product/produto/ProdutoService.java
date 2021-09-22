package br.senac.devweb.api.product.produto;

import br.senac.devweb.api.product.exceptions.NotFoundException;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProdutoService {


    private final ProdutoRepository produtoRepository;

    public Produto salvar() {
        return this.produtoRepository.save(new Produto());
    }

    public void atualizar(Long id) {
        Produto produto = this.buscarUm(id);

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

