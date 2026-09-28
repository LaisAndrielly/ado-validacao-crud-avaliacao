package br.senac.tads.dsw.exemplo3.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.senac.tads.dsw.exemplo3.model.Avaliacao;
import br.senac.tads.dsw.exemplo3.model.Produto;
import br.senac.tads.dsw.exemplo3.repository.AvaliacaoRepository;
import br.senac.tads.dsw.exemplo3.repository.ProdutoRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    private final AvaliacaoRepository avaliacaoRepository;
    private final ProdutoRepository produtoRepository;

    public AvaliacaoController(AvaliacaoRepository avaliacaoRepository,
                               ProdutoRepository produtoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.produtoRepository = produtoRepository;
    }

    @PostMapping("/produto/{produtoId}")
    public ResponseEntity<Avaliacao> criarAvaliacao(
            @PathVariable Long produtoId,
            @Valid @RequestBody Avaliacao avaliacao) {

        Optional<Produto> produto = produtoRepository.findById(produtoId);

        if (produto.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        avaliacao.setProduto(produto.get());
        Avaliacao avaliacaoSalva = avaliacaoRepository.save(avaliacao);

        URI location = URI.create("/api/avaliacoes/" + avaliacaoSalva.getId());

        return ResponseEntity.created(location).body(avaliacaoSalva);
    }

    @GetMapping
    public List<Avaliacao> listarTodos() {
        return avaliacaoRepository.findAll();
    }
    @GetMapping("/{id}")
public ResponseEntity<Avaliacao> buscarPorId(@PathVariable Long id) {
    return avaliacaoRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}
@PutMapping("/{id}")
public ResponseEntity<Avaliacao> atualizar(
        @PathVariable Long id,
        @Valid @RequestBody Avaliacao avaliacao) {

    if (!avaliacaoRepository.existsById(id)) {
        return ResponseEntity.notFound().build();
    }

    avaliacao.setId(id);
    Avaliacao avaliacaoAtualizada = avaliacaoRepository.save(avaliacao);

    return ResponseEntity.ok(avaliacaoAtualizada);
}
@DeleteMapping("/{id}")
public ResponseEntity<Void> excluir(@PathVariable Long id) {

    if (!avaliacaoRepository.existsById(id)) {
        return ResponseEntity.notFound().build();
    }

    avaliacaoRepository.deleteById(id);
    return ResponseEntity.noContent().build();
}
}