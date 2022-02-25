package com.edi.apirestful.controller;

import com.edi.apirestful.dto.FilmeDto;
import com.edi.apirestful.model.Filme;
import com.edi.apirestful.service.FilmeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/filmes")
public class FilmeController {

    @Autowired
    private FilmeService service;

    @PostMapping
    public ResponseEntity<Filme> save(@RequestBody Filme filme) {
        try {
            Filme save = service.save(filme);
            return ResponseEntity.ok(save);
        } catch (Exception e) {
            return ResponseEntity.badRequest().header("Erro ao salvar filme" + e.getMessage()).body(null);
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Filme> update(@RequestBody Filme filme, @PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.update(filme, id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().header("Erro ao editar filme " + e.getMessage() + ".").body(null);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Filme> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().header("Erro ao deletar filme " + e.getMessage() + ".").body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Filme> findById(@PathVariable Long id) {
        try {
            Filme byId = service.findById(id);
            return ResponseEntity.ok(byId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().header("Erro ao buscar filme por id " + e.getMessage() + ".").body(null);
        }
    }

    @GetMapping("/export/producers/max/interval")
    public ResponseEntity<FilmeDto> exportMaxInterval() {
        try {
            FilmeDto filmeDto = service.exportProducersMaxInterval();
            return new ResponseEntity<>(filmeDto, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().header("Erro ao exportar " + e.getMessage() + ".").body(null);
        }
    }

    @GetMapping("/export/producers/two/prizes/next")
    public ResponseEntity<FilmeDto> exportTwoPrizesNext() {
        try {
            FilmeDto dto = service.exportProducersTwoPrizesNext();
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().header("Erro ao exportar " + e.getMessage() + ".").body(null);
        }
    }
}
