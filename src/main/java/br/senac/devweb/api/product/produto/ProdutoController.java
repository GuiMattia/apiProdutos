package br.senac.devweb.api.product.produto;

import br.senac.devweb.api.product.categoria.Categoria;
import br.senac.devweb.api.product.categoria.CategoriaRepresentation;
import br.senac.devweb.api.product.categoria.CategoriaService;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/produto")
@AllArgsConstructor
public class ProdutoController {

    private ProdutoService produtoService;
    private final CategoriaService categoriaService;

    @PostMapping("/")
    public ResponseEntity<ProdutoRepresentation.Detalhes> createProduto(
            @Valid @RequestBody ProdutoRepresentation.CreateOrUpdateProduto createOrUpdateProduto) {

        Categoria categoria = this.categoriaService.getCategoria(createOrUpdateProduto.getCategoria());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ProdutoRepresentation.Detalhes.from(this.produtoService.salvar(createOrUpdateProduto, categoria)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoRepresentation.Detalhes> atualizaProduto(
            @PathVariable("id") Long id, @Valid @RequestBody ProdutoRepresentation.CreateOrUpdateProduto createOrUpdateProduto, Categoria categoria) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ProdutoRepresentation.Detalhes.from(this.produtoService.atualizar(id, createOrUpdateProduto, categoria)));
    }

    @GetMapping("/")
    public ResponseEntity<List<ProdutoRepresentation.Lista>> buscarTodos() {

        BooleanExpression filter = QProduto.produto.status.eq(Produto.Status.ATIVO);
        return ResponseEntity.ok(ProdutoRepresentation.Lista.from(this.produtoService.buscarTodos(filter)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoRepresentation.Detalhes> buscarUmProduto(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ProdutoRepresentation.Detalhes.from(this.produtoService.buscarUm(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduto(@PathVariable("id") Long id) {
        System.out.println("Deletado com sucesso");
        this.produtoService.deleteProduto(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
