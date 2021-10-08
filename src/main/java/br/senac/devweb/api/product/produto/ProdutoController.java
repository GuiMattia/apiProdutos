package br.senac.devweb.api.product.produto;

import br.senac.devweb.api.product.categoria.Categoria;
import br.senac.devweb.api.product.categoria.CategoriaRepresentation;
import br.senac.devweb.api.product.categoria.CategoriaService;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
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
    private ProdutoRepository produtoRepository;

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
    public ResponseEntity<List<ProdutoRepresentation.Lista>> buscarTodos(
            @QuerydslPredicate(root = Produto.class) Predicate filtroProduto,
            @RequestParam(name = "fitro", required = false, defaultValue = "") String filtro,
            @RequestParam(name = "paginaSelecionada", defaultValue = "0") Integer paginaSelecionada,
            @RequestParam(name = "tamanhoPagina", defaultValue = "2") Integer tamanhoPagina) {

        BooleanExpression filter = Strings.isEmpty(filtro) ? QProduto.produto.status.eq(Produto.Status.ATIVO) :
                QProduto.produto.status.eq(Produto.Status.ATIVO).and(QProduto.produto.descricao.containsIgnoreCase(filtro));

        Pageable pageRequest = PageRequest.of(paginaSelecionada, tamanhoPagina);

        Page<Produto> produtoList = this.produtoRepository.findAll(filter, pageRequest);

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
